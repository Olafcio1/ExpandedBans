package pl.olafcio.expandedbans.commands.args;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public abstract class Argument<T> {
    public final Type type;
    protected Argument(@NonNull Type type) {
        this.type = type;
    }

    public enum Type {
        REQUIRED,
        OPTIONAL,
        REST
    }

    public abstract T parse(String input) throws PatternError;
    public abstract List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args, String arg);
}
