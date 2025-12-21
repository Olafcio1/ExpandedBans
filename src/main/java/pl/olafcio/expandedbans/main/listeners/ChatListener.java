package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatListener implements Listener {
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();

        try {
            ResultSet mute;
            if ((mute = ExpandedBans.Database.getMute("U" + uuid)) != null) {
                event.setCancelled(true);
                player.sendMessage(ExpandedBans.Messages.mute(
                        player,
                        mute.getString(2),
                        mute.getString(3)
                ));

                mute.close();
            } else if ((mute = ExpandedBans.Database.getMute("I" + player.getAddress().getHostString())) != null) {
                event.setCancelled(true);
                player.sendMessage(ExpandedBans.Messages.mute(
                        player,
                        mute.getString(2),
                        mute.getString(3)
                ));

                mute.close();
            }
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check player's mute state on message send", e);
        }
    }
}
