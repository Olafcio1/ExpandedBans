package pl.olafcio.expandedbans.commands.args.function;

import java.util.stream.Stream;

@FunctionalInterface
public interface Completer {
    Stream<String> complete();
}
