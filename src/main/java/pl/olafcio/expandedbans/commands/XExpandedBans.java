package pl.olafcio.expandedbans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;

public class XExpandedBans implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("expandedbans")) {
            var cmds = ExpandedBans.INSTANCE.commands.stream()
                    .filter(x -> {
                        var perm = x.getPermission();
                        return perm != null && sender.hasPermission(perm);
                    })
                    .map(Command::getName)
            .toList();

            sender.sendMessage(ExpandedBans.INSTANCE.configurations.messages.getString("prefix") +
                                "§7Made by §2Olafcio§7 with §4♥");

            if (!cmds.isEmpty())
                sender.sendMessage(
                        ExpandedBans.INSTANCE.configurations.messages.getString("prefix") +
                        "§7Available commands: §7[§8" +
                        String.join(", ", cmds) +
                        "§7]"
                );

            return true;
        }

        return false;
    }
}
