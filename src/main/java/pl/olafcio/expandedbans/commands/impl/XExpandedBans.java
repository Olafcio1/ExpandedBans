package pl.olafcio.expandedbans.commands.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.EnumArg;

import java.util.List;
import java.util.Objects;

public class XExpandedBans extends XCommand {
    public XExpandedBans() {
        super.name("expandedbans")
             .perm("expandedbans")
             .then("arg", new EnumArg.Builder<>()
                     .type(Argument.Type.OPTIONAL)
                     .with("", "help", "reload")
             .build());
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        var arg = args.get(0);
        if (Objects.equals(arg, "reload")) {
            reload(sender);
        } else if (Objects.equals(arg, "help")) {
            help(sender);
        } else {
            about(sender);
        }
    }

    private static void about(CommandSender sender) {
        var cmds = ExpandedBans.Plugin.Commands.stream()
                .filter(x -> {
                    var perm = x.getPermission();
                    return perm != null && sender.hasPermission(perm);
                })
                .map(Command::getName)
        .toList();

        $send(sender,
              "§7Made by §2Olafcio§7 with §4🖤");

        if (!cmds.isEmpty())
            $send(sender,
                    "§7Available commands: §7[§8" +
                    String.join(", ", cmds) +
                    "§7]"
            );
    }

    private static void reload(CommandSender sender) {
        if (!sender.hasPermission("expandedbans.reload")) {
            $send(sender,
                  "§cError: §4No permission.");

            return;
        }

        var start = System.currentTimeMillis();
        ExpandedBans.getInstance().reloadConfigurations();
        var diff = System.currentTimeMillis() - start;

        $send(sender,
              "§7Reloaded in §2" + diff + "ms§7.");
    }

    private static void help(CommandSender sender) {
        $send(sender,
              "§7Available commands:");

        for (var cmd : ExpandedBans.Plugin.Commands) {
            var perm = cmd.getPermission();
            if (perm != null && sender.hasPermission(perm)) {
                $send(sender,
                      "§7› §3" + cmd.getName());
            }
        }
    }
}
