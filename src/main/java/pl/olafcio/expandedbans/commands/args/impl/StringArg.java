package pl.olafcio.expandedbans.commands.args.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.commands.args.Argument;

import java.util.List;

public class StringArg extends Argument<String> {
    public StringArg(Type type) {
        super(type);
    }

    @Override
    public String parse(String input) {
        return input;
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args, String arg) {
        return List.of();
    }
}
