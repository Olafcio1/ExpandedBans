package pl.olafcio.expandedbans;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.olafcio.expandedbans.commands.*;
import pl.olafcio.expandedbans.database.Database;
import pl.olafcio.expandedbans.messages.Messages;

import java.io.File;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public final class ExpandedBans extends JavaPlugin implements Listener {
    public static ExpandedBans INSTANCE;

    public static class Configurations {
        private Configurations() {}

        public YamlConfiguration messages;
        public YamlConfiguration punishments;
    }

    public Configurations configurations;
    public Messages messages;

    public Path db_path;
    public Database database;
    public List<PluginCommand> commands;

    @Override
    public void onLoad() {
        INSTANCE = this;

        configurations = new Configurations();
        messages = new Messages();

        configurations.messages = config("messages.yml");
        configurations.punishments = config("punishments.yml");

        db_path = getDataFolder().toPath().resolve("expandedbans.sqlite3");
    }

    YamlConfiguration config(String path) {
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

        database = new Database();
        commands = section.getKeys(false).stream().map(this::getCommand).toList();

        getCommand("expandedbans").setExecutor(new XExpandedBans());
        getCommand("xban").setExecutor(new XBan());

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        database.close();
    }

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            ResultSet ban;
            if ((ban = database.getBan("P" + event.getUniqueId())) != null)
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.ban(
                        Bukkit.getOfflinePlayer(event.getUniqueId()),
                        ban.getString(2),
                        ban.getString(3)
                ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
