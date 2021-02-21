package uk.co.gosseyn.xanax.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;
import static uk.co.gosseyn.xanax.domain.BlockMap.TREE;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    private MapService mapService;

    @Inject
    private BitcoinService bitcoinService;

    @Inject NameService nameService;

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
        Man man = new Man(nameService.newName());
        Man man2 = new Man(nameService.newName());
        Game game = getGame(gameId);
        game.getActiveItems().add(man);
        game.getActiveItems().add(man2);

        mapService.placeItem(game.getMap(), new Vector2d(0, 44), man);
        mapService.placeItem(game.getMap(), new Vector2d(3, 45), man2);

        player.setGame(game);
        player.getSocialGroups().iterator().next().getMembers().addAll(asList(man, man2));
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
        for(Change change : game.getChanges()) {
            change.perform(game);
        }
        game.getChanges().clear();
    }

}
