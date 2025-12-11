package pl.olafcio.expandedbans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;

import java.util.stream.Collectors;

public class XBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("ban")) {
            if (!sender.hasPermission("expandedbans.ban")) {
                sender.sendMessage("");
            }
            if (args.length == 0) {
                sender.sendMessage("§7 Usage:");
                return true;
            }

            return true;
        }

        return false;
    }
}
