package pl.olafcio.expandedbans.commands.impl.lockchat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;
import pl.olafcio.expandedbans.main.ChatLock;

import java.util.List;

public class XLockChat extends XCommand {
    public XLockChat() {
        super.name("xlockchat")
             .perm("expandedbans.lockchat")
             .then("reason", new StringArg(Argument.Type.REST));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException {
        var reason = (String) args.getFirst();

        ExpandedBans.ChatLock = new ChatLock(reason, sender.getName());
        Bukkit.getServer().broadcastMessage(ExpandedBans.Messages.lockchatNotify(
                ExpandedBans.ChatLock.reason(),
                ExpandedBans.ChatLock.by()
        ));

        if (reason == null)
            ExpandedBans.Messages.send(sender,
                    "§7Locked the chat.");
        else ExpandedBans.Messages.send(sender,
                "§7Locked the chat with the reason §e%s§7.".formatted(reason));
    }
}
