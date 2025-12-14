package pl.olafcio.expandedbans.commands.args.function;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface Tabcompleter {
    List<String> apply(CommandSender sender, Command command, String label, String[] args, String arg);
}
