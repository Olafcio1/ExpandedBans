package pl.olafcio.expandedbans.commands.impl.mute;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XMuteClear extends XTargetCommand {
    public XMuteClear() {
        super.name("xmuteclear")
             .perm("expandedbans.muteclear")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void apply(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        ExpandedBans.Database.clearMutes((String) args.getFirst());
        ExpandedBans.Messages.send(sender,
                           "§7All mutes have been removed.");
    }
}
