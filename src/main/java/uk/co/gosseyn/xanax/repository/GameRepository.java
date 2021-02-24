package uk.co.gosseyn.xanax.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Game;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GameRepository {
    public final  Object lock = new Object();
    @Getter
    private Map<String, Game> games = new HashMap<>();
    public Game getGame(String gameId) {
        return games.get(gameId);
    }
    public void saveGame(Game game) {
        synchronized(lock) {
            games.put(game.getGameId(), game);
        }
    }
    public Collection<Game> findAllGames() {
        return games.values();
    }
}
