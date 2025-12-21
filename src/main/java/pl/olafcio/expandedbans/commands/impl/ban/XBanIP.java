package pl.olafcio.expandedbans.commands.impl.ban;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.IPTargetArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class XBanIP extends XTargetCommand {
    public XBanIP() {
        super.name("xbanip")
             .perm("expandedbans.banip")
             .then("target", new IPTargetArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws SQLException, CommandMessageException {
        var ipInfo = (IPTargetArg.IPTarget) args.get(0);
        var reason = (String) args.get(1);

        var target = ipInfo.getTarget();
        var by = sender.getName();

        String action;
        String reasonPtr;

        if (!ExpandedBans.Database.isBanned(target)) {
            action = "IP-banned";
            reasonPtr = "for";

            ExpandedBans.Database.ban(
                    target,
                    by,
                    reason,
                    null
            );

            var player = ipInfo.player();
            if (player != null)
                ifOnline(player, plr -> plr.kickPlayer(ExpandedBans.Messages.banIP(
                        player,
                        reason,
                        by
                )));
        } else {
            action = "Updated the IP-ban for";
            reasonPtr = "to";

            ExpandedBans.Database.updateBan(
                    target,
                    by,
                    reason,
                    null
            );
        }

        ExpandedBans.Messages.send(sender, "§7%s §6%s§7 %s §e%s§7.".formatted(
                action, ipInfo.getName(),
                reasonPtr, Objects.requireNonNullElse(reason, ExpandedBans.Configurations.Punishments.getString("ban-ip.default-reason"))
        ));
    }
}
