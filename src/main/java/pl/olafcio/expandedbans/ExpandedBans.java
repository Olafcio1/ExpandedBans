package pl.olafcio.expandedbans;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.olafcio.expandedbans.commands.*;
import pl.olafcio.expandedbans.commands.bans.XBan;
import pl.olafcio.expandedbans.commands.bans.XClearBans;
import pl.olafcio.expandedbans.commands.bans.XUnban;
import pl.olafcio.expandedbans.database.Database;
import pl.olafcio.expandedbans.main.Configurations;
import pl.olafcio.expandedbans.main.Plugin;
import pl.olafcio.expandedbans.messages.Messages;

import java.io.File;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class ExpandedBans extends JavaPlugin implements Listener {
    private static ExpandedBans INSTANCE;
    private static boolean initialized = false;

    public static Configurations Configurations;
    public static Messages Messages;
    public static Plugin Plugin;

    public static Database Database;
    private static Path db_path;

    public ExpandedBans() {
        if (initialized)
            throw new XBSingletonException("Can't initialize more than once");

        initialized = true;
    }

    public static ExpandedBans getInstance() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        INSTANCE = this;

        Configurations = new Configurations();
        Messages = new Messages();
        Plugin = new Plugin();

        Plugin.Logger = getLogger();

        Configurations.Messages = config("messages.yml");
        Configurations.Punishments = config("punishments.yml");

        db_path = getDataFolder().toPath().resolve("database.sqlite3");
    }

    private YamlConfiguration config(String path) {
        var file = new File(getDataFolder(), path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource(path, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    // If changing the plugin's API base (Paper/Forge, etc.), change this!
    private YamlConfiguration getPluginYML() {
        var reader = Objects.requireNonNull(getTextResource("plugin.yml"));
        var config = YamlConfiguration.loadConfiguration(reader);

        return config;
    }

    @Override
    public void onEnable() {
        var config = getPluginYML();
        var section = Objects.requireNonNull(config.getConfigurationSection("commands"));

        Database = new Database(db_path);
        Plugin.Commands = section.getKeys(false).stream().map(this::getCommand).toList();

        getCommand("expandedbans").setExecutor(new XExpandedBans());
        getCommand("xban").setExecutor(new XBan());
        getCommand("xunban").setExecutor(new XUnban());
        getCommand("xclearbans").setExecutor(new XClearBans());

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Database.close();
    }

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            ResultSet ban;
            if ((ban = Database.getBan("P" + event.getUniqueId())) != null)
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Messages.ban(
                        Bukkit.getOfflinePlayer(event.getUniqueId()),
                        ban.getString(2),
                        ban.getString(3)
                ));
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check player's ban state on connect", e);
        }
    }
}
