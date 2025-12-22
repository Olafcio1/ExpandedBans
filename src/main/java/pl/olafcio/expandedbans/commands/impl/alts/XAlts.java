package pl.olafcio.expandedbans.commands.impl.alts;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.AnyPlayerArg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class XAlts extends XTargetCommand {
    public XAlts() {
        super.name("xalts")
             .perm("expandedbans.alts")
             .then("player", new AnyPlayerArg(Argument.Type.REQUIRED));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var player = (Player) args.getFirst();
        var persona = ExpandedBans.Database.Player2Persona(player.getUniqueId());

        if (persona == null)
            throw new CommandMessageException($translate("unknown-player"));

        var nicks = new ArrayList<String>();
        try (var res = ExpandedBans.Database.Persona2Players(persona)) {
            while (res.next()) {
                var uuid = UUID.fromString(res.getString(1));
                var plr = Bukkit.getOfflinePlayer(uuid);

                nicks.add(plr.getName());
            }
        }

        var ips = new ArrayList<String>();
        try (var res = ExpandedBans.Database.Persona2IPs(persona)) {
            while (res.next()) {
                var ip = res.getString(1);
                ips.add(ip);
            }
        }

        $send(sender, $translate("results.usernames") +
                           "§6" + String.join("§7, §6", nicks));
        $send(sender, $translate("results.ip-addresses") +
                           "§6" + String.join("§7, §6", ips));
    }
}
