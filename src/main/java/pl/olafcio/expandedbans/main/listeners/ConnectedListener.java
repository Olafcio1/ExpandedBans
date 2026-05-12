package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ConnectedListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onConnect(AsyncPlayerPreLoginEvent event) {
        String persona;

        try {
            persona = ExpandedBans.Database.Player2Persona(event.getUniqueId());
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check player's persona on connected", e);
        }

        try {
            var nicks = new ArrayList<String>();
            try (var res = ExpandedBans.Database.Persona2Players(persona)) {
                while (res.next()) {
                    var f_uuid = UUID.fromString(res.getString(1));
                    var f_plr = Bukkit.getOfflinePlayer(f_uuid);

                    if (!f_plr.getUniqueId().equals(event.getUniqueId()))
                        nicks.add(f_plr.getName());
                }
            }

            String message;

            if (nicks.isEmpty()) {
                message = ExpandedBans.Messages.$translate("alts-autonotify.empty");
            } else {
                message = ExpandedBans.Messages.$translate("alts-autonotify.has")
                                               .formatted(event.getName()) + "§6" + String.join("§7, §6", nicks);
            }

            var online = ExpandedBans.getInstance().getServer().getOnlinePlayers();

            for (var l_plr : online)
                if (l_plr.hasPermission("expandedbans.alts-autonotify"))
                    ExpandedBans.Messages.$send(l_plr, message);
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check player's alts state on connect", e);
        }
    }
}
