package pl.olafcio.expandedbans.database;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.XBDatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.stream.Stream;

public interface DBPunishmentTrait extends DBTrait {
    /**
     * Inserts a punishment entry.
     * @param target A target can be either:
     *               <ul>
     *                 <li>{@code "P" + uuid}, for a player</li>
     *                 <li>{@code "I" + ip},   for an IP address</li>
     *               </ul>
     * @param reason The punishment reason. May be {@code null} which shows the default message for this punishment.
     * @param expires The time when the punishment expires. Can be calculated by adding {@code System.currentTimeMillis() + durationInMs}
     */
    default void punish(
            String table,
            @NonNull String target,
            @NonNull String by,
            @Nullable String reason,
            @Nullable Long expires
    ) throws SQLException {
        var stmt = connection().prepareStatement("INSERT INTO `%s` (target, reason, by, expires) VALUES (?, ?, ?, ?)".formatted(table));
        stmt.setString(1, target);
        stmt.setString(3, by);

        if (reason == null)
            stmt.setNull(2, Types.LONGNVARCHAR);
        else stmt.setString(2, reason);

        if (expires == null)
            stmt.setNull(4, Types.BIGINT);
        else stmt.setLong(4, expires);

        stmt.executeUpdate();
    }

    default Stream<ResultSet> punishments(String table) throws SQLException {
        statement().execute(
                "SELECT * FROM `%s`".formatted(table)
        );

        var results = statement().getResultSet();
        if (!results.next())
            return Stream.empty();

        return Stream.iterate(results, set -> {
            try {
                return set.next();
            } catch (SQLException e) {
                throw new XBDatabaseException("Failed to proceed to the next row", e);
            }
        }, set -> set);
    }

    default void updatePunishment(
            String table,
            @NonNull String target,
            @NonNull String by,
            @Nullable String reason,
            @Nullable Long expires
    ) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "UPDATE `%s` SET reason=?, by=?, expires=? WHERE target=?".formatted(table)
        )) {
            stmt.setString(4, target);
            stmt.setString(2, by);

            if (reason == null)
                stmt.setNull(1, Types.LONGNVARCHAR);
            else stmt.setString(1, reason);

            if (expires == null)
                stmt.setNull(3, Types.BIGINT);
            else stmt.setLong(3, expires);

            stmt.executeUpdate();
        }
    }

    default boolean removePunishment(
            String table,
            @NonNull String target,
            @Nullable String reason
    ) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "DELETE FROM `%s` WHERE target=?".formatted(table)
        )) {
            stmt.setString(1, target);
            return stmt.executeUpdate() >= 1;
        }
    }

    default int clearPunishments(
            String table,
            @Nullable String reason
    ) throws SQLException {
        return statement().executeUpdate(
                "DELETE FROM `%s`".formatted(table)
        );
    }

    default @Nullable ResultSet punishments(
            String table,
            @NonNull String target
    ) throws SQLException {
        var stmt = connection().prepareStatement(
                "SELECT * FROM `%s` WHERE `target`=?".formatted(table)
        );

        stmt.setString(1, target);
        stmt.execute();

        var res = stmt.getResultSet();
        if (res.next()) {
            return res;
        } else return null;
    }

    default boolean isPunished(
            String table,
            @NonNull String target
    ) throws SQLException {
        try (var res = punishments(table, target)) {
            return res != null;
        }
    }
}
