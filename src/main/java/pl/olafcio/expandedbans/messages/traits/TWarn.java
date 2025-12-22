package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.MSGTrait;

public interface TWarn extends MSGTrait {
    default String warn(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Notifications.getString("warn.default-reason");

        return $format(player,
                ExpandedBans.Configurations.Notifications.getString("warn.message")
                    .replace("%player%", player.getName())
                    .replace("%admin%", by)
                    .replace("%reason%", reason)
        );
    }
}
