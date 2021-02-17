package uk.co.gosseyn.xanax.view.web;

import org.newdawn.slick.util.pathfinding.PathFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.gosseyn.xanax.domain.Bounds;
import uk.co.gosseyn.xanax.domain.ForestTask;
import uk.co.gosseyn.xanax.domain.ForestZone;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Man;
import uk.co.gosseyn.xanax.domain.MovingObject;
import uk.co.gosseyn.xanax.domain.Player;
import uk.co.gosseyn.xanax.domain.Point;
import uk.co.gosseyn.xanax.domain.SocialGroup;
import uk.co.gosseyn.xanax.domain.Task;
import uk.co.gosseyn.xanax.domain.TaskAssignable;
import uk.co.gosseyn.xanax.domain.TaskAssignment;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.service.GameService;
import uk.co.gosseyn.xanax.service.MapService;
import uk.co.gosseyn.xanax.service.NameService;
import uk.co.gosseyn.xanax.service.PlayerService;
import uk.co.gosseyn.xanax.service.TaskService;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static uk.co.gosseyn.xanax.domain.BlockMap.TREE;

@RestController
public class MainController {

    private final GameFacade gameFacade;
    @Inject
    public MainController(GameFacade gameFacade) {
        this.gameFacade = gameFacade;
    }
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private MapService mapService;
    @Autowired
    private NameService nameService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PathFinder finder;

    private MovingObject item;

    //TODO this will be passed from client
    private UUID playerId;
    private UUID gameId;


    @RequestMapping("/gameData")
    //TODO work out synchronization
    public synchronized FrameData gameData(@RequestParam int left,
                              @RequestParam int top,
                              @RequestParam int z,
                              @RequestParam int width,
                              @RequestParam int height) {
       // jdbcTemplate.execute("create table abc (id int not null, primary key (id));");
        Game game = gameService.getGame(gameId);
        if(game == null) {
            this.newMap();
        }
        taskService.assignTasks(game);
        gameService.update(game);

//      s  BlockMap map = game.getMap();
//        if(item != null && item.getPath() != null && item.getPathStep() < item.getPath().getLength()) {
//            item.setPathStep(item.getPathStep() + 1); // first one contains current
//            Point step = item.getPath().getStep(item.getPathStep());
//                map.removeItem(new Point(item.getLocation().getX(),
//                        item.getLocation().getY(),
//                        item.getLocation().getZ()),item);
//                Point newLocation = new Point(step.getX(), step.getY(), item.getLocation().getZ());
//                map.addItem(newLocation, item);
//                item.setLocation(newLocation);
//        }
        return gameFacade.getFrameData(game, left, top, z, width, height);
    }

    @RequestMapping("/newMap")
    public void newMap() {
        item = null;
        BlockMap map =  mapService.newMap(100, 100, 8, 15);

        Man man = new Man(nameService.newName());
        Man man2 = new Man(nameService.newName());
        mapService.placeItem(map, new Vector2d(0, 44), man);
        mapService.placeItem(map, new Vector2d(3, 45), man2);

        mapService.placeBlock(map, new Vector2d(8, 49), TREE);

        mapService.placeBlock(map, new Vector2d(9, 48), TREE);
        mapService.placeBlock(map, new Vector2d(11, 50), TREE);
        mapService.placeBlock(map, new Vector2d(19, 51), TREE);
        mapService.placeBlock(map, new Vector2d(6, 55), TREE);
        mapService.placeBlock(map, new Vector2d(14, 57), TREE);
        mapService.placeBlock(map, new Vector2d(4, 51), TREE);

        Game game = gameService.newGame(map);
        game.getActiveItems().add(man);
        game.getActiveItems().add(man2);
        this.gameId = game.getGameId();
        Player player = playerService.newPlayer();
        game.getSocialGroups().addAll(player.getSocialGroups());
        player.setGame(game);
        player.getSocialGroups().iterator().next().getMembers().addAll(asList(man, man2));
        playerService.savePlayer(player);
        playerId = player.getPlayerId();
        game.getPlayers().add(player);
        gameService.saveGame(game);

    }

    @RequestMapping("/zone")
    public void zone(@RequestParam int startx,
                         @RequestParam int starty,
                         @RequestParam int startz,
                         @RequestParam int endx,
                         @RequestParam int endy,
                         @RequestParam int endz) {
        BlockMap map = gameService.getGame(gameId).getMap();
        Player player = playerService.getPlayer(playerId);

        //zone.setLocation(location);
        //zone.setExtent(extent);
        //player.getSocialGroups().iterator().next().getZones().add(zone);
        Point min = new Point(startx, starty, startz);
        Point max = new Point(endx, endy, endz);
        ForestTask forestTask = new ForestTask(new ForestZone(map,new Bounds(min, max)));

        player.getSocialGroups().iterator().next().getTasks().add(forestTask);

    }
}
