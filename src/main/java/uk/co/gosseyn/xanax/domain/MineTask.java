package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.Path;

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
                    game.getChanges().add(new MoveBlockChange(moveable, new Point(step.getX(), step.getY(),
                            moveable.getLocation().getZ())));
                    if(bounds.contains(moveable.getLocation())) {
                        moveable.setPath(null);
                        moveable.setPathStep(0);
                    }
                }
            }
        }

        System.out.println(this.getAssignees()+"mining");
    }
}
