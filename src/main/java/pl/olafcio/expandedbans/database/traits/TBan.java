package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBTrait;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public interface TBan extends DBTrait {
    /**
     * Inserts a ban entry.
     * @param target A target can be either:
     *               <ul>
     *                 <li>{@code "P" + uuid}, for a player</li>
     *                 <li>{@code "I" + ip},   for an IP address</li>
     *               </ul>
     * @param reason The ban reason. May be `null` which shows the default ban message.
     * @param expires The time when the ban expires. Can be calculated by adding {@code System.currentTimeMillis() + banDurationInMs}
     */
    default void ban(@NonNull String target, @NonNull String by, @Nullable String reason, @Nullable Long expires) throws SQLException {
        var stmt = connection().prepareStatement("INSERT INTO `bans` (target, reason, by, expires) VALUES (%s, %s, %s, %s)");
        stmt.setString(0, target);
        stmt.setString(3, by);

        if (reason == null)
            stmt.setNull(1, Types.LONGNVARCHAR);
        else stmt.setString(1, reason);

        if (expires == null)
            stmt.setNull(2, Types.BIGINT);
        else stmt.setLong(2, expires);

        stmt.executeUpdate();
    }

    default @Nullable ResultSet getBan(@NonNull String target) throws SQLException {
        var stmt = connection().prepareStatement("SELECT * FROM `bans` WHERE `target`=%s");
        stmt.setString(0,  target);
        stmt.execute();
        var res = stmt.getResultSet();
        if (res.next()) {
            return res;
        } else return null;
    }
}
