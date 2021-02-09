package uk.co.gosseyn.xanax.view.web;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.gosseyn.xanax.domain.Bounds;
import uk.co.gosseyn.xanax.domain.CanJoinSocialGroup;
import uk.co.gosseyn.xanax.domain.ForrestZone;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Man;
import uk.co.gosseyn.xanax.domain.MineTask;
import uk.co.gosseyn.xanax.domain.Moveable;
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

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
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
            this.newMap();
        }
        for(SocialGroup group : game.getSocialGroups()) {

            Collection<TaskAssignable> potentialAssignees = group.getMembers().stream()
                    .filter(m -> m instanceof TaskAssignable).map(TaskAssignable.class::cast)
                    .collect(Collectors.toList());
            // TODO need to assign > 1 to a tasks
            Collection<Task> unassigned = group.getTasks().stream().filter(t -> t.getTaskAssignments().isEmpty())
                    .collect(Collectors.toList());

            // TODO logic to determine best / who's able
            if(!potentialAssignees.isEmpty() && !unassigned.isEmpty()) {
                Task task = unassigned.iterator().next();
                for(TaskAssignable assignee : potentialAssignees) {
                    TaskAssignment taskAssignment = new TaskAssignment(task, assignee);
                    task.getTaskAssignments().add(taskAssignment);
                    assignee.getTaskAssignments().add(taskAssignment);
                }
            }
            //TODO process group level needs
            for(CanJoinSocialGroup member : group.getMembers()) {
                // TODO process individual needs
            }
            group.getTasks().forEach(t -> t.perform(game));

            gameService.update(game);
        }


        BlockMap map = game.getMap();
        if(item != null && item.getPath() != null && item.getPathStep() < item.getPath().getLength()) {
            item.setPathStep(item.getPathStep() + 1); // first one contains current
            Point step = item.getPath().getStep(item.getPathStep());
                map.removeItem(new Point(item.getLocation().getX(),
                        item.getLocation().getY(),
                        item.getLocation().getZ()),item);
                Point newLocation = new Point(step.getX(), step.getY(), item.getLocation().getZ());
                map.addItem(newLocation, item);
                item.setLocation(newLocation);
        }
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
        game.getTaskDoers().addAll(asList(man, man2));
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
        item = (Moveable) map.getItem(new Point(startx, starty, startz)).iterator().next();
        item.setPathStep(0);
        item.setPath(
                finder.findPath(new UnitMover(0),
                        new Point(startx, starty, startz), new Point(endx, endy, endz)));

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
        MineTask mineTask = new MineTask(new ForrestZone(map,new Bounds(min, max)));

        player.getSocialGroups().iterator().next().getTasks().add(mineTask);

    }
}
