package pl.olafcio.expandedbans.commands.impl.warn;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.AnyPlayerArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class XWarn extends XTargetCommand {
    public XWarn() {
        super.name("xwarn")
             .perm("expandedbans.warn")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        var target = getTargetForPlayer(player);
        var by = sender.getName();

        ExpandedBans.Database.warn(
                target,
                by,
                reason,
                null
        );

        ifOnline(player, plr -> plr.sendMessage(ExpandedBans.Messages.warn(
                player,
                reason,
                by
        )));

        ExpandedBans.Messages.send(sender, "§7Warned §6%s§7 for §e%s§7.".formatted(
                player.getName(),
                Objects.requireNonNullElse(reason, ExpandedBans.Configurations.Punishments.getString("ban.default-reason"))
        ));
    }
}
