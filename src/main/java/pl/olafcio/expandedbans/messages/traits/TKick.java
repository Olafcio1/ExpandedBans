package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.MSGTrait;

public interface TKick extends MSGTrait {
    default String kick(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Notifications.getString("kick.default-reason");

        return $format(player,
                ExpandedBans.Configurations.Notifications.getString("kick.message")
                    .replace("%player%", player.getName())
                    .replace("%admin%", by)
                    .replace("%reason%", reason)
        );
    }

    default String kickAll(@NonNull Player player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Notifications.getString("kickall.default-reason");

        return $format(player,
                ExpandedBans.Configurations.Notifications.getString("kickall.message")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }
}
