package pl.olafcio.expandedbans;

import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;

public final class EXPaperOnly {
    private EXPaperOnly() {}

    static void unregister(Command cmd) {
        cmd.unregister(MinecraftServer.getServer().server.getCommandMap());
    }
}
