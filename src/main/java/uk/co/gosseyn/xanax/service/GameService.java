package uk.co.gosseyn.xanax.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Active;
import uk.co.gosseyn.xanax.domain.Change;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.Man;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Player;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.repository.GameRepository;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static uk.co.gosseyn.xanax.domain.BlockMap.TREE;

@Service
@Slf4j
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    private MapService mapService;

    @Inject
    private BitcoinService bitcoinService;

    @Inject NameService nameService;

    @Inject
    private TaskService taskService;

    public Game getGame(String gameId) {
        return gameRepository.getGame(gameId);
    }

    public Game newGame() {
        BlockMap map =  mapService.newMap(100, 100, 8, 15);

        mapService.placeBlock(map, new Vector2d(8, 49), TREE);

        mapService.placeBlock(map, new Vector2d(9, 48), TREE);
        mapService.placeBlock(map, new Vector2d(11, 50), TREE);
        mapService.placeBlock(map, new Vector2d(19, 51), TREE);
        mapService.placeBlock(map, new Vector2d(6, 55), TREE);
        mapService.placeBlock(map, new Vector2d(14, 57), TREE);
        mapService.placeBlock(map, new Vector2d(4, 51), TREE);

        Game game = Game.builder().map(map).gameId(bitcoinService.newAddress()).build();

        gameRepository.saveGame(game);
        return game;
    }

    public void addPlayer(String gameId, Player player) {
        Game game = getGame(gameId);

        for(int i = 0; i < 10; i++) {
            Man man = new Man(nameService.newName());
            game.getActiveItems().add(man);
            mapService.placeItem(game.getMap(), new Vector2d(i, i), man);
            player.getSocialGroups().iterator().next().getMembers().add(man);
        }

        player.setGame(game);
        game.getSocialGroups().addAll(player.getSocialGroups());
        game.getPlayers().add(player);
    }

    public void saveGame(Game game) {
        gameRepository.saveGame(game);
    }

    public void update(Game game) {

        game.getActiveItems().forEach(a -> a.update(game));

        //TODO conflict resolution for when two items move to same place.
        // bear in mind ability to undo so all changes need to go via here
     //   synchronized (game.lock) {
            for (Change change : game.getChanges()) {
                change.perform(game);
            }
            game.setFrame(game.getFrame().add(BigInteger.ONE));
     //   }
        game.getChanges().clear();
    }

    @Async
    public void gameLoop() throws InterruptedException {
        while(true) {
            //TODO a game per thread
            synchronized (gameRepository.lock) {
                for (Game game : gameRepository.findAllGames()) {
                    try {
                        taskService.assignTasks(game);
                        update(game);
                        //log.trace("Updated game {}", game);
                    } catch (Exception e) {
                        log.error("Update game failed: {}", game.getGameId(), e);
                    }
                }
            }
            Thread.sleep(500);
        }
    }
}
