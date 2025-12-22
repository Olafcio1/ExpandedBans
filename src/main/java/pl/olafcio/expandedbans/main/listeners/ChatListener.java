package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;

@ApiStatus.Internal
public class ChatListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
        if (ExpandedBans.ChatLock != null && !player.hasPermission("expandedbans.lockchat-bypass")) {
            event.setCancelled(true);
            player.sendMessage(ExpandedBans.Messages.lockchat(
                    player,
                    ExpandedBans.ChatLock.reason(),
                    ExpandedBans.ChatLock.by()
            ));

        }
    }
}
