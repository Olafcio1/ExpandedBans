package pl.olafcio.expandedbans.commands.impl.lockdown;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XLockdown extends XCommand {
    public XLockdown() {
        super.name("xlockdown")
             .perm("expandedbans.lockdown")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) {
        var reason = (String) args.getFirst();

        try {
            ExpandedBans.Database.unLockdown();
            ExpandedBans.Database.lockdown(sender.getName(), reason);
        } catch (SQLException e) {
            e.printStackTrace();
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Database error.");

            return;
        }

        if (reason == null)
            ExpandedBans.Messages.send(sender,
                    "§7Applied a lockdown with the default reason.");
        else ExpandedBans.Messages.send(sender,
                     "§7Applied a lockdown with the reason §e%s§7.".formatted(reason));
    }
}
