package pl.olafcio.expandedbans.main.dataclasses;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;

public final class Configurations {
    public YamlConfiguration Messages;
    public YamlConfiguration Notifications;

    @ApiStatus.Experimental
    public YamlConfiguration Settings;
}
