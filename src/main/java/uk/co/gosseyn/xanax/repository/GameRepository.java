package uk.co.gosseyn.xanax.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GameRepository {
    private Map<String, Game> games = new HashMap<>();
    public Game getGame(String gameId) {
        return games.get(gameId);
    }
    public void saveGame(Game game) {
        this.games.put(game.getGameId(), game);
    }
}
