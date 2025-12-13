package pl.olafcio.expandedbans.commands.args.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.commands.args.Argument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EnumArg<T> extends Argument<T> {
    private final Map<String, T> mapping;
    private final List<String> keys;

    public static final class Builder<T> {
        private Type type;
        private T[] values;

        public Builder<T> type(Type type) {
            return copy(b -> {
                b.type = type;
                b.values = values;
            });
        }

        public Builder<String> with(String... values) {
            return copy(b -> {
                b.type = type;
                b.values = values;
            });
        }

        @SafeVarargs
        public final <E extends Enum<E>> Builder<E> with(E... values) {
            return copy(b -> {
                b.type = type;
                b.values = values;
            });
        }

        private <NT> Builder<NT> copy(Consumer<Builder<NT>> callback) {
            var b = new Builder<NT>();
            callback.accept(b);
            return b;
        }

        public EnumArg<T> build() {
            return new EnumArg<>(type, values);
        }
    }

    public static class InvalidValueError extends RuntimeException {
        public InvalidValueError(String e) {
            super(e);
        }
    }

    @SafeVarargs
    protected EnumArg(Type type, T... values) {
        super(type);

        mapping = new HashMap<>();
        keys = new ArrayList<>();

        for (var value : values) {
            String name;
            if (value instanceof String str)
                name = str;
            else if (value instanceof Enum<?> member)
                name = member.name();
            else throw new InvalidValueError("value of type '%s' cannot be handled; accepting only strings or enum members".formatted(
                    value.getClass().getSimpleName()
            ));

            if (keys.contains(name))
                throw new InvalidValueError("multiple values of name '%s'".formatted(name));

            keys.add(name);
            mapping.put(name, value);
        }
    }

    @Override
    public T parse(String input) {
        return mapping.get(input);
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command command, String label, String[] args, String arg) {
        return keys
                .stream()
                .filter(x -> !x.isEmpty() && x.startsWith(arg))
        .toList();
    }
}
