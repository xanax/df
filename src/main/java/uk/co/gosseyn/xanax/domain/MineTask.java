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
            if(bounds.contains(moveable.getLocation())
                    && taskAssignment.getStatus() == MineTaskStatus.MOVING_TO_ZONE) {
                moveable.setPath(null);
            }
            if(!bounds.contains(moveable.getLocation())) {
                // Not in zone, find route
                taskAssignment.setStatus(MineTaskStatus.MOVING_TO_ZONE);
                moveable.setPath(pathFinderService.findPath(game.getMap(), moveable.getLocation(), bounds.center()));
                moveable.setPathStep(0);
            } else if(moveable.getPath() != null
                    && reserved.contains(moveable.getPath().getLastStep().getPoint().addz(moveable.getLocation().getZ()))
                    && moveable.getPathStep() == moveable.getPath().getLength() - 2) {
                mineBlock(game, taskAssignment, moveable);
            } else if(moveable.getPath() == null){
                taskAssignment.setStatus(MineTaskStatus.MOVING_TO_ITEM);
                moveable.setPath(mapService.pathToNearestBlock(game.getMap(), moveable.getLocation(), TREE, bounds, reserved));
                moveable.setPathStep(0);
                if(moveable.getPath() != null) {
                    Point point = moveable.getPath().getLastStep().getPoint().addz(moveable.getLocation().getZ());
                    // TODO conflict resolution in another class
                    if(!reserved.contains(point)) {
                        reserved.add(point);
                        if(moveable.getPath().getLength() == 2) {
                            mineBlock(game, taskAssignment, moveable);
                        }
//                        game.getChanges().add(new ReserveBlockChange(this, point));
                    } else {
                        moveable.setPath(null);
                    }
                }
            }
        }
    }

    private void mineBlock(final Game game, final TaskAssignment taskAssignment, final Moveable moveable) {
        Path.Step nextStep = moveable.getPath().getStep(moveable.getPathStep()+1);
        Point nextPoint = new Point(nextStep.getX(), nextStep.getY(),
                moveable.getLocation().getZ());
        game.getChanges().add(new MineBlockChange(nextPoint));
        taskAssignment.setStatus(null);
        moveable.setPath(null);
    }

    public enum MineTaskStatus implements TaskStatus  {
        START(1), MOVING_TO_ZONE(2), MOVING_TO_ITEM(3);

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
