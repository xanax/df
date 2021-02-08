package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.Path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static uk.co.gosseyn.xanax.domain.BlockMap.TREE;
import static uk.co.gosseyn.xanax.domain.MineTask.MineTaskStatus.MOVING_TO_ITEM;
import static uk.co.gosseyn.xanax.domain.MineTask.MineTaskStatus.MOVING_TO_ZONE;

@Slf4j
@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class MineTask extends Task {
    Bounds bounds; // TODO change to zone?

    Set<Point> reserved = new HashSet<>();

    @Override
    public void perform(Game game) {

        for(TaskAssignment taskAssignment : getTaskAssignments()) {
            Moveable moveable = (Moveable) taskAssignment.getTaskAssignable();
            TaskStatus status = taskAssignment.getStatus();
            if(status == null) {
                // Start up - find path to zone
                moveable.setPath(pathFinderService.findPath(game.getMap(), moveable.getLocation(), bounds.center()));
                if(moveable.getPath() != null) {
                    moveable.setPathStep(0);
                    taskAssignment.setStatus(MOVING_TO_ZONE);
                } else {
                    taskAssignment.setStatus(MineTaskStatus.BLOCKED);
                }
            }
            if(status == MineTaskStatus.MOVING_TO_ZONE && bounds.contains(moveable.getLocation())) {
                // Arrived at zone
                moveable.setPath(null);
                taskAssignment.setStatus(MOVING_TO_ITEM);
            }
            if(status == MineTaskStatus.MOVING_TO_ITEM) {
                if(moveable.getPath() == null) {
                    moveable.setPath(mapService.pathToNearestBlock(game.getMap(), moveable.getLocation(), TREE, bounds, reserved));
                    moveable.setPathStep(0);
                    if(moveable.getPath() != null) {
                        Point point = moveable.getPath().getLastStep().getPoint().addz(moveable.getLocation().getZ());
                        // TODO conflict resolution in another class
                        reserved.add(point);
                    } else {
                        taskAssignment.setStatus(MineTaskStatus.BLOCKED);
                    }
                }
                if(moveable.getPath() != null && moveable.getPathStep() == moveable.getPath().getLength() - 2) {
                    mineBlock(game, moveable);
                }
            }
        }
    }

    private void mineBlock(final Game game, final Moveable moveable) {
        Path.Step nextStep = moveable.getPath().getStep(moveable.getPathStep()+1);
        Point nextPoint = new Point(nextStep.getX(), nextStep.getY(),
                moveable.getLocation().getZ());
        game.getChanges().add(new MineBlockChange(nextPoint));
        moveable.setPath(null);
    }

    public enum MineTaskStatus implements TaskStatus  {
        START(1), MOVING_TO_ZONE(2), MOVING_TO_ITEM(3), BLOCKED(4);

        int status;
        MineTaskStatus(int s) {
             status = s;
        }
        @Override
        public int getStatus() {
            return status;
        }
    }
}
