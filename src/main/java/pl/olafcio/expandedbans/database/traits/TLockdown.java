package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBTrait;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TLockdown extends DBTrait {
    default void lockdown(@NonNull String by, @Nullable String reason) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "INSERT INTO `lockdowns` (by, reason, active) VALUES (?, ?, TRUE)"
        )) {
            stmt.setString(1, by);
            stmt.setString(2, reason);

            stmt.execute();
        }
    }

    default boolean unLockdown() throws SQLException {
        return statement().executeUpdate("UPDATE `lockdowns` SET active=FALSE WHERE active=TRUE") >= 1;
    }

    default @Nullable ResultSet getLockdown() throws SQLException {
        var res = statement().executeQuery("SELECT by, reason, at FROM `lockdowns` WHERE active=TRUE LIMIT 1");
        if (res.next())
            return res;
        else return null;
    }
}
