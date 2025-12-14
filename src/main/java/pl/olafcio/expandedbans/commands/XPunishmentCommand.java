package pl.olafcio.expandedbans.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public abstract class XPunishmentCommand extends XCommand {
    @Override
    protected final void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException {
        try {
            punish(sender, command, label, args);
        } catch (SQLException e) {
            e.printStackTrace();
            ExpandedBans.Messages.send(sender,
                    "§cError:§4 Database error.");
        }
    }

    protected abstract void punish(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException;

    protected static final Pattern IPv4_PATTERN = Pattern.compile("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}", Pattern.CASE_INSENSITIVE);
    protected final IPTarget ipTarget(String arg) throws CommandMessageException {
        OfflinePlayer player;
        String ip;

        if (IPv4_PATTERN.matcher(arg).find()) {
            player = null;
            ip = arg;
        } else {
            player = Bukkit.getOfflinePlayer(arg);

            try { ip = ExpandedBans.Database.getIpByPlayer(player.getUniqueId()); }
            catch (SQLException e) {
                throw new CommandMessageException("Database error.");
            }

            if (ip == null)
                throw new CommandMessageException("Player never joined; cannot find the IP.");
        }

        return new IPTarget(player, ip);
    }

    protected record IPTarget(
            @Nullable OfflinePlayer player,
            @NonNull String ip
    ) {
        public String getName() {
            if (player != null)
                return player.getName();
            else return ip;
        }
    }

    protected final void ifOnline(@Nullable OfflinePlayer player, Consumer<Player> handler) {
        if (player != null && player.isOnline())
            handler.accept((Player) player);
    }

    protected final String getTargetForIP(String ip) {
        return "I" + ip;
    }

    protected final String getTargetForPlayer(OfflinePlayer player) {
        return "P" + player.getUniqueId();
    }
}
