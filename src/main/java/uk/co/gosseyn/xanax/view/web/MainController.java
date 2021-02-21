package uk.co.gosseyn.xanax.view.web;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    @RequestMapping("/frameData")
    //TODO work out synchronization
    public synchronized FrameData gameData(@RequestParam String playerId, @RequestParam int left,
                              @RequestParam int top,
                              @RequestParam int z,
                              @RequestParam int width,
                              @RequestParam int height) {
       // jdbcTemplate.execute("create table abc (id int not null, primary key (id));");
        log.info("Frame start: {}", System.currentTimeMillis());
        Player player = playerService.getPlayer(playerId);
        Game game = gameService.getGame(player.getGame().getGameId());
        taskService.assignTasks(game);
        gameService.update(game);
        FrameData frameData = gameFacade.getFrameData(game, left, top, z, width, height);
        log.info("Frame end: {}", System.currentTimeMillis());
        return frameData;
    }

    @RequestMapping("/newGame")
    public Game newGame() {
        Game game = gameService.newGame();
        return game;
    }

    @RequestMapping("/newPlayer")
    public String newPlayer(@RequestParam String gameId) {
        Player player = playerService.newPlayer();
        gameService.addPlayer(gameId, player);
        return player.getPlayerId();
    }

    @RequestMapping("/joinGame")
    public synchronized void joinGame(@RequestParam String gameId, @RequestParam String playerId) {
        gameService.addPlayer(gameId, playerService.getPlayer(playerId));
    }

    @RequestMapping("/zone")
    public void zone(@RequestParam String playerId, @RequestParam int startx,
                         @RequestParam int starty,
                         @RequestParam int startz,
                         @RequestParam int endx,
                         @RequestParam int endy,
                         @RequestParam int endz) {

        Player player = playerService.getPlayer(playerId);
        BlockMap map = gameService.getGame(player.getGame().getGameId()).getMap();


        //zone.setLocation(location);
        //zone.setExtent(extent);
        //player.getSocialGroups().iterator().next().getZones().add(zone);
        Point min = new Point(startx, starty, startz);
        Point max = new Point(endx, endy, endz);
        ForestTask forestTask = new ForestTask(new ForestZone(map,new Bounds(min, max)));

        player.getSocialGroups().iterator().next().getTasks().add(forestTask);

    }
}
