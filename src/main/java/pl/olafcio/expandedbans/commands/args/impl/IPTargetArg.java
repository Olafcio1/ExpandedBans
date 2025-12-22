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
import java.util.UUID;
import java.util.regex.Pattern;

public class IPTargetArg extends Argument<IPTargetArg.IPTarget> {
    public IPTargetArg(Type type) {
        super(type);
    }

    public static final Pattern IPv4_PATTERN = Pattern.compile("^([0-9](|[0-9]){2}\\.){3}(|[0-9]){3}$", Pattern.CASE_INSENSITIVE);
    public record IPTarget(
            @Nullable OfflinePlayer player,
            @NonNull String ip,
            @NonNull String persona
    ) {
        public String getName() {
            if (player != null)
                return player.getName();
            else return ip;
        }

        public String getTarget() {
            return "P" + persona;
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
        String persona;

        if (IPv4_PATTERN.matcher(arg).find()) {
            ip = arg;
            persona = ExpandedBans.Database.IP2Persona(arg);

            if (persona == null) {
                persona = UUID.randomUUID().toString();
                ExpandedBans.Database.registerPersonaIP(ip, persona);
            }

            var uuid = ExpandedBans.Database.Persona2Player(persona);
            player = uuid == null
                    ? null
                    : Bukkit.getOfflinePlayer(uuid);
        } else {
            player = Bukkit.getOfflinePlayer(arg);
            persona = ExpandedBans.Database.Player2Persona(player.getUniqueId());

            if (persona == null)
                throw new PatternError("Player never joined; cannot find the IP.");

            ip = ExpandedBans.Database.Persona2IP(persona);
            assert ip != null;
        }

        return new IPTarget(player, ip, persona);
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
