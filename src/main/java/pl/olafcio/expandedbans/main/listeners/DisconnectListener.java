package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;

@ApiStatus.Internal
public class DisconnectListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ExpandedBans.Players.remove(event.getPlayer().getUniqueId());
    }
}
