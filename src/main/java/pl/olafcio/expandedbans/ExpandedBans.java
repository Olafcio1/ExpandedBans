package pl.olafcio.expandedbans;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.commands.impl.XExpandedBans;
import pl.olafcio.expandedbans.commands.impl.alts.*;
import pl.olafcio.expandedbans.commands.impl.ban.*;
import pl.olafcio.expandedbans.commands.impl.freeze.*;
import pl.olafcio.expandedbans.commands.impl.kick.*;
import pl.olafcio.expandedbans.commands.impl.lockchat.*;
import pl.olafcio.expandedbans.commands.impl.lockdown.*;
import pl.olafcio.expandedbans.commands.impl.mute.*;
import pl.olafcio.expandedbans.commands.impl.warn.*;
import pl.olafcio.expandedbans.database.Database;
import pl.olafcio.expandedbans.main.ChatLock;
import pl.olafcio.expandedbans.main.PlayerMap;
import pl.olafcio.expandedbans.main.dataclasses.Configurations;
import pl.olafcio.expandedbans.main.dataclasses.Plugin;
import pl.olafcio.expandedbans.main.listeners.*;
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
    public static PlayerMap Players;
    public static @Nullable ChatLock ChatLock;

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
        Configurations.Notifications = config("notifications.yml");
        Configurations.Settings = config("settings.yml");
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
        Players = new PlayerMap();
        ChatLock = null;

        Plugin.Commands = section.getKeys(false).stream().map(this::getCommand).toList();

        getCommand("expandedbans").setExecutor(new XExpandedBans());

        getCommand("xban").setExecutor(new XBan());
        getCommand("xbanip").setExecutor(new XBanIP());
        getCommand("xbanclear").setExecutor(new XBanClear());
        getCommand("xunban").setExecutor(new XUnban());
        getCommand("xunbanip").setExecutor(new XUnbanIP());

        getCommand("xmute").setExecutor(new XMute());
        getCommand("xmuteip").setExecutor(new XMuteIP());
        getCommand("xmuteclear").setExecutor(new XMuteClear());
        getCommand("xunmute").setExecutor(new XUnmute());
        getCommand("xunmuteip").setExecutor(new XUnmuteIP());

        getCommand("xwarn").setExecutor(new XWarn());
        getCommand("xwarnip").setExecutor(new XWarnIP());
        getCommand("xwarnclear").setExecutor(new XWarnClear());
        getCommand("xunwarn").setExecutor(new XUnwarn());
        getCommand("xunwarnip").setExecutor(new XUnwarnIP());

        getCommand("xkick").setExecutor(new XKick());
        getCommand("xkickip").setExecutor(new XKickIP());
        getCommand("xkickall").setExecutor(new XKickAll());

        getCommand("xfreeze").setExecutor(new XFreeze());
        getCommand("xunfreeze").setExecutor(new XUnFreeze());

        getCommand("xlockchat").setExecutor(new XLockChat());
        getCommand("xunlockchat").setExecutor(new XUnLockChat());

        getCommand("xlockdown").setExecutor(new XLockdown());
        getCommand("xunlockdown").setExecutor(new XUnLockdown());

        getCommand("xalts").setExecutor(new XAlts());

        ConnectListener forEach1;
        JoinListener forEach2;

        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new FreezeListener(), this);
        getServer().getPluginManager().registerEvents(new DisconnectListener(), this);
        getServer().getPluginManager().registerEvents(forEach1 = new ConnectListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(forEach2 = new JoinListener(), this);

        var players = getServer().getOnlinePlayers();
        for (var player : players) {
            forEach1.onAsyncPreLogin(new AsyncPlayerPreLoginEvent(player.getName(), player.getAddress().getAddress(), player.getUniqueId()));
            forEach2.onPlayerJoin(new PlayerJoinEvent(player, ""));
        }
    }

    @Override
    public void onDisable() {
        Database.close();
    }
}
