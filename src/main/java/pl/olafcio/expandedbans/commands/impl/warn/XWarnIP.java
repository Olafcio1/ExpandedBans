package pl.olafcio.expandedbans.commands.impl.warn;

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

public class XWarnIP extends XTargetCommand {
    public XWarnIP() {
        super.name("xwarnip")
             .perm("expandedbans.warnip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var ipInfo = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var target = ipInfo.getTarget();
        var by = sender.getName();

        ExpandedBans.Database.warn(
                target,
                by,
                reason,
                null
        );

        var player = ipInfo.player();
        if (player != null)
            ifOnline(player, plr -> plr.sendMessage($Messages.warnIP(
                    player,
                    reason,
                    by
            )));

        $send(sender, $translate("success").formatted(
                ipInfo.getName(),
                Objects.requireNonNullElse(reason, $Notifications.getString("ban-ip.default-reason"))
        ));
    }
}
