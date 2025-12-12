package pl.olafcio.expandedbans.commands.bans;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;

import java.sql.SQLException;

public class XClearBans implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("xclearbans")) {
            try {
                ExpandedBans.Database.clearBans(args.length > 0 ? String.join(" ", args) : null);
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                                   "§7Cleared all bans.");
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                        "§cError:§4 Database error.");

                return true;
            }

            return true;
        }

        return false;
    }
}
