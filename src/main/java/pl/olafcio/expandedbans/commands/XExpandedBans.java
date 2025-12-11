package pl.olafcio.expandedbans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;

import java.util.stream.Collectors;

public class XExpandedBans implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("expandedbans")) {
            var cmds = ExpandedBans.INSTANCE.commands.stream()
                    .filter(x -> sender.hasPermission(x.getPermission()))
                    .map(Command::getName)
            .toList();

            sender.sendMessage(ExpandedBans.INSTANCE.configurations.messages.getString("prefix") +
                                "§7Made by §2Olafcio§7 with §4♥");

            if (!cmds.isEmpty())
                sender.sendMessage(
                        ExpandedBans.INSTANCE.configurations.messages.getString("prefix") +
                        "§7Available commands: §8[" +
                        cmds.stream()
                            .map(x -> "§7" + x + "§8")
                            .collect(Collectors.joining(", ")) +
                        "§8]"
                );

            return true;
        }

        return false;
    }
}
