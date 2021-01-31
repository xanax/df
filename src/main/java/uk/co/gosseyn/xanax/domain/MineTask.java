package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.Path;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class MineTask extends Task {
    Bounds bounds; // TODO change to zone?

    @Override
    public void perform(Game game) {
        List<Moveable> moveables = this.getAssignees().stream().map(Moveable.class::cast).collect(Collectors.toList());
        log.debug("Task assignees: %s", moveables);
        for(Moveable moveable : moveables) {
            //TODO cancel any current paths
            if(moveable.getPath() == null) {
                if(!bounds.contains(moveable.getLocation())) {
                    moveable.setPath(pathFinderService.findPath(game.getMap(), moveable.getLocation(), bounds.center()));
                }
            } else {
                if(moveable.getPathStep() < moveable.getPath().getLength()) {
                    moveable.setPathStep(moveable.getPathStep() + 1); // first one contains current
                    //TODO check if blocked
                    Path.Step step = moveable.getPath().getStep(moveable.getPathStep());
                    BlockMap map = game.getMap();
                    map.removeItem(new Vector3d(moveable.getLocation().getX(),
                            moveable.getLocation().getY(),
                            moveable.getLocation().getZ()),moveable);
                    Vector3d newLocation = new Vector3d(step.getX(), step.getY(), moveable.getLocation().getZ());
                    map.addItem(newLocation, moveable);
                    moveable.setLocation(newLocation);
                    // TODO: check if arrived (within bounds)
                }
            }
        }

        System.out.println(this.getAssignees()+"mining");
    }
}
