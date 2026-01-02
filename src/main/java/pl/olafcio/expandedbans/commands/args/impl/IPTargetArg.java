package pl.olafcio.expandedbans.commands.args.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.PatternError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IPTargetArg extends Argument<IPTargetArg.IPTarget> {
    public IPTargetArg(Type type) {
        super(type);
    }

    public static final Pattern IPv4_PATTERN = Pattern.compile("^([0-9](|[0-9]){2}\\.){3}(|[0-9]){3}$", Pattern.CASE_INSENSITIVE);
    public record IPTarget(
            @NonNull List<@NonNull OfflinePlayer> players,
            @NonNull List<@NonNull String> ips,
            @NonNull String persona
    ) {
        public String getName() {
            if (!players.isEmpty())
                return players.stream()
                              .map(OfflinePlayer::getName)
                              .collect(Collectors.joining(" / "));
            else return ips.get(0);
        }

        public String getTarget() {
            return "P" + persona;
        }

        public boolean contains(Player player) {
            return players.contains(player);
        }

        public boolean contains(String ip) {
            return ips.contains(ip);
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
        var players = new ArrayList<OfflinePlayer>();
        var ips = new ArrayList<String>();

        String persona;

        if (IPv4_PATTERN.matcher(arg).find()) {
            persona = ExpandedBans.Database.IP2Persona(arg);

            if (persona == null) {
                persona = UUID.randomUUID().toString();
                ExpandedBans.Database.registerPersonaIP(arg, persona);
            }
        } else {
            var player = Bukkit.getOfflinePlayer(arg);
            persona = ExpandedBans.Database.Player2Persona(player.getUniqueId());

            if (persona == null)
                throw new PatternError("Player never joined; cannot find the IP.");
        }

        try (var results = ExpandedBans.Database.Persona2Players(persona)) {
            while (results.next()) {
                var uuid = UUID.fromString(results.getString(1));
                var player = Bukkit.getOfflinePlayer(uuid);

                players.add(player);
            }
        }

        try (var results = ExpandedBans.Database.Persona2IPs(persona)) {
            while (results.next()) {
                var ip = results.getString(1);
                ips.add(ip);
            }
        }

        return new IPTarget(players, ips, persona);
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
