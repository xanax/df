package uk.co.gosseyn.xanax.repository;

import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class PlayerRepository {
    private Map<UUID, Player> players = new HashMap<>();
    public Player getPlayer(UUID playerId) {
        return players.get(playerId);
    }
    public UUID savePlayer(Player player) {
        UUID playerId = player.getPlayerId();
        players.put(playerId, player);
        return playerId;
    }
}
