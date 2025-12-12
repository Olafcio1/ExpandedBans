package pl.olafcio.expandedbans.commands.bans;

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
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                    "§cError:§4 Insufficient permissions.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                    "§7Usage: §o/ban §n<player>§7§o §n[<reason>]");
                return true;
            }

            var player = Bukkit.getServer().getOfflinePlayer(args[0]);
            var reason = args.length >= 2 ? args[1] : null;

            String action;
            try {
                var target = "P" + player.getUniqueId();
                if (ExpandedBans.Database.getBan(target) == null) {
                    action = "Banned";
                    ExpandedBans.Database.ban(
                            target,
                            sender.getName(),
                            reason,
                            null
                    );
                } else {
                    action = "Updated the ban for";
                    ExpandedBans.Database.updateBan(
                            target,
                            sender.getName(),
                            reason,
                            null
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                    "§cError:§4 Database error.");
                return true;
            }

            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                               "§7%s §6%s§7.".formatted(action, player.getName()));

            return true;
        }

        return false;
    }
}
