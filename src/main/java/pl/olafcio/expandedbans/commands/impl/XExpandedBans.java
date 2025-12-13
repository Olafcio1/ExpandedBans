package pl.olafcio.expandedbans.commands.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.util.List;

public class XExpandedBans extends XCommand {
    public XExpandedBans() {
        super.name("expandedbans")
             .then("void", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        var cmds = ExpandedBans.Plugin.Commands.stream()
                .filter(x -> {
                    var perm = x.getPermission();
                    return perm != null && sender.hasPermission(perm);
                })
                .map(Command::getName)
        .toList();

        sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                            "§7Made by §2Olafcio§7 with §4🖤");

        if (!cmds.isEmpty())
            sender.sendMessage(
                    ExpandedBans.Configurations.Messages.getString("prefix") +
                    "§7Available commands: §7[§8" +
                    String.join(", ", cmds) +
                    "§7]"
            );
    }
}
