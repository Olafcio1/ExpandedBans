package pl.olafcio.expandedbans.commands.impl.relate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.expandedbans.XBDatabaseException;
import pl.olafcio.expandedbans.commands.CommandMessageException;
import pl.olafcio.expandedbans.commands.XTargetCommand;
import pl.olafcio.expandedbans.commands.args.Argument;
import pl.olafcio.expandedbans.commands.args.impl.IPTargetArg;
import pl.olafcio.expandedbans.commands.args.impl.StringArg;
import pl.olafcio.expandedbans.main.PlayerMap;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class XRelate extends XTargetCommand {
    public XRelate() {
        super.name("xrelate")
             .perm("expandedbans.relate")
             .then("to_relate", new IPTargetArg(Argument.Type.REQUIRED))
             .then("relate_with", new IPTargetArg(Argument.Type.REQUIRED));
    }

    @Override
    protected void execute(CommandSender sender, Command command, String label, List<Object> args) throws CommandMessageException, SQLException {
        var toRelate = (IPTargetArg.IPTarget) args.get(0);
        var relateWith = (IPTargetArg.IPTarget) args.get(1);

        String persona;
        try {
            persona = relateWith.persona();

            for (var ip : toRelate.ips())
                ExpandedBans.Database.registerPersonaIP(ip, persona);

            for (var plr : toRelate.players())
                ExpandedBans.Database.registerPlayerPersona(plr.getUniqueId(), persona);
        } catch (SQLException e) {
            throw new XBDatabaseException("Failed to register player's IP address [/relate]", e);
        }

        $send(sender, $translate("success").formatted(
                toRelate.getName(),
                relateWith.getName()
        ));
    }
}
