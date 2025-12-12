package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.XBDatabaseException;
import pl.olafcio.expandedbans.database.DBTrait;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.stream.Stream;

public interface TBan extends DBTrait {
    /**
     * Inserts a ban entry.
     * @param target A target can be either:
     *               <ul>
     *                 <li>{@code "P" + uuid}, for a player</li>
     *                 <li>{@code "I" + ip},   for an IP address</li>
     *               </ul>
     * @param reason The ban reason. May be {@code null} which shows the default ban message.
     * @param expires The time when the ban expires. Can be calculated by adding {@code System.currentTimeMillis() + banDurationInMs}
     */
    default void ban(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        var stmt = connection().prepareStatement("INSERT INTO `bans` (target, reason, by, expires) VALUES (?, ?, ?, ?)");
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

    default Stream<ResultSet> bans() throws SQLException {
        statement().execute(
                "SELECT * FROM `bans`"
        );

        var results = statement().getResultSet();
        var ref = new Object() {
            boolean proceed = true;
        };

        return Stream.iterate(results, x -> ref.proceed, set -> {
            try {
                if (ref.proceed = set.next())
                    return set;
                else return null;
            } catch (SQLException e) {
                throw new XBDatabaseException("Failed to proceed to the next row", e);
            }
        });
    }

    default void updateBan(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "UPDATE `bans` SET reason=?, by=?, expires=? WHERE target=?"
        )) {
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
    }

    default boolean removeBan(@NonNull String target, @Nullable String reason) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "DELETE FROM `bans` WHERE target=?"
        )) {
            stmt.setString(1, target);
            return stmt.executeUpdate() >= 1;
        }
    }

    default int clearBans(@Nullable String reason) throws SQLException {
        return statement().executeUpdate(
                "DELETE FROM `bans`"
        );
    }

    default @Nullable ResultSet getBan(@NonNull String target) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "SELECT * FROM `bans` WHERE `target`=?"
        )) {
            stmt.setString(1, target);
            stmt.execute();

            var res = stmt.getResultSet();
            if (res.next()) {
                return res;
            } else return null;
        }
    }
}
