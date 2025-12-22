package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.MSGTrait;

public interface TMuteIP extends MSGTrait {
    default String muteIPNotify(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Notifications.getString("mute-ip.default-reason");

        return $format(player,
                ExpandedBans.Configurations.Notifications.getString("mute-ip.notify")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }

    default String muteIP(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Notifications.getString("mute-ip.default-reason");

        return $format(player,
                ExpandedBans.Configurations.Notifications.getString("mute-ip.on-message")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }
}
