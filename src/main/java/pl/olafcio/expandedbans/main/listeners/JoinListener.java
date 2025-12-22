package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;

@ApiStatus.Internal
public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ExpandedBans.Players.get(event.getPlayer().getUniqueId()).updateFrozen();
    }
}
