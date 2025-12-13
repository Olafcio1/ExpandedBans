package pl.olafcio.expandedbans.commands.impl.bans;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.AnyPlayerArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XBan extends XCommand {
    public XBan() {
        super.name("xban")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        if (!sender.hasPermission("expandedbans.ban")) {
            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                    "§cError:§4 Insufficient permissions.");
            return;
        }

        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        String action;
        try {
            var target = "P" + player.getUniqueId();
            var by = sender.getName();

            if (!ExpandedBans.Database.isBanned(target)) {
                action = "Banned";
                ExpandedBans.Database.ban(
                        target,
                        by,
                        reason,
                        null
                );

                if (player.isOnline())
                    ((Player) player).kickPlayer(ExpandedBans.Messages.ban(
                            player,
                            reason,
                            by
                    ));
            } else {
                action = "Updated the ban for";
                ExpandedBans.Database.updateBan(
                        target,
                        by,
                        reason,
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                    "§cError:§4 Database error.");

            return;
        }

        sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                "§7%s §6%s§7.".formatted(action, player.getName()));
    }
}
