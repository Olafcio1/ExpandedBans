package pl.olafcio.expandedbans.commands.impl.nutes;

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

public class XMute extends XCommand {
    public XMute() {
        super.name("xmute")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        if (!sender.hasPermission("expandedbans.mute")) {
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

            if (!ExpandedBans.Database.isMuted(target)) {
                action = "Muted";
                ExpandedBans.Database.mute(
                        target,
                        by,
                        reason,
                        null
                );

                if (player.isOnline())
                    ((Player) player).sendMessage(ExpandedBans.Messages.muteNotify(
                            player,
                            reason,
                            by
                    ));
            } else {
                action = "Updated the mute for";
                ExpandedBans.Database.updateMute(
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
