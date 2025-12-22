package pl.olafcio.expandedbans.main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;
import pl.olafcio.expandedbans.messages.MessageProducer;

import java.sql.ResultSet;
import java.sql.SQLException;

@ApiStatus.Internal
public class MuteListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
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

    @EventHandler(ignoreCancelled = true)
    public void onServerCommand(ServerCommandEvent event) {
        var sender = event.getSender();
        if (
                sender instanceof Player player &&
                ExpandedBans.Configurations.Settings.getStringList("chat-commands")
                                                    .contains(event.getCommand().substring(
                                                            0,
                                                            event.getCommand().indexOf(" ")
                                                    ))
        ) {
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
                throw new XBDatabaseException("Failed to check player's mute state on server command", e);
            }
        }
    }

    private static void muted(Cancellable event, Player player, ResultSet mute, MessageProducer producer) throws SQLException {
        event.setCancelled(true);
        player.sendMessage(producer.produce(
                player,
                mute.getString(2),
                mute.getString(3)
        ));

        mute.close();
    }
}
