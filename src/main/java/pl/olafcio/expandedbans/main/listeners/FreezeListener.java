package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
        var holder = event.getInventory().getHolder();
        if (holder instanceof Player player) {
            var uuid = player.getUniqueId();

            if (ExpandedBans.Players.get(uuid).isFrozen())
                event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();

        if (ExpandedBans.Players.get(uuid).isFrozen())
            event.setCancelled(true);
    }
}
