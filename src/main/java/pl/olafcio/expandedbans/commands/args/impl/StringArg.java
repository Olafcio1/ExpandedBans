package pl.olafcio.expandedbans.commands.args.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.PatternError;
import pl.olafcio.expandedbans.commands.args.function.Completer;

import java.util.List;
import java.util.regex.Pattern;

public class StringArg extends Argument<String> {
    private final Pattern pattern;
    private final Completer tabcompleter;

    public StringArg(Type type) {
        this(type, null, null);
    }

    protected StringArg(Type type, Pattern pattern, Completer tabcompleter) {
        super(type);

        this.pattern = pattern;
        this.tabcompleter = tabcompleter;
    }

    public static class Builder {
        private Type type = Type.REQUIRED;
        private Pattern pattern = null;
        private Completer tabcompleter = null;

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder regex(Pattern pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder tabcompleter(Completer tabcompleter) {
            this.tabcompleter = tabcompleter;
            return this;
        }

        public StringArg build() {
            return new StringArg(type, pattern, tabcompleter);
        }
    }

    @Override
    public String parse(String input) throws PatternError {
        if (pattern != null && !pattern.matcher(input).find())
            throw new PatternError("didn't match the given part");

        return input;
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args, String arg) {
        if (tabcompleter == null)
            return List.of();
        else return tabcompleter.complete()
                .filter(v -> v.startsWith(arg))
        .toList();
    }
}
