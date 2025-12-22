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

public class XUnbanIP extends XTargetCommand {
    public XUnbanIP() {
        super.name("xunbanip")
             .perm("expandedbans.unbanip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var ipInfo = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var target = ipInfo.getTarget();
        var nick = ipInfo.getName();
        var by = sender.getName();

        if (!ExpandedBans.Database.isBanned(target))
            throw new CommandMessageException($translate("not-banned").formatted(
                    nick
            ));

        ExpandedBans.Database.removeBan(
                target,
                reason
        );

        if (reason == null)
            $send(sender, $translate("success.without-reason").formatted(
                  nick, by));
        else $send(sender, $translate("success.with-reason").formatted(
                   nick, by,
                   reason));
    }
}
