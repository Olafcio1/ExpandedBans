package pl.olafcio.expandedbans.database;

import org.bukkit.Bukkit;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.database.traits.TBan;

import java.nio.file.Path;
import java.sql.*;
import java.util.function.Consumer;

public final class Database implements TBan {
    private Connection connection;
    private Statement statement;

    private final Thread startThread;
    private final Path path;

    public Database(Path path) {
        Consumer<Throwable> handle = e -> {
            close();
            Bukkit.getPluginManager().disablePlugin(ExpandedBans.getInstance());

            throw new RuntimeException(e);
        };

        this.path = path;

        try { connect(); }
        catch (SQLException e) { handle.accept(e); }

        (startThread = new Thread(() -> {
            try {
                while (true) {
                    while (!connection.isClosed())
                        Thread.sleep(1000);

                    connect();
                }
            } catch (SQLException e) {
                handle.accept(e);
            } catch (InterruptedException e) {
                ExpandedBans.Plugin.Logger.info("Stopped database reconnection thread");
            }
        })).start();
    }

    private void connect() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:%s".formatted(path));
        this.statement = connection.createStatement();
        this.statement.setQueryTimeout(30);

        setup();
    }

    public void setup() throws SQLException {
        this.statement.executeUpdate("CREATE TABLE IF NOT EXISTS `bans` (target STRING NOT NULL, reason STRING, by STRING NOT NULL, expires BIGINT)");
        this.statement.executeUpdate("CREATE TABLE IF NOT EXISTS `mutes` (target STRING NOT NULL, reason STRING, by STRING NOT NULL, expires BIGINT)");
        this.statement.executeUpdate("CREATE TABLE IF NOT EXISTS `warns` (target STRING NOT NULL, reason STRING, by STRING NOT NULL, expires BIGINT)");
        this.statement.executeUpdate("CREATE TABLE IF NOT EXISTS `notes` (target STRING NOT NULL, reason STRING, by STRING NOT NULL)");
    }

    public Connection connection() {
        return connection;
    }

    @Override
    public Statement statement() {
        return statement;
    }

    public void close() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        startThread.interrupt();
    }
}
