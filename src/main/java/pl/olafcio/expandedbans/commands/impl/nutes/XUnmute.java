package pl.olafcio.expandedbans.commands.impl.nutes;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.AnyPlayerArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XUnmute extends XCommand {
    public XUnmute() {
        super.name("xunmute")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        if (!sender.hasPermission("expandedbans.unmute")) {
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Insufficient permissions.");
            return;
        }

        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        try {
            var target = "P" + player.getUniqueId();
            if (!ExpandedBans.Database.removeMute(target, reason)) {
                ExpandedBans.Messages.send(sender,
                        "§cError:§6 %s§4 is not banned.".formatted(player.getName()));

                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Database error.");

            return;
        }

        if (reason == null) {
            ExpandedBans.Messages.send(sender,
                               "§7Unmuted §6%s§7.".formatted(player.getName()));
        } else {
            ExpandedBans.Messages.send(sender,
                               "§7Unmuted §6%s§7 with the reason §o%s.".formatted(player.getName(), reason));
        }
    }
}
