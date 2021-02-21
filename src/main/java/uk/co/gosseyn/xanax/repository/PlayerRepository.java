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
    private Map<String, Player> players = new HashMap<>();
    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }
    public String savePlayer(Player player) {
        String playerId = player.getPlayerId();
        players.put(playerId, player);
        return playerId;
    }
}
