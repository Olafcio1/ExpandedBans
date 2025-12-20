package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.MSGTrait;

public interface TLockdown extends MSGTrait {
    default String lockdown(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("lockdown.default-reason");

        return format(player,
                ExpandedBans.Configurations.Punishments.getString("lockdown.message")
                    .replace("%player%", player.getName())
                    .replace("%admin%", by)
                    .replace("%reason%", reason)
        );
    }
}
