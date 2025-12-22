package pl.olafcio.expandedbans.messages;

import org.bukkit.OfflinePlayer;

public interface MSGTrait {
    String $format(OfflinePlayer player, String input);
    String $format(String input);
}
