package pl.olafcio.expandedbans.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import net.matrixcreations.libraries.MatrixColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import pl.olafcio.expandedbans.messages.traits.TBan;
import pl.olafcio.expandedbans.messages.traits.TMute;

public final class Messages implements MSGTrait, TBan, TMute {
    private final boolean isPAPIloaded;
    public Messages() {
        isPAPIloaded = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public String format(OfflinePlayer player, String data) {
        if (isPAPIloaded)
            data = PlaceholderAPI.setPlaceholders(player, data);

        data = MatrixColorAPI.process(data);
        return data;
    }
}
