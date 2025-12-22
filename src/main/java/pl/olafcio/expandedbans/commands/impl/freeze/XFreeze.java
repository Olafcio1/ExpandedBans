package pl.olafcio.expandedbans.commands.impl.freeze;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.PlayerArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XFreeze extends XTargetCommand {
    public XFreeze() {
        super.name("xfreeze")
             .perm("expandedbans.freeze")
             .then("player", new PlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var player = (Player) args.get(0);
        var reason = (String) args.get(1);

        if (player == null)
            throw new CommandMessageException($translate("unknown-player"));

        var uuid = player.getUniqueId();

        ExpandedBans.Players.get(uuid).setFrozen(true);
        ExpandedBans.Database.freeze("U" + uuid, reason, sender.getName());

        if (reason == null) {
            $send(sender,
                  $translate("success.without-reason").formatted(player.getName()));
        } else {
            $send(sender,
                  $translate("success.with-reason").formatted(player.getName(), reason));
        }
    }
}
