package pl.olafcio.expandedbans.commands.impl.lockdown;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.sql.SQLException;
import java.util.List;

public class XUnLockdown extends XCommand {
    public XUnLockdown() {
        super.name("xunlockdown")
             .perm("expandedbans.unlockdown")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException {
        var reason = (String) args.getFirst();

        try {
            if (ExpandedBans.Database.unLockdown()) {
                if (reason == null)
                    ExpandedBans.Messages.send(sender,
                            "§7Removed the current lockdown.");
                else ExpandedBans.Messages.send(sender,
                            "§7Removed the current lockdown with the reason §e%s§7.".formatted(reason));
            } else {
                throw new CommandMessageException("There is currently no lockdown active.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Database error.");
        }
    }
}
