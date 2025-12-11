package pl.olafcio.expandedbans;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.olafcio.expandedbans.commands.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class ExpandedBans extends JavaPlugin {
    public static ExpandedBans INSTANCE;

    public YamlConfiguration messages;

    public Path db_path;
    public Database database;
    public List<PluginCommand> commands;

    @Override
    public void onLoad() {
        var config = getPluginYML();
        var section = Objects.requireNonNull(config.getConfigurationSection("commands"));

        INSTANCE = this;

        messages = YamlConfiguration.loadConfiguration(getTextResource("messages.yml"));

        db_path = getDataFolder().toPath().resolve("expandedbans.sqlite3");
        database = new Database();
        commands = section.getKeys(false).stream().map(this::getCommand).toList();
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
        getCommand("ban").setExecutor(new XBan());
    }

    @Override
    public void onDisable() {
        database.close();
    }
}
