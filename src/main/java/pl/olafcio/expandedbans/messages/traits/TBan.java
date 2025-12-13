package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.MSGTrait;

public interface TBan extends MSGTrait {
    default String ban(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("ban.default-reason");

        return format(player,
                ExpandedBans.Configurations.Punishments.getString("ban.message")
                    .replace("%player%", player.getName())
                    .replace("%admin%", by)
                    .replace("%reason%", reason)
        );
    }
}
