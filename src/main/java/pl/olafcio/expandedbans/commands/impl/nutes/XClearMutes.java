package pl.olafcio.expandedbans.commands.impl.nutes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XPunishmentCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XClearMutes extends XPunishmentCommand {
    public XClearMutes() {
        super.name("xclearmutes")
             .perm("expandedbans.clearmutes")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void punish(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        ExpandedBans.Database.clearMutes((String) args.getFirst());
        ExpandedBans.Messages.send(sender,
                           "§7Cleared all nutes.");
    }
}
