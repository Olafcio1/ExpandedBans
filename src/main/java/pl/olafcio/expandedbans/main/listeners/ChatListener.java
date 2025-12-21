package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;
import pl.olafcio.expandedbans.messages.MessageProducer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatListener implements Listener {
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
        if (ExpandedBans.ChatLock != null && !player.hasPermission("expandedbans.lockchat-bypass")) {
            event.setCancelled(true);
            player.sendMessage(ExpandedBans.Messages.lockchat(
                    player,
                    ExpandedBans.ChatLock.reason(),
                    ExpandedBans.ChatLock.by()
            ));

            return;
        }

        var uuid = player.getUniqueId();

        try {
            ResultSet mute;
            if ((mute = ExpandedBans.Database.getMute("U" + uuid)) != null) {
                muted(event, player, mute, ExpandedBans.Messages::mute);
            } else if ((mute = ExpandedBans.Database.getMute(
                    "P" + ExpandedBans.Players.get(player.getUniqueId())
            )) != null) {
                muted(event, player, mute, ExpandedBans.Messages::muteIP);
            }
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to check player's mute state on message send", e);
        }
    }

    private static void muted(AsyncPlayerChatEvent event, Player player, ResultSet mute, MessageProducer producer) throws SQLException {
        event.setCancelled(true);
        player.sendMessage(producer.produce(
                player,
                mute.getString(2),
                mute.getString(3)
        ));

        mute.close();
    }
}
