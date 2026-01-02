package pl.olafcio.expandedbans.commands.impl.kick;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.IPTargetArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XKickIP extends XTargetCommand {
    public XKickIP() {
        super.name("xkickip")
             .perm("expandedbans.kickip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var target = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var any = false;
        var players = Bukkit.getOnlinePlayers();
        for (var player : players) {
            var ip = player.getAddress().getHostString();
            if (target.contains(ip)) {
                player.kickPlayer($Messages.kick(player, reason, sender.getName()));
                any = true;
            }
        }

        if (!any) {
            $send(sender,
                  $translate("no-matches").formatted(target.getName()));
            return;
        }

        if (reason == null) {
            $send(sender,
                  $translate("success.without-reason").formatted(target.getName()));
        } else {
            $send(sender,
                  $translate("success.with-reason").formatted(target.getName(), reason));
        }
    }
}
