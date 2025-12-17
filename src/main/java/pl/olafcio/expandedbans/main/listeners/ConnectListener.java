package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.olafcio.expandedbans.ExpandedBans;
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
        System.out.println(uuid);
        System.out.println(ip);

        try {
            ExpandedBans.Database.registerPlayerIp(uuid, ip);
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to register player's IP address on connect", e);
        }

        try {
            ResultSet ban;

            if ((ban = ExpandedBans.Database.getBan("P" + uuid)) != null)
                banned(event, uuid, ban, ExpandedBans.Messages::ban);
            else if ((ban = ExpandedBans.Database.getBan("I" + ip)) != null)
                banned(event, uuid, ban, ExpandedBans.Messages::banIP);
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check player's ban state on connect", e);
        }
    }

    private void banned(AsyncPlayerPreLoginEvent event, UUID uuid, ResultSet ban, MessageProducer producer) throws SQLException {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, producer.produce(
                Bukkit.getOfflinePlayer(uuid),
                ban.getString(2),
                ban.getString(3)
        ));

        ban.close();
    }
}
