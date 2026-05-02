package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;

@ApiStatus.Internal
public class FreezeListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();

        if (ExpandedBans.Players.get(uuid).isFrozen())
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        var player = ((PlayerInventory) event.getInventory()).getHolder();
        var uuid = player.getUniqueId();

        if (ExpandedBans.Players.get(uuid).isFrozen())
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();

        if (ExpandedBans.Players.get(uuid).isFrozen())
            event.setCancelled(true);
    }
}
