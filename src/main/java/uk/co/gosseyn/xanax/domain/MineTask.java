package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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

    @Override
    public void perform(Game game) {

        for(TaskAssignment taskAssignment : getTaskAssignments()) {
            Moveable moveable = (Moveable) taskAssignment.getTaskAssignable();
            if(!bounds.contains(moveable.getLocation())) {
                taskAssignment.setStatus(MineTaskStatus.MOVING_TO_ZONE);
                moveable.setPath(pathFinderService.findPath(game.getMap(), moveable.getLocation(), bounds.center()));
                moveable.setPathStep(0);
                //TODO
            } else {
                taskAssignment.setStatus(MineTaskStatus.MOVING_TO_ITEM);
                moveable.setPath(mapService.pathToNearestBlock(game.getMap(), moveable.getLocation(), TREE, bounds));
            }
        }
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
