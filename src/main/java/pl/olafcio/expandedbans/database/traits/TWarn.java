package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBPunishmentTrait;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public interface TWarn extends DBPunishmentTrait {
    /**
     * Inserts a warn entry.
     * @param target A target can be either:
     *               <ul>
     *                 <li>{@code "U" + uuid},    for a player</li>
     *                 <li>{@code "P" + persona}, for a persona</li>
     *               </ul>
     * @param reason The ban reason. May be {@code null} which shows the default warn message.
     * @param expires The time when the warn expires. Can be calculated by adding {@code System.currentTimeMillis() + durationInMs}
     */
    default void warn(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        punish("warns", target, by, reason, expires);
    }

    default Stream<ResultSet> warns() throws SQLException {
        return punishments("warns");
    }

    default @Nullable ResultSet warns(@NonNull String target) throws SQLException {
        return punishments("warns", target);
    }

    default void updateWarn(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        updatePunishment("warns", target, by, reason, expires);
    }

    default boolean removeWarn(@NonNull String target, @Nullable String reason) throws SQLException {
        return removePunishment("warns", target, reason);
    }

    default int clearWarns(@Nullable String reason) throws SQLException {
        return clearPunishments("warns", reason);
    }
}
