package pl.olafcio.expandedbans.commands.impl.bans;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XClearBans extends XCommand {
    public XClearBans() {
        super.name("xclearbans")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        try {
            ExpandedBans.Database.clearBans((String) args.getFirst());
            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                               "§7Cleared all bans.");
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(ExpandedBans.Configurations.Messages.getString("prefix") +
                    "§cError:§4 Database error.");
        }
    }
}
