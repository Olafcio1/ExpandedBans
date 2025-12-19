package pl.olafcio.expandedbans.commands.impl.bans;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XPunishmentCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XBanClear extends XPunishmentCommand {
    public XBanClear() {
        super.name("xbanclear")
             .perm("expandedbans.banclear")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void punish(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        ExpandedBans.Database.clearBans((String) args.getFirst());
        ExpandedBans.Messages.send(sender,
                           "§7All bans have been removed.");
    }
}
