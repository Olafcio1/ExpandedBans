package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBTrait;

import java.sql.SQLException;
import java.util.UUID;

public interface TPlayers extends DBTrait {
    default void registerPlayerPersona(UUID uuid, String persona) throws SQLException {
        try (var statement = connection().prepareStatement(
                "INSERT INTO `players` (uuid, tag) VALUES (?, ?)"
        )) {
            statement.setString(1, uuid.toString());
            statement.setString(2, persona);

            statement.executeUpdate();
        }
    }

    default @Nullable String Player2Persona(UUID uuid) throws SQLException {
        try (var statement = connection().prepareStatement(
                "SELECT tag FROM `players` WHERE uuid=? LIMIT 1"
        )) {
            statement.setString(1, uuid.toString());

            try (var results = statement.executeQuery()) {
                if (results.next())
                    return results.getString(1);
                else return null;
            }
        }
    }

    default @Nullable UUID Persona2Player(String tag) throws SQLException {
        try (var statement = connection().prepareStatement(
                "SELECT uuid FROM `players` WHERE tag=? ORDER BY last_connected DESC LIMIT 1"
        )) {
            statement.setString(1, tag);

            try (var results = statement.executeQuery()) {
                if (results.next())
                    return UUID.fromString(results.getString(1));
                else return null;
            }
        }
    }

    default @NonNull ResultIterator Persona2Players(String tag) throws SQLException {
        var statement = connection().prepareStatement(
                "SELECT uuid, last_connected FROM `players` WHERE tag=? ORDER BY last_connected DESC"
        );

        statement.setString(1, tag);
        return results(statement, statement::executeQuery);
    }
}
