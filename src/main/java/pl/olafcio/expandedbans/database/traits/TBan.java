package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBPunishmentTrait;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public interface TBan extends DBPunishmentTrait {
    /**
     * Inserts a ban entry.
     * @param target A target can be either:
     *               <ul>
     *                 <li>{@code "U" + uuid},    for a player</li>
     *                 <li>{@code "P" + persona}, for a persona</li>
     *               </ul>
     * @param reason The ban reason. May be {@code null} which shows the default ban message.
     * @param expires The time when the ban expires. Can be calculated by adding {@code System.currentTimeMillis() + durationInMs}
     */
    default void ban(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        punish("bans", target, by, reason, expires);
    }

    default Stream<ResultSet> bans() throws SQLException {
        return punishments("bans");
    }

    default @Nullable ResultSet getBan(@NonNull String target) throws SQLException {
        return punishments("bans", target);
    }

    default boolean isBanned(@NonNull String target) throws SQLException {
        return isPunished("bans", target);
    }

    default void updateBan(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        updatePunishment("bans", target, by, reason, expires);
    }

    default boolean removeBan(@NonNull String target, @Nullable String reason) throws SQLException {
        return removePunishment("bans", target, reason);
    }

    default int clearBans(@Nullable String reason) throws SQLException {
        return clearPunishments("bans", reason);
    }
}
