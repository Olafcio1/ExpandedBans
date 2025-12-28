package pl.olafcio.expandedbans.commands.impl.kick;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XKickAll extends XTargetCommand {
    public XKickAll() {
        super.name("xkickall")
             .perm("expandedbans.kickall")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var reason = (String) args.get(0);
        var players = Bukkit.getOnlinePlayers();

        for (var player : players)
            player.kickPlayer($Messages.kickAll(player, reason, sender.getName()));

        if (reason == null) {
            $send(sender,
                  $translate("without-reason").formatted(players.size()));
        } else {
            $send(sender,
                  $translate("with-reason").formatted(players.size(), reason));
        }
    }
}
