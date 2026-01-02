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
import java.util.Objects;

public class XMuteIP extends XTargetCommand {
    public XMuteIP() {
        super.name("xmuteip")
             .perm("expandedbans.muteip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var ipInfo = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var players = ipInfo.players();
        var target = ipInfo.getTarget();

        var by = sender.getName();
        String format;

        if (!ExpandedBans.Database.isBanned(target)) {
            format = $translate("created");
            ExpandedBans.Database.mute(
                    target,
                    by,
                    reason,
                    null
            );

            forPlayer(players, plr -> $Messages.muteIPNotify(
                    plr,
                    reason,
                    by
            ));
        } else {
            format = $translate("updated");
            ExpandedBans.Database.updateMute(
                    target,
                    by,
                    reason,
                    null
            );
        }

        $send(sender, format.formatted(
                ipInfo.getName(),
                Objects.requireNonNullElse(reason, $Notifications.getString("mute-ip.default-reason"))
        ));
    }
}
