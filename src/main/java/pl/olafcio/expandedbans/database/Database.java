package pl.olafcio.expandedbans.database;

import org.bukkit.Bukkit;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.database.traits.TBan;

import java.sql.*;

public final class Database implements TBan {
    private Connection connection;
    private Statement statement;

    private final Thread startThread;

    public Database() {
        (startThread = new Thread(() -> {
            try {
                connect();

                while (true) {
                    while (!connection.isClosed());
                    connect();
                }
            } catch (SQLException e) {
                close();
                Bukkit.getPluginManager().disablePlugin(ExpandedBans.INSTANCE);

                throw new RuntimeException(e);
            }
        })).start();
    }

    private void connect() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:%s".formatted(ExpandedBans.INSTANCE.db_path));
        this.statement = connection.createStatement();
        this.statement.setQueryTimeout(30);

        setup();
    }

    public void setup() throws SQLException {
        this.statement.executeUpdate("CREATE TABLE `bans` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING, by STRING NOT NULL, expires BIGINT)");
        this.statement.executeUpdate("CREATE TABLE `mutes` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING, by STRING NOT NULL, expires BIGINT)");
        this.statement.executeUpdate("CREATE TABLE `warns` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING, by STRING NOT NULL, expires BIGINT)");
        this.statement.executeUpdate("CREATE TABLE `notes` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING, by STRING NOT NULL)");
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
