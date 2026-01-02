package pl.olafcio.expandedbans.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.commands.args.PatternError;
import pl.olafcio.expandedbans.commands.args.impl.IPTargetArg;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public abstract class XTargetCommand extends XCommand {
    @Deprecated
    protected final IPTargetArg.IPTarget ipTarget(String arg) throws CommandMessageException, SQLException {
        try {
            return IPTargetArg._parseInternal(arg);
        } catch (PatternError e) {
            throw new CommandMessageException(e.getMessage());
        }
    }

    protected final void ifOnline(@Nullable OfflinePlayer player, @NonNull Consumer<Player> handler) {
        if (player != null && player.isOnline())
            handler.accept((Player) player);
    }

    protected final void forPlayer(@NonNull List<OfflinePlayer> players, @NonNull Consumer<Player> handler) {
        players.forEach(player -> {
            if (player != null && player.isOnline())
                handler.accept((Player) player);
        });
    }

    protected final String getTargetForPlayer(OfflinePlayer player) {
        return "U" + player.getUniqueId();
    }
}
