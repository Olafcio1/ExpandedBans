package pl.olafcio.expandedbans.commands.impl.bans;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.AnyPlayerArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XBan extends XCommand {
    public XBan() {
        super.name("xban")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED))
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        if (!sender.hasPermission("expandedbans.ban")) {
            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                    "§cError:§4 Insufficient permissions.");
            return;
        }

        if (args.isEmpty()) {
            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                    "§7Usage: §o/ban §n<player>§7§o §n[<reason>]");
            return;
        }

        var player = (OfflinePlayer) args.get(0);
        var reason = (String) args.get(1);

        String action;
        try {
            var target = "P" + player.getUniqueId();
            if (ExpandedBans.Database.getBan(target) == null) {
                action = "Banned";
                ExpandedBans.Database.ban(
                        target,
                        sender.getName(),
                        reason,
                        null
                );
            } else {
                action = "Updated the ban for";
                ExpandedBans.Database.updateBan(
                        target,
                        sender.getName(),
                        reason,
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                    "§cError:§4 Database error.");

            return;
        }

        sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                "§7%s §6%s§7.".formatted(action, player.getName()));
    }
}
