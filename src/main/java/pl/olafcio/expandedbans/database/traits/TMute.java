package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBPunishmentTrait;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public interface TMute extends DBPunishmentTrait {
    /**
     * Inserts a mute entry.
     * @param target A target can be either:
     *               <ul>
     *                 <li>{@code "P" + uuid}, for a player</li>
     *                 <li>{@code "I" + ip},   for an IP address</li>
     *               </ul>
     * @param reason The ban reason. May be {@code null} which shows the default mute message.
     * @param expires The time when the mute expires. Can be calculated by adding {@code System.currentTimeMillis() + durationInMs}
     */
    default void mute(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        punish("mutes", target, by, reason, expires);
    }

    default Stream<ResultSet> mutes() throws SQLException {
        return punishments("mutes");
    }

    default @Nullable ResultSet getMute(@NonNull String target) throws SQLException {
        return punishments("mutes", target);
    }

    default boolean isMuted(@NonNull String target) throws SQLException {
        return isPunished("mutes", target);
    }

    default void updateMute(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        updatePunishment("mutes", target, by, reason, expires);
    }

    default boolean removeMute(@NonNull String target, @Nullable String reason) throws SQLException {
        return removePunishment("mutes", target, reason);
    }

    default int clearMutes(@Nullable String reason) throws SQLException {
        return clearPunishments("mutes", reason);
    }
}
