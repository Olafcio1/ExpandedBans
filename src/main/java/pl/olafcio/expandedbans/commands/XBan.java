package pl.olafcio.expandedbans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;

import java.sql.SQLException;

public class XBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("xban")) {
            if (!sender.hasPermission("expandedbans.ban")) {
                sender.sendMessage(ExpandedBans.INSTANCE.configurations.messages.getString("prefix") +
                                    "§cError:§4 Insufficient permissions.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ExpandedBans.INSTANCE.configurations.messages.getString("prefix") +
                                    "§7Usage: §o/ban §n<player>§7§o §n[<reason>]");
                return true;
            }

            var target = Bukkit.getServer().getOfflinePlayer(args[0]);
            var reason = args.length >= 2 ? args[1] : null;

            try {
                ExpandedBans.INSTANCE.database.ban(
                        "P" + target.getUniqueId(),
                        sender.getName(),
                        reason,
                        null
                );
            } catch (SQLException e) {
                sender.sendMessage(ExpandedBans.INSTANCE.configurations.messages.getString("prefix") +
                                    "§cError:§4 Database error.");
                return true;
            }

            return true;
        }

        return false;
    }
}
