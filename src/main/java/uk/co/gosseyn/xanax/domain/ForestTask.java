package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

import static uk.co.gosseyn.xanax.domain.BlockMap.TREE;

@Slf4j
@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class ForestTask extends Task {
    ForestZone zone;

    Set<Point> reserved = new HashSet<>();

    @Override
    public void perform(Game game) {

        for(TaskAssignment taskAssignment : getTaskAssignments()) {
            MovingObject MovingObject = (MovingObject) taskAssignment.getTaskAssignable();
            TaskStatus status = taskAssignment.getStatus();
            if(status == null) {
                if(MovingObject.getPath() == null) {
                    MovingObject.setPath(mapService.pathToNearestBlock(game.getMap(), MovingObject.getLocation(), TREE, zone, reserved));
                    if(MovingObject.getPath() != null) {
                        Point point = MovingObject.getPath().getLastStep();
                        // TODO conflict resolution in another class
                        reserved.add(point);
                    } else {
                        taskAssignment.setStatus(MineTaskStatus.BLOCKED);
                    }
                }
                if(MovingObject.getPath() != null && MovingObject.getPathStep() == MovingObject.getPath().getLength() - 2) {
                    mineBlock(game, MovingObject);
                }
            }
        }
    }

    private void mineBlock(final Game game, final MovingObject MovingObject) {
        Point nextStep = MovingObject.getPath().getStep(MovingObject.getPathStep()+1);
        Point nextPoint = new Point(nextStep.getX(), nextStep.getY(),
                nextStep.getZ());
        game.getChanges().add(new MineBlockChange(nextPoint));
        MovingObject.setPath(null);
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
