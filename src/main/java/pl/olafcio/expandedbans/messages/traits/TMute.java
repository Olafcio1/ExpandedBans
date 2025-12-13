package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.MSGTrait;

public interface TMute extends MSGTrait {
    default String muteNotify(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("mute.default-reason");

        return format(player,
                ExpandedBans.Configurations.Punishments.getString("mute.notify")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }

    default String mute(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("mute.default-reason");

        return format(player,
                ExpandedBans.Configurations.Punishments.getString("mute.on-message")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }
}
