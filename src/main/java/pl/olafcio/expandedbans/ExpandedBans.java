package pl.olafcio.expandedbans;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.olafcio.expandedbans.commands.impl.XExpandedBans;
import pl.olafcio.expandedbans.commands.impl.bans.XBan;
import pl.olafcio.expandedbans.commands.impl.bans.XClearBans;
import pl.olafcio.expandedbans.commands.impl.bans.XUnban;
import pl.olafcio.expandedbans.commands.impl.kicks.XKick;
import pl.olafcio.expandedbans.commands.impl.nutes.XMute;
import pl.olafcio.expandedbans.commands.impl.nutes.XMuteIP;
import pl.olafcio.expandedbans.commands.impl.nutes.XUnmute;
import pl.olafcio.expandedbans.commands.impl.nutes.XUnmuteIP;
import pl.olafcio.expandedbans.database.Database;
import pl.olafcio.expandedbans.main.dataclasses.Configurations;
import pl.olafcio.expandedbans.main.dataclasses.Plugin;
import pl.olafcio.expandedbans.main.listeners.ChatListener;
import pl.olafcio.expandedbans.main.listeners.ConnectListener;
import pl.olafcio.expandedbans.messages.Messages;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public final class ExpandedBans extends JavaPlugin {
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

    public void reloadConfigurations() {
        Configurations.Messages = config("messages.yml");
        Configurations.Punishments = config("punishments.yml");
    }

    @Override
    public void onLoad() {
        INSTANCE = this;

        Configurations = new Configurations();
        Messages = new Messages();
        Plugin = new Plugin();

        Plugin.Logger = getLogger();

        reloadConfigurations();

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
        getCommand("xmute").setExecutor(new XMute());
        getCommand("xmuteip").setExecutor(new XMuteIP());
        getCommand("xunmute").setExecutor(new XUnmute());
        getCommand("xunmuteip").setExecutor(new XUnmuteIP());
        getCommand("xkick").setExecutor(new XKick());

        getServer().getPluginManager().registerEvents(new ConnectListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        Database.close();
    }
}
