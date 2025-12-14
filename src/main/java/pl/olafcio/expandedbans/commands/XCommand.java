package pl.olafcio.expandedbans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBCommandDefinitionException;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.PatternError;
import pl.olafcio.expandedbans.commands.args.function.Tabcompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class XCommand implements CommandExecutor, TabExecutor {
    private @MonotonicNonNull
            String name;
    private @NonNull
            String usage;
    private @Nullable
            String permission;

    private final ArrayList<Argument<?>> arguments;

    protected XCommand() {
        this.usage = "";
        this.permission = null;
        this.arguments = new ArrayList<>();
    }

    public XCommand name(String name) {
        this.name = name;
        return this;
    }

    public XCommand perm(String permission) {
        this.permission = permission;
        return this;
    }

    private boolean declaredRest = false;
    private boolean declaredOptional = false;
    public XCommand then(String name, Argument<?> arg) {
        if (declaredRest)
            throw new XBCommandDefinitionException("Cannot declare an argument after a rest argument");

        String uSingle = null;
        if (arg.type == Argument.Type.REQUIRED) {
            if (declaredOptional)
                throw new XBCommandDefinitionException("Cannot declare required argument after optional argument");

            uSingle = "<%s>";
        } else if (arg.type == Argument.Type.OPTIONAL) {
            declaredOptional = true;
            uSingle = "[<%s>]";
        } else if (arg.type == Argument.Type.REST) {
            declaredRest = true;
            uSingle = "%s...";
        }

        assert uSingle != null;

        arguments.add(arg);
        usage += " §n%s§7§o".formatted(uSingle).formatted(name);

        return this;
    }

    public static <T> Argument<T> arg(
            Argument.Type type,
            Function<String, T> parser,
            Tabcompleter tabcompleter
    ) {
        return new Argument<>(type) {
            @Override
            public T parse(String input) {
                return parser.apply(input);
            }

            @Override
            public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args, String arg) {
                if (tabcompleter == null)
                    return List.of();
                else return tabcompleter.apply(sender, command, label, args, arg);
            }
        };
    }

    protected abstract void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException;

    @Override
    @SuppressWarnings("NullableProblems")
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (matches(command)) {
            if (permission != null && !sender.hasPermission(permission)) {
                ExpandedBans.Messages.send(
                        sender,
                        "§cError:§4 Insufficient permissions."
                );
                return true;
            }

            var parsed = new ArrayList<>();
            var index = 0;

            for (var arg : arguments) {
                try {
                    String input = null;
                    if (arg.type == Argument.Type.REQUIRED) {
                        input = args[index];
                    } else if (arg.type == Argument.Type.OPTIONAL) {
                        if (args.length > index)
                            input = args[index];
                    } else if (arg.type == Argument.Type.REST) {
                        input = String.join(" ", Arrays.copyOfRange(args, index, args.length));
                    }

                    if (Objects.equals(input, ""))
                        parsed.add(null);
                    else parsed.add(arg.parse(input));
                } catch (ArrayIndexOutOfBoundsException e) {
                    ExpandedBans.Messages.send(sender,
                                       "§7Usage: §o/%s%s".formatted(name, usage));

                    return true;
                } catch (PatternError e) {
                    ExpandedBans.Messages.send(sender,
                                       "§7Invalid syntax.");

                    return true;
                }

                index++;
            }

            try {
                execute(sender, command, label, parsed);
            } catch (CommandMessageException e) {
                ExpandedBans.Messages.send(sender,
                        "§cError:§4 " + e.getMessage());
            }

            return true;
        }

        return false;
    }

    private boolean erroredTC = false;

    @Override
    @SuppressWarnings("NullableProblems")
    public final List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (matches(command)) {
            try {
                return tabcomplete(sender, command, label, args);
            } catch (Exception e) {
                if (!erroredTC) {
                    erroredTC = true;

                    e.printStackTrace();
                    ExpandedBans.Plugin.Logger.info("Failed to suggest '%s' completions".formatted(name));
                }

                return List.of();
            }
        }

        return null;
    }

    protected List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args) throws Exception {
        var index = Math.max(0, args.length - 1);
        if (arguments.size() <= index)
            return List.of();

        var arg = arguments.get(index);
        return arg.tabcomplete(sender, command, label, args, args[index]);
    }

    private boolean matches(Command command) {
        assert name != null;
        return command.getName().equals(name);
    }
}
