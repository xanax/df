package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

import static uk.co.gosseyn.xanax.domain.BlockMap.ROCK;

@Slf4j
@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class ForestTask extends Task {
    ForestZone zone;

    Set<Point> reserved = new HashSet<>();

    @Override
    public void perform(Game game) {
        super.perform(game);

        Set<TaskAssignment> completed = new HashSet<>();
        for(TaskAssignment taskAssignment : getTaskAssignments()) {
            MovingObject movingobject = (MovingObject) taskAssignment.getTaskAssignable();
            TaskStatus status = taskAssignment.getStatus();


            if(status == null) {
                if(movingobject.getPath() == null) {
                    movingobject.setPath(mapService.pathToNearestBlock(game.getMap(), movingobject.getLocation(), ROCK, zone, reserved));
                    if(movingobject.getPath() != null) {
                        Point point = movingobject.getPath().getLastStep();
                        // TODO conflict resolution in another class
                        reserved.add(point);
                    } else {
                        completed.add(taskAssignment);
                        taskAssignment.setStatus(MineTaskStatus.BLOCKED);

                    }
                }
                if(movingobject.getPath() != null && movingobject.getPathStep() == movingobject.getPath().getLength() - 2) {
                    Point nextStep = movingobject.getPath().getStep(movingobject.getPathStep()+1);
                    game.getChanges().add(new MineBlockChange(zone, nextStep));
                    movingobject.setPath(null);
                }
            }
        }
        for(TaskAssignment taskAssignment : completed) {
            getTaskAssignments().remove(taskAssignment);
            taskAssignment.getTaskAssignable().setCurrentTask(null);
        }
        if(getTaskAssignments().isEmpty() && zone.getTreeLocations().isEmpty()) {
            setStatus(Status.COMPLETE);
        }
    }

    @Override
    public boolean canDo(final TaskAssignable taskAssignee) {
        return false;
    }

    @Override
    public float suitability(final TaskAssignable taskAssignee) {
        if(!(taskAssignee instanceof MovingObject)) {
            return Float.MAX_VALUE;
        }
        return ((MovingObject)taskAssignee).getLocation().distanceToPow2(zone.getBounds().center());
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
