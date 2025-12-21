package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.olafcio.expandedbans.ExpandedBans;

public class DisconnectListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ExpandedBans.Personas.remove(event.getPlayer().getUniqueId());
    }
}
