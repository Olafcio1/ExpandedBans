package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBTrait;

import java.sql.SQLException;

public interface TFreeze extends DBTrait {
    default void freeze(@NonNull String target, @Nullable String reason, @NonNull String by) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "INSERT INTO freezes (target, reason, by) VALUES (?, ?, ?)"
        )) {
            stmt.setString(1, target);
            stmt.setString(2, reason);
            stmt.setString(3, by);

            stmt.executeUpdate();
        }
    }

    default boolean isFrozen(@NonNull String target) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "SELECT 1 FROM freezes WHERE target=?"
        )) {
            stmt.setString(1, target);
            try (var res = stmt.executeQuery()) {
                return res.next();
            }
        }
    }

    default void unfreeze(@NonNull String target) throws SQLException {
        try (var stmt = connection().prepareStatement(
                "DELETE FROM freezes WHERE target=?"
        )) {
            stmt.setString(1, target);
            stmt.executeUpdate();
        }
    }
}
