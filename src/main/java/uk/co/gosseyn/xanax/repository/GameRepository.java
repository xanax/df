package uk.co.gosseyn.xanax.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Game;

import java.util.UUID;

@Component
public class GameRepository {
    private Game game;
    public Game getGame(String gameId) {
        return game;
    }
    public void saveGame(Game game) {
        this.game = game;
    }
}
