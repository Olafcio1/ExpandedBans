package pl.olafcio.expandedbans.commands.bans;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class XUnban implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("xunban")) {
            if (!sender.hasPermission("expandedbans.unban")) {
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                    "§cError:§4 Insufficient permissions.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                    "§7Usage: §o/unban §n<player>§7§o §n[<reason>]");
                return true;
            }

            var player = Bukkit.getServer().getOfflinePlayer(args[0]);
            var reason = args.length >= 2 ? args[1] : null;

            try {
                var target = "P" + player.getUniqueId();
                if (!ExpandedBans.Database.removeBan(target, reason)) {
                    sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                       "§cError:§6 %s§4 is not banned.".formatted(player.getName()));

                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                    "§cError:§4 Database error.");

                return true;
            }

            if (reason == null) {
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                   "§7Unbanned §6%s§7.".formatted(player.getName()));
            } else {
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                   "§7Unbanned §6%s§7 with the reason §o%s.".formatted(player.getName(), reason));
            }

            return true;
        }

        return false;
    }

    boolean printedTCstacktrace = false;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("xunban")) {
            try {
                return ExpandedBans.Database.bans()
                        .map(r -> {
                            try { return r.getString(1); }
                            catch (SQLException e) { throw new XBDatabaseException("Failed to extract a nick from a ban entry", e); }
                        })
                        .filter(t -> t.startsWith("P"))
                        .map(t -> t.substring(1))
                        .map(UUID::fromString)
                        .map(Bukkit::getOfflinePlayer)
                        .map(OfflinePlayer::getName)
                .toList();
            } catch (SQLException e) {
                if (!printedTCstacktrace) {
                    printedTCstacktrace = true;

                    e.printStackTrace();
                    ExpandedBans.Plugin.Logger.info("Failed to suggest 'xunban' completions");
                }

                return List.of();
            }
        }

        return null;
    }
}
