package pl.olafcio.expandedbans.commands.args.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.PatternError;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class IPTargetArg extends Argument<IPTargetArg.IPTarget> {
    public IPTargetArg(Type type) {
        super(type);
    }

    public static final Pattern IPv4_PATTERN = Pattern.compile("([0-9](|[0-9]){2}\\.){3}(|[0-9]){3}", Pattern.CASE_INSENSITIVE);
    public record IPTarget(
            @Nullable OfflinePlayer player,
            @NonNull String ip
    ) {
        public String getName() {
            if (player != null)
                return player.getName();
            else return ip;
        }

        public String getTarget() {
            return "I" + ip;
        }
    }

    public IPTarget parse(String input) throws PatternError {
        try {
            return _parseInternal(input);
        } catch (SQLException e) {
            throw new PatternError(e.getMessage());
        }
    }

    @ApiStatus.Internal
    public static IPTarget _parseInternal(String arg) throws PatternError, SQLException {
        OfflinePlayer player;
        String ip;

        if (IPv4_PATTERN.matcher(arg).find()) {
            var uuid = ExpandedBans.Database.getIP2Player(arg);

            player = uuid == null
                    ? null
                    : Bukkit.getOfflinePlayer(uuid);

            ip = arg;
        } else {
            player = Bukkit.getOfflinePlayer(arg);

            try { ip = ExpandedBans.Database.getPlayer2IP(player.getUniqueId()); }
            catch (SQLException e) {
                throw new PatternError("Database error.");
            }

            if (ip == null)
                throw new PatternError("Player never joined; cannot find the IP.");
        }

        return new IPTarget(player, ip);
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args, String arg) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(v -> v.startsWith(arg))
        .toList();
    }
}
