package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.main.PlayerMap;
import pl.olafcio.expandedbans.XBDatabaseException;
import pl.olafcio.expandedbans.messages.MessageProducer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ConnectListener implements Listener {
    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        var uuid = event.getUniqueId();
        var ip = event.getAddress().getHostAddress();

        String persona;
        try {
            persona = getPersona(ip, uuid);

            ExpandedBans.Database.registerPersonaIP(ip, persona);
            ExpandedBans.Database.registerPlayerPersona(uuid, persona);

            ExpandedBans.Players.put(new PlayerMap.Entry(persona, uuid));
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to register player's IP address on connect", e);
        }

        try {
            ResultSet lockdown;
            if ((lockdown = ExpandedBans.Database.getLockdown()) != null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ExpandedBans.Messages.lockdown(
                        Bukkit.getOfflinePlayer(uuid),
                        lockdown.getString(2),
                        lockdown.getString(1)
                ));

                lockdown.close();
                return;
            }
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check server's lockdown state on connect", e);
        }

        try {
            ResultSet ban;

            if ((ban = ExpandedBans.Database.getBan("U" + uuid)) != null)
                banned(event, uuid, ban, ExpandedBans.Messages::ban);
            else if ((ban = ExpandedBans.Database.getBan("P" + persona)) != null)
                banned(event, uuid, ban, ExpandedBans.Messages::banIP);
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check player's ban state on connect", e);
        }
    }

    private static String getPersona(String ip, UUID uuid) throws SQLException {
        String persona;

        if ((persona = ExpandedBans.Database.IP2Persona(ip)) != null);
        else if ((persona = ExpandedBans.Database.Player2Persona(uuid)) != null);
        else persona = UUID.randomUUID().toString();

        return persona;
    }

    private static void banned(AsyncPlayerPreLoginEvent event, UUID uuid, ResultSet ban, MessageProducer producer) throws SQLException {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, producer.produce(
                Bukkit.getOfflinePlayer(uuid),
                ban.getString(2),
                ban.getString(3)
        ));

        ban.close();
    }
}
