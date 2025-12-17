package pl.olafcio.expandedbans.messages;

import org.bukkit.OfflinePlayer;

@FunctionalInterface
public interface MessageProducer {
    String produce(OfflinePlayer player, String reason, String by);
}
