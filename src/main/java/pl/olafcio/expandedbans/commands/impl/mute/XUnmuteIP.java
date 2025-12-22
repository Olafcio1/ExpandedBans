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

public class XUnmuteIP extends XTargetCommand {
    public XUnmuteIP() {
        super.name("xunmuteip")
             .perm("expandedbans.unmuteip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var ipInfo = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var target = ipInfo.getTarget();
        var player = ipInfo.player();

        if (!ExpandedBans.Database.removeMute(target, reason)) {
            $send(sender,
                  $translate("not-ipmuted").formatted(ipInfo.getName()));

            return;
        }

        if (reason == null) {
            $send(sender,
                  $translate("success.without-reason").formatted(ipInfo.getName()));
        } else {
            $send(sender,
                  $translate("success.with-reason").formatted(ipInfo.getName(), reason));
        }
    }
}
