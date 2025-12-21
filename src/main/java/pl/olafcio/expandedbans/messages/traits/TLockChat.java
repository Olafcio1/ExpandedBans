package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.MSGTrait;

public interface TLockChat extends MSGTrait {
    default String lockchat(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("lockchat.default-reason");

        return format(player,
                ExpandedBans.Configurations.Punishments.getString("lockchat.message")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }

    default String lockchatNotify(@Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("lockchat.default-reason");

        return format(
                ExpandedBans.Configurations.Punishments.getString("lockchat.notify")
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }
}
