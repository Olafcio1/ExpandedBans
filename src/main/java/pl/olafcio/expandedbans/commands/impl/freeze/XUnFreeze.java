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

public class XUnFreeze extends XTargetCommand {
    public XUnFreeze() {
        super.name("xunfreeze")
             .perm("expandedbans.unfreeze")
             .then("player", new PlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var player = (Player) args.get(0);
        var reason = (String) args.get(1);

        if (player == null)
            throw new CommandMessageException("Player not found.");

        var uuid = player.getUniqueId();

        ExpandedBans.Players.get(uuid).setFrozen(false);
        ExpandedBans.Database.unfreeze("U" + uuid);

        if (reason == null) {
            ExpandedBans.Messages.send(sender,
                    "§7Unfroze §6%s§7.".formatted(player.getName()));
        } else {
            ExpandedBans.Messages.send(sender,
                    "§7Unfroze §6%s§7 for §e%s§7.".formatted(player.getName(), reason));
        }
    }
}
