package pl.olafcio.expandedbans.commands.impl.warn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XWarnClear extends XTargetCommand {
    public XWarnClear() {
        super.name("xwarnclear")
             .perm("expandedbans.warnclear")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        ExpandedBans.Database.clearWarns((String) args.getFirst());
        ExpandedBans.Messages.send(sender,
                           "§7All warns have been removed.");
    }
}
