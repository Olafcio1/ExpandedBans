package pl.olafcio.expandedbans;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

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

        var commands = new HashMap<String, Supplier<CommandExecutor>>() {{
            put("expandedbans", XExpandedBans::new);

            put("xban", XBan::new);
            put("xbanip", XBanIP::new);
            put("xbanclear", XBanClear::new);
            put("xunban", XUnban::new);
            put("xunbanip", XUnbanIP::new);

            put("xmute", XMute::new);
            put("xmuteip", XMuteIP::new);
            put("xmuteclear", XMuteClear::new);
            put("xunmute", XUnmute::new);
            put("xunmuteip", XUnmuteIP::new);

            put("xwarn", XWarn::new);
            put("xwarnip", XWarnIP::new);
            put("xwarnclear", XWarnClear::new);
            put("xunwarn", XUnwarn::new);
            put("xunwarnip", XUnwarnIP::new);

            put("xkick", XKick::new);
            put("xkickip", XKickIP::new);
            put("xkickall", XKickAll::new);

            put("xfreeze", XFreeze::new);
            put("xunfreeze", XUnFreeze::new);

            put("xlockchat", XLockChat::new);
            put("xunlockchat", XUnLockChat::new);

            put("xlockdown", XLockdown::new);
            put("xunlockdown", XUnLockdown::new);

            put("xalts", XAlts::new);
        }};

        var enabledCommands = Configurations.Settings.getStringList("enabled-commands");
        var nonPaper = !Bukkit.getName().contains("Paper");

        for (var entry : commands.entrySet()) {
            var name = entry.getKey();
            var obj = getCommand(name);

            assert obj != null;

            if (nonPaper || enabledCommands.contains(name) || name.equals("expandedbans")) {
                obj.setExecutor(entry.getValue().get());
            } else {
                EXPaperOnly.unregister(obj);
            }
        }

        ConnectListener forEach1;
        JoinListener forEach2;

        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new FreezeListener(), this);
        getServer().getPluginManager().registerEvents(new DisconnectListener(), this);
        getServer().getPluginManager().registerEvents(forEach1 = new ConnectListener(), this);
        getServer().getPluginManager().registerEvents(new ConnectedListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(forEach2 = new JoinListener(), this);

        var players = getServer().getOnlinePlayers();
        for (var player : players) {
            //noinspection deprecation -- I cannot use the non-deprecated constructor, as I need to support older versions
            forEach1.onAsyncPreLogin(new AsyncPlayerPreLoginEvent(player.getName(), player.getAddress().getAddress(), player.getUniqueId()));
            forEach2.onPlayerJoin(new PlayerJoinEvent(player, ""));
        }
    }

    @Override
    public void onDisable() {
        Database.close();
    }
}
