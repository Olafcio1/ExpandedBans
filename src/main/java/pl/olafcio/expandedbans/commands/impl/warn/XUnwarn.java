package pl.olafcio.expandedbans.commands.impl.warn;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class XUnwarn extends XTargetCommand {
    public XUnwarn() {
        super.name("xunwarn")
             .perm("expandedbans.unwarn")
             .then("player", arg(
                Argument.Type.REQUIRED,
                Bukkit::getOfflinePlayer,
                null
             ))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        var target = getTargetForPlayer(player);
        if (!ExpandedBans.Database.removeWarns(target, reason))
            throw new CommandMessageException($translate("not-warned").formatted(player.getName()));

        if (reason == null) {
            $send(sender,
                  $translate("success.without-reason").formatted(player.getName()));
        } else {
            $send(sender,
                  $translate("success.with-reason").formatted(player.getName(), reason));
        }
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args) throws SQLException {
        return ExpandedBans.Database.bans()
                .map(r -> {
                    try { return r.getString(1); }
                    catch (SQLException e) { throw new XBDatabaseException("Failed to extract a nick from a ban entry", e); }
                })
                .filter(t -> t.startsWith("U"))
                .map(t -> t.substring(1))
                .map(UUID::fromString)
                .map(Bukkit::getOfflinePlayer)
                .map(OfflinePlayer::getName)
        .toList();
    }
}
