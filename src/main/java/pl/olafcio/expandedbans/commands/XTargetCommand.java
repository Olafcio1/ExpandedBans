package pl.olafcio.expandedbans.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.args.PatternError;
import pl.olafcio.expandedbans.commands.args.impl.IPTargetArg;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public abstract class XTargetCommand extends XCommand {
    @Override
    protected final void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException {
        try {
            apply(sender, command, label, args);
        } catch (SQLException e) {
            e.printStackTrace();
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Database error.");
        }
    }

    protected abstract void apply(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException;

    protected final IPTargetArg.IPTarget ipTarget(String arg) throws CommandMessageException, SQLException {
        try {
            return IPTargetArg._parseInternal(arg);
        } catch (PatternError e) {
            throw new CommandMessageException(e.getMessage());
        }
    }

    protected final void ifOnline(@Nullable OfflinePlayer player, Consumer<Player> handler) {
        if (player != null && player.isOnline())
            handler.accept((Player) player);
    }

    @Deprecated(forRemoval = true)
    protected final String getTargetForIP(String ip) {
        return "I" + ip;
    }

    protected final String getTargetForPlayer(OfflinePlayer player) {
        return "U" + player.getUniqueId();
    }
}
