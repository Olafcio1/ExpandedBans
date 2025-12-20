package pl.olafcio.expandedbans.commands.impl.nutes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XPunishmentCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.IPTargetArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XUnmuteIP extends XPunishmentCommand {
    public XUnmuteIP() {
        super.name("xunmuteip")
             .perm("expandedbans.unmuteip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void punish(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var target = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var ipt = target.getTarget();
        var player = target.player();

        if (!ExpandedBans.Database.removeMute(ipt, reason)) {
            ExpandedBans.Messages.send(sender,
                    "§cError:§6 %s§4 is not IP-muted.".formatted(target.getName()));

            return;
        }

        if (reason == null) {
            ExpandedBans.Messages.send(sender,
                               "§7Removed the IP mute for §6%s§7.".formatted(target.getName()));
        } else {
            ExpandedBans.Messages.send(sender,
                               "§7Removed the IP mute for §6%s§7 with the reason §o%s.".formatted(target.getName(), reason));
        }
    }
}
