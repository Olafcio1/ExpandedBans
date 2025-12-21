package pl.olafcio.expandedbans.commands.impl.lockchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;

import java.util.List;

public class XUnLockChat extends XCommand {
    public XUnLockChat() {
        super.name("xunlockchat")
             .perm("expandedbans.unlockchat")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException {
        if (ExpandedBans.ChatLock == null)
            throw new CommandMessageException("The chat isn't locked.");

        var reason = (String) args.getFirst();
        ExpandedBans.ChatLock = null;

        if (reason == null)
            ExpandedBans.Messages.send(sender,
                    "§7Unlocked the chat.");
        else ExpandedBans.Messages.send(sender,
                "§7Unlocked the chat with the reason §e%s§7.".formatted(reason));
    }
}
