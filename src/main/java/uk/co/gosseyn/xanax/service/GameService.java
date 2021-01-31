package uk.co.gosseyn.xanax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Change;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.Man;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Player;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.repository.GameRepository;

import java.util.Map;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    private MapService mapService;

    public Game getGame(UUID gameId) {
        return gameRepository.getGame(gameId);
    }

    public Game newGame(BlockMap map) {
        Game game = Game.builder().map(map).build();
        gameRepository.saveGame(game);
        return game;
    }

    public void saveGame(Game game) {
        gameRepository.saveGame(game);
    }

    public void update(Game game) {
        //TODO conflict resolution for when two items move to same place.
        for(Change change : game.getChanges()) {
            change.perform(game);
        }
        game.getChanges().clear();
    }

}
