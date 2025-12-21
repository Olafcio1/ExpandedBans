package pl.olafcio.expandedbans.commands.impl.bans;

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

public class XBan extends XTargetCommand {
    public XBan() {
        super.name("xban")
             .perm("expandedbans.ban")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        var target = getTargetForPlayer(player);
        var by = sender.getName();

        String action;
        String reasonPtr;

        if (!ExpandedBans.Database.isBanned(target)) {
            action = "Banned";
            reasonPtr = "for";

            ExpandedBans.Database.ban(
                    target,
                    by,
                    reason,
                    null
            );

            ifOnline(player, plr -> plr.kickPlayer(ExpandedBans.Messages.ban(
                    player,
                    reason,
                    by
            )));
        } else {
            action = "Updated the ban for";
            reasonPtr = "to";

            ExpandedBans.Database.updateBan(
                    target,
                    by,
                    reason,
                    null
            );
        }

        ExpandedBans.Messages.send(sender, "§7%s §6%s§7 %s §e%s§7.".formatted(
                action, player.getName(),
                reasonPtr, Objects.requireNonNullElse(reason, ExpandedBans.Configurations.Punishments.getString("ban.default-reason"))
        ));
    }
}
