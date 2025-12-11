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
                    .map(Command::getName);

            sender.sendMessage("§3[ExpandedBans]§7 Made by §2Olafcio§7 with §4♥");
            if (cmds.findAny().isPresent())
                sender.sendMessage(
                        "§3[ExpandedBans]§7 Available commands: [" +
                        cmds.collect(Collectors.joining(", ")) +
                        "]"
                );

            return true;
        }

        return false;
    }
}
