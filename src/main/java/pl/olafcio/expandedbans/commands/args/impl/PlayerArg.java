package pl.olafcio.expandedbans.commands.args.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.commands.args.Argument;

import java.util.List;

public class PlayerArg extends Argument<Player> {
    public PlayerArg(Type type) {
        super(type);
    }

    @Override
    public Player parse(String input) {
        return Bukkit.getPlayer(input);
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
