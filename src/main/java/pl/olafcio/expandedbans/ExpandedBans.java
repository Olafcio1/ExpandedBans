package pl.olafcio.expandedbans;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.olafcio.expandedbans.commands.*;
import pl.olafcio.expandedbans.database.Database;
import pl.olafcio.expandedbans.messages.Messages;

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
        var config = getPluginYML();
        var section = Objects.requireNonNull(config.getConfigurationSection("commands"));

        INSTANCE = this;

        configurations = new Configurations();
        messages = new Messages();

        configurations.messages = YamlConfiguration.loadConfiguration(getTextResource("messages.yml"));
        configurations.punishments = YamlConfiguration.loadConfiguration(getTextResource("punishments.yml"));

        db_path = getDataFolder().toPath().resolve("expandedbans.sqlite3");
        database = new Database();
        System.out.println(section.getKeys(false));
        commands = section.getKeys(false).stream().map(this::getCommand).toList();
        System.out.println(commands);
    }

    // If changing the plugin's API base (Paper/Forge, etc.), change this!
    private YamlConfiguration getPluginYML() {
        var reader = Objects.requireNonNull(getTextResource("plugin.yml"));
        var config = YamlConfiguration.loadConfiguration(reader);

        return config;
    }

    @Override
    public void onEnable() {
        getCommand("expandedbans").setExecutor(new XExpandedBans());
        getCommand("xban").setExecutor(new XBan());
    }

    @Override
    public void onDisable() {
        database.close();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) throws SQLException {
        ResultSet ban;
        if ((ban = database.getBan("P" + event.getPlayer().getUniqueId())) != null)
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, messages.ban(
                    event.getPlayer(),
                    ban.getString(1),
                    ban.getString(2)
            ));
    }
}
