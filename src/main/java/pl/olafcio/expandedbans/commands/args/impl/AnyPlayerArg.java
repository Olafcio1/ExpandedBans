package pl.olafcio.expandedbans.commands.args.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.commands.args.Argument;

import java.util.List;

public class AnyPlayerArg extends Argument<OfflinePlayer> {
    public AnyPlayerArg(Type type) {
        super(type);
    }

    @Override
    public OfflinePlayer parse(String input) {
        return Bukkit.getOfflinePlayer(input);
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args, String arg) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(v -> v.startsWith(arg))
        .toList();
    }
}
