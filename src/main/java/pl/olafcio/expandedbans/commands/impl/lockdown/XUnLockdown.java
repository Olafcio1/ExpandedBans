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
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var reason = (String) args.getFirst();
        if (!ExpandedBans.Database.unLockdown())
            throw new CommandMessageException($translate("no-lockdown"));

        if (reason == null)
            $send(sender,
                  $translate("success.without-reason"));
        else $send(sender,
                   $translate("success.with-reason").formatted(reason));
    }
}
