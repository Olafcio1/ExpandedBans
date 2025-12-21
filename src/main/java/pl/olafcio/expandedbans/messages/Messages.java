package pl.olafcio.expandedbans.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import net.matrixcreations.libraries.MatrixColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.messages.traits.*;

public final class Messages implements MSGTrait, TBan, TBanIP, TMute, TMuteIP, TKick, TLockdown {
    private final boolean isPAPIloaded;
    public Messages() {
        isPAPIloaded = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public String format(OfflinePlayer player, String data) {
        if (isPAPIloaded)
            data = PlaceholderAPI.setPlaceholders(player, data);

        return format(data);
    }

    public String format(String data) {
        // TODO: More message processors? MiniMessage would be cool to support, but that'd require integration with the
        //       weird-ahh Adventure API - or, doing weird shit with:
        //       > spigot().sendMessage(md_5::BaseComponent)
        //       > sendMessage(kyori::Component)

        return MatrixColorAPI.process(data);
    }

    public void send(CommandSender player, String data) {
        data = ExpandedBans.Configurations.Messages.getString("prefix") +
               data;

        player.sendMessage(format(data));
    }
}
