package pl.olafcio.expandedbans.database.traits;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.database.DBTrait;

import java.sql.SQLException;

public interface TPersona extends DBTrait {
    default void registerPersonaIP(String ip, String persona) throws SQLException {
        try (var statement = connection().prepareStatement(
                "INSERT INTO `personas` (ip, tag) VALUES (?, ?)"
        )) {
            statement.setString(1, ip);
            statement.setString(2, persona);

            statement.executeUpdate();
        }
    }

    default @Nullable String IP2Persona(String ip) throws SQLException {
        try (var statement = connection().prepareStatement(
                "SELECT tag FROM `personas` WHERE ip=?"
        )) {
            statement.setString(1, ip);

            try (var results = statement.executeQuery()) {
                if (results.next())
                    return results.getString(1);
                else return null;
            }
        }
    }

    default @Nullable String Persona2IP(String tag) throws SQLException {
        try (var statement = connection().prepareStatement(
                "SELECT ip FROM `personas` WHERE tag=? ORDER BY last_connected DESC LIMIT 1"
        )) {
            statement.setString(1, tag);

            try (var results = statement.executeQuery()) {
                if (results.next())
                    return results.getString(1);
                else return null;
            }
        }
    }

    default @NonNull ResultIterator Persona2IPs(String tag) throws SQLException {
        var statement = connection().prepareStatement(
                "SELECT ip, last_connected FROM `personas` WHERE tag=? ORDER BY last_connected DESC"
        );

        statement.setString(1, tag);
        return results(statement, statement::executeQuery);
    }
}
