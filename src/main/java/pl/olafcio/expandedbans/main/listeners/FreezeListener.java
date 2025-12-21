package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.olafcio.expandedbans.ExpandedBans;

public class FreezeListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();

        if (ExpandedBans.Players.get(uuid).isFrozen())
            event.setCancelled(true);
    }
}
