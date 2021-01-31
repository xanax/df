package uk.co.gosseyn.xanax.view.web;

import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import lombok.var;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.gosseyn.xanax.domain.Bounds;
import uk.co.gosseyn.xanax.domain.CanJoinSocialGroup;
import uk.co.gosseyn.xanax.domain.ForrestZone;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.HasNeeds;
import uk.co.gosseyn.xanax.domain.Man;
import uk.co.gosseyn.xanax.domain.MineTask;
import uk.co.gosseyn.xanax.domain.Moveable;
import uk.co.gosseyn.xanax.domain.Player;
import uk.co.gosseyn.xanax.domain.SocialGroup;
import uk.co.gosseyn.xanax.domain.Task;
import uk.co.gosseyn.xanax.domain.TaskAssignable;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.domain.Vector3d;
import uk.co.gosseyn.xanax.service.GameService;
import uk.co.gosseyn.xanax.service.MapService;
import uk.co.gosseyn.xanax.service.PlayerService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private PathFinder finder;

    private Moveable item;

    //TODO this will be passed from client
    private UUID playerId;
    private UUID gameId;

    @RequestMapping("/gameData")
    public FrameData gameData(@RequestParam int left,
                              @RequestParam int top,
                              @RequestParam int z,
                              @RequestParam int width,
                              @RequestParam int height) {
        Game game = gameService.getGame(gameId);
        if(game == null) {
            throw new IllegalArgumentException("Game does not exist.");
        }
        for(TaskAssignable taskDoer: game.getTaskDoers()) {
            if(taskDoer.getCurrentTask() != null) {
                taskDoer.getCurrentTask().perform(game);
            }
        }

        for(SocialGroup group : game.getSocialGroups()) {

            Collection<Task> unassigned = group.getTasks().stream()
                    .filter(t -> t.getAssignees().isEmpty()).collect(Collectors.toList());

            Collection<TaskAssignable> potentialAssignees =  group.getMembers().stream()
                    .filter(t -> t instanceof TaskAssignable && ((TaskAssignable) t).getCurrentTask() == null)
                    .map(TaskAssignable.class::cast).collect(Collectors.toList());

            // TODO logic to determine best / who's able
            if(!potentialAssignees.isEmpty() && !unassigned.isEmpty()) {
                TaskAssignable assignee = potentialAssignees.iterator().next();
                Task task = unassigned.iterator().next();
                task.getAssignees().add(assignee);
                assignee.setCurrentTask(task);
            }
            //TODO process group level needs
            for(CanJoinSocialGroup member : group.getMembers()) {
                // TODO process individual needs
            }

        }


        BlockMap map = game.getMap();
        if(item != null && item.getPath() != null && item.getPathStep() < item.getPath().getLength()) {
            item.setPathStep(item.getPathStep() + 1); // first one contains current
            Path.Step step = item.getPath().getStep(item.getPathStep());
                map.removeItem(new Vector3d(item.getLocation().getX(),
                        item.getLocation().getY(),
                        item.getLocation().getZ()),item);
                Vector3d newLocation = new Vector3d(step.getX(), step.getY(), item.getLocation().getZ());
                map.addItem(newLocation, item);
                item.setLocation(newLocation);
        }
        return gameFacade.getFrameData(game, left, top, z, width, height);
    }

    @RequestMapping("/newMap")
    public void newMap() {
        item = null;
        BlockMap map =  mapService.newMap(100, 100, 8, 15);

        Man man = new Man();
        mapService.placeItem(map, new Vector2d(0, 44), man);

        Game game = gameService.newGame(map);
        this.gameId = game.getGameId();
        var player = playerService.newPlayer();
        game.getSocialGroups().addAll(player.getSocialGroups());
        player.setGame(game);
        player.getSocialGroups().iterator().next().getMembers().add(man);
        game.getTaskDoers().add(man);
        playerService.savePlayer(player);
        playerId = player.getPlayerId();
        game.getPlayers().add(player);
        gameService.saveGame(game);

    }

    @RequestMapping("/findPath")
    public void findPath(@RequestParam int startx,
                         @RequestParam int starty,
                         @RequestParam int startz,
                         @RequestParam int endx,
                         @RequestParam int endy,
                         @RequestParam int endz) {
        BlockMap map = gameService.getGame(gameId).getMap();
        finder = new AStarPathFinder(map, 500, true);
        item = (Moveable) map.getItem(new Vector3d(startx, starty, startz)).iterator().next();
        item.setPathStep(0);
        item.setPath(
                finder.findPath(new UnitMover(0),
                        startx, starty, endx, endy));

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

        ForrestZone zone = new ForrestZone();
        //zone.setLocation(location);
        //zone.setExtent(extent);
        //player.getSocialGroups().iterator().next().getZones().add(zone);
        Vector3d min = new Vector3d(startx, starty, startz);
        Vector3d max = new Vector3d(endx, endy, endz);
        MineTask mineTask = new MineTask(new Bounds(min, max));

        player.getSocialGroups().iterator().next().getTasks().add(mineTask);

    }
}
