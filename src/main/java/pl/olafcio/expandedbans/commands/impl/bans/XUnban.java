package pl.olafcio.expandedbans.commands.impl.bans;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class XUnban extends XCommand {
    public XUnban() {
        super.name("xunban")
             .then("player", arg(
                Argument.Type.REQUIRED,
                Bukkit::getOfflinePlayer,
                null
             ))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        if (!sender.hasPermission("expandedbans.unban")) {
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Insufficient permissions.");
            return;
        }

        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        try {
            var target = "P" + player.getUniqueId();
            if (!ExpandedBans.Database.removeBan(target, reason)) {
                ExpandedBans.Messages.send(sender,
                        "§cError:§6 %s§4 is not banned.".formatted(player.getName()));

                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Database error.");

            return;
        }

        if (reason == null) {
            ExpandedBans.Messages.send(sender,
                    "§7Unbanned §6%s§7.".formatted(player.getName()));
        } else {
            ExpandedBans.Messages.send(sender,
                    "§7Unbanned §6%s§7 with the reason §o%s.".formatted(player.getName(), reason));
        }
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args) throws SQLException {
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
    }
}
