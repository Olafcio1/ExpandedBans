package pl.olafcio.expandedbans.commands.impl.nutes;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class XMuteIP extends XCommand {
    public XMuteIP() {
        super.name("xmuteip")
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
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        if (!sender.hasPermission("expandedbans.muteip")) {
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Insufficient permissions.");
            return;
        }

        var ip = (String) args.get(0);
        var reason = (String) args.get(1);

        OfflinePlayer player = null;

        var pattern = Pattern.compile("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}", Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(ip).find()) {
            var nick = ip;
            player = Bukkit.getOfflinePlayer(nick);

            try { ip = ExpandedBans.Database.getIpByPlayer(player.getUniqueId()); }
            catch (SQLException e) {
                ExpandedBans.Messages.send(sender,
                                   "§cError:§4 Database error.");
                return;
            }

            if (ip == null) {
                ExpandedBans.Messages.send(sender,
                                   "§cError:§4 Player never joined; cannot find the IP.");
                return;
            }
        }

        String action;
        try {
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

                if (player != null && player.isOnline())
                    ((Player) player).sendMessage(ExpandedBans.Messages.muteNotify(
                            player,
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
        } catch (SQLException e) {
            e.printStackTrace();
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Database error.");

            return;
        }

        ExpandedBans.Messages.send(sender,
                "§7%s §6%s§7.".formatted(action, player.getName()));
    }
}
