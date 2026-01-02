package pl.olafcio.expandedbans.commands.impl.ban;

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

public class XBanIP extends XTargetCommand {
    public XBanIP() {
        super.name("xbanip")
             .perm("expandedbans.banip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var ipInfo = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var target = ipInfo.getTarget();
        var by = sender.getName();

        String format;

        if (!ExpandedBans.Database.isBanned(target)) {
            format = $translate("created");

            ExpandedBans.Database.ban(
                    target,
                    by,
                    reason,
                    null
            );

            var players = ipInfo.players();
            forPlayer(players, plr -> plr.kickPlayer($Messages.banIP(
                    plr,
                    reason,
                    by
            )));
        } else {
            format = $translate("updated");

            ExpandedBans.Database.updateBan(
                    target,
                    by,
                    reason,
                    null
            );
        }

        $send(sender, format.formatted(
                ipInfo.getName(),
                Objects.requireNonNullElse(reason, $Notifications.getString("ban-ip.default-reason"))
        ));
    }
}
