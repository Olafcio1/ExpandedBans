package pl.olafcio.expandedbans.commands.impl.mutes;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.AnyPlayerArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XMute extends XTargetCommand {
    public XMute() {
        super.name("xmute")
             .perm("expandedbans.mute")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        var target = getTargetForPlayer(player);
        var by = sender.getName();

        String action;

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

        ExpandedBans.Messages.send(sender,
                "§7%s §6%s§7.".formatted(action, player.getName()));
    }
}
