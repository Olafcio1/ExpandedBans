package pl.olafcio.expandedbans.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import pl.olafcio.expandedbans.ExpandedBans;
import pl.olafcio.protocolextension.server.ProtocolExtension;
import pl.olafcio.protocolextension.server.VariableAPI;

public final class PlayerMap extends HashMap<UUID, PlayerMap.Entry> {
    public final static class Entry {
        private final String persona;
        private final UUID uuid;
        private boolean frozen;

        public Entry(String persona, UUID uuid, boolean frozen) {
            this.persona = persona;
            this.uuid = uuid;
            this.setFrozen(frozen);
        }

        public Entry(String persona, UUID uuid) throws SQLException {
            this(persona, uuid, ExpandedBans.Database.isFrozen("P" + persona) ||
                                       ExpandedBans.Database.isFrozen("U" + uuid));
        }

        public String getPersona() {
            return persona;
        }

        public UUID getUuid() {
            return uuid;
        }

        public boolean isFrozen() {
            return frozen;
        }

        private Player player = null;
        public Player getPlayer() {
            if (player == null)
                player = Bukkit.getPlayer(uuid);

            return player;
        }

        public void setFrozen(boolean frozen) {
            this.frozen = frozen;
            updateFrozen();
        }

        @ApiStatus.Internal
        public void updateFrozen() {
            this.getPlayer();
            if (this.player != null) {
                this.player.setAllowFlight(frozen || (
                        this.player.getGameMode() == GameMode.CREATIVE ||
                        this.player.getGameMode() == GameMode.SPECTATOR
                ));

                this.player.setFlying(
                        frozen ||
                        this.player.getGameMode() == GameMode.SPECTATOR
                );
            }

            if (Bukkit.getPluginManager().isPluginEnabled("protocolextension") && VariableAPI.isActivated(player))
                ProtocolExtension.getAPI().playerManager().moveToggle(
                        player,
                        !frozen
                );
        }

        @Override
        public String toString() {
            return persona;
        }
    }

    public void put(Entry player) {
        this.put(player.uuid, player);
    }
}
