package pl.olafcio.expandedbans.commands.impl.nutes;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.AnyPlayerArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XUnmute extends XTargetCommand {
    public XUnmute() {
        super.name("xunmute")
             .perm("expandedbans.unmute")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        var target = getTargetForPlayer(player);
        if (!ExpandedBans.Database.removeMute(target, reason))
            throw new CommandMessageException("§6%s§4 is not muted.".formatted(player.getName()));

        if (reason == null) {
            ExpandedBans.Messages.send(sender,
                               "§7Unmuted §6%s§7.".formatted(player.getName()));
        } else {
            ExpandedBans.Messages.send(sender,
                               "§7Unmuted §6%s§7 with the reason §o%s.".formatted(player.getName(), reason));
        }
    }
}
