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
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws SQLException {
        var reason = (String) args.getFirst();
        var action = ExpandedBans.Database.unLockdown()
                ? "updated"
                : "created";

        ExpandedBans.Database.lockdown(sender.getName(), reason);

        if (reason == null)
            $send(sender,
                  $translate(action + ".without-reason"));
        else $send(sender,
                   $translate(action + "with-reason").formatted(reason));
    }
}
