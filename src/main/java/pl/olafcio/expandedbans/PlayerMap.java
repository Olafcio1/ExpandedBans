package pl.olafcio.expandedbans;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public final class PlayerMap extends HashMap<UUID, PlayerMap.Player> {
    public final static class Player {
        private final String persona;
        private final UUID uuid;
        private boolean frozen;

        public Player(String persona, UUID uuid, boolean frozen) {
            this.persona = persona;
            this.uuid = uuid;
            this.frozen = frozen;
        }

        public Player(String persona, UUID uuid) throws SQLException {
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

        public void setFrozen(boolean frozen) {
            this.frozen = frozen;
        }

        @Override
        public String toString() {
            return persona;
        }
    }

    public void put(Player player) {
        this.put(player.uuid, player);
    }
}
