package pl.olafcio.expandedbans.commands.impl.nutes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XPunishmentCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XMuteIP extends XPunishmentCommand {
    public XMuteIP() {
        super.name("xmuteip")
             .perm("expandedbans.muteip")
             .then("target", new StringArg.Builder()
                                         .type(Argument.Type.REQUIRED)
                                         .tabcompleter(() -> Bukkit.getOnlinePlayers()
                                                 .stream()
                                                 .map(Player::getName)
                                         )
                                   .build())
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void punish(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var ip = (String) args.get(0);
        var reason = (String) args.get(1);

        var ipInfo = ipTarget(ip);
        var player = ipInfo.player();

        ip = ipInfo.ip();

        String action;
        var target = "I" + ip;
        var by = sender.getName();

        if (!ExpandedBans.Database.isBanned(target)) {
            action = "IP-muted";
            ExpandedBans.Database.mute(
                    target,
                    by,
                    reason,
                    null
            );

            ifOnline(player, plr -> ExpandedBans.Messages.muteNotify(
                    plr,
                    reason,
                    by
            ));
        } else {
            action = "Updated the mute for";
            ExpandedBans.Database.updateMute(
                    target,
                    by,
                    reason,
                    null
            );
        }

        ExpandedBans.Messages.send(sender,
                "§7%s §6%s§7.".formatted(action, player.getName()));
    }
}
