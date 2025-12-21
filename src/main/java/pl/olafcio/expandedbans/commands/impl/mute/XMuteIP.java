package pl.olafcio.expandedbans.commands.impl.mute;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.IPTargetArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XMuteIP extends XTargetCommand {
    public XMuteIP() {
        super.name("xmuteip")
             .perm("expandedbans.muteip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var ipInfo = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var player = ipInfo.player();
        var target = ipInfo.getTarget();

        var by = sender.getName();
        String action;

        if (!ExpandedBans.Database.isBanned(target)) {
            action = "IP-muted";
            ExpandedBans.Database.mute(
                    target,
                    by,
                    reason,
                    null
            );

            ifOnline(player, plr -> ExpandedBans.Messages.muteIPNotify(
                    plr,
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
