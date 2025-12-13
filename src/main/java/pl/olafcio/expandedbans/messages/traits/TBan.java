package pl.olafcio.expandedbans.messages.traits;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.olafcio.expandedbans.ExpandedBans;
import net.matrixcreations.libraries.MatrixColorAPI;

public interface TBan {
    default String ban(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("ban.default-reason");

        return MatrixColorAPI.process(
                ExpandedBans.Configurations.Punishments.getString("ban.message")
                    .replace("%player%", player.getName())
                    .replace("%admin%", by)
                    .replace("%reason%", reason)
        );
    }

    default String muteNotify(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("mute.default-reason");

        return MatrixColorAPI.process(
                ExpandedBans.Configurations.Punishments.getString("mute.notify")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }

    default String mute(@NonNull OfflinePlayer player, @Nullable String reason, @NonNull String by) {
        if (reason == null)
            reason = ExpandedBans.Configurations.Punishments.getString("mute.default-reason");

        return MatrixColorAPI.process(
                ExpandedBans.Configurations.Punishments.getString("mute.on-message")
                        .replace("%player%", player.getName())
                        .replace("%admin%", by)
                        .replace("%reason%", reason)
        );
    }
}
