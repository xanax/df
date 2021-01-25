package uk.co.gosseyn.xanax.view.web;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.Item;
import uk.co.gosseyn.xanax.domain.Map;
import uk.co.gosseyn.xanax.domain.Vector3d;
import uk.co.gosseyn.xanax.service.GameService;
import uk.co.gosseyn.xanax.service.MapService;

import javax.inject.Inject;
import java.lang.instrument.Instrumentation;

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
    private MapService mapService;

    private PathFinder finder;

    private Item item;

    @RequestMapping("/gameData")
    public GameDataDto gameData(@RequestParam int left,
                                @RequestParam int top,
                                @RequestParam int z,
                                @RequestParam int width,
                                @RequestParam int height) {
        Game game = gameService.getGame();
        Map map = game.getMap();

        if (left < 0) {
            left = 0;
        } else if (left > map.getWidthInTiles() - width) {
            left = map.getWidthInTiles() - width;
        }
        if (top < 0) {
            top = 0;
        } else if (top > map.getHeightInTiles() - height) {
            top = map.getHeightInTiles() - height;
        }
        if(item != null && item.getPathStep() < item.getPath().getLength()) {
            item.setPathStep(item.getPathStep() + 1); // first one contains current
            Path.Step step = item.getPath().getStep(item.getPathStep());
                map.removeItem(new Vector3d(item.getLocation().getX(),
                        item.getLocation().getY(),
                        item.getLocation().getZ()),item);
                Vector3d newLocation = new Vector3d(step.getX(), step.getY(), item.getLocation().getZ());
                map.addItem(newLocation, item);
                item.setLocation(newLocation);
        }
        return gameFacade.getGameData(left, top, z, width, height);
    }

    @RequestMapping("/newMap")
    public void newMap() {
        item = null;
        gameService.getGame().setMap(mapService.newMap(100, 100, 8, 15));
    }

    @RequestMapping("/findPath")
    public void findPath(@RequestParam int startx,
                         @RequestParam int starty,
                         @RequestParam int startz,
                         @RequestParam int endx,
                         @RequestParam int endy,
                         @RequestParam int endz) {
        finder = new AStarPathFinder(gameService.getGame().getMap(), 500, false);
        item = gameService.getGame().getMap().getItem(new Vector3d(startx, starty, startz)).iterator().next();
        item.setPathStep(0);
        item.setPath(
        finder.findPath(new UnitMover(0),
                startx, starty, endx, endy));

    }
}
