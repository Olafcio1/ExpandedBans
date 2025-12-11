package pl.olafcio.expandedbans;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;
    private Statement statement;

    private final Thread startThread;

    Database() {
        (startThread = new Thread(() -> {
            try {
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
        this.statement.executeUpdate("CREATE TABLE `bans` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING, expires DATE)");
        this.statement.executeUpdate("CREATE TABLE `mutes` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING, expires DATE)");
        this.statement.executeUpdate("CREATE TABLE `warns` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING, expires DATE)");
        this.statement.executeUpdate("CREATE TABLE `notes` IF DOES NOT EXIST (target STRING NOT NULL, reason STRING)");
    }

//    public

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
