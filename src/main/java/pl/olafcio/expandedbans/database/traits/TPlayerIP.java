package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBPunishmentTrait;

import java.sql.SQLException;
import java.util.UUID;

public interface TPlayerIP extends DBPunishmentTrait {
    default void registerPlayerIp(UUID uuid, String ip) throws SQLException {
        try (var statement = connection().prepareStatement(
                "INSERT INTO `player-ip` (uuid, ip) VALUES (?, ?)"
        )) {
            statement.setString(1, uuid.toString());
            statement.setString(2, ip);

            statement.executeUpdate();
        }
    }

    default @Nullable String getIpByPlayer(UUID uuid) throws SQLException {
        try (var statement = connection().prepareStatement(
                "SELECT ip FROM `player-ip` WHERE uuid=?"
        )) {
            statement.setString(1, uuid.toString());
            System.out.println(statement);

            try (var results = statement.executeQuery()) {
                if (results.next())
                    return results.getString(1);
                else return null;
            }
        }
    }
}
