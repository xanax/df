package uk.co.gosseyn.xanax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.Man;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Player;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.repository.GameRepository;

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

    public Game newGame() {
        BlockMap map =  mapService.newMap(100, 100, 8, 15);

        Game game = Game.builder().map(map).build();
        Man man = new Man();
        mapService.placeItem(map, new Vector2d(0, 44), man);
        gameRepository.saveGame(game);
        return game;
    }

    public void update() {

    }

}
