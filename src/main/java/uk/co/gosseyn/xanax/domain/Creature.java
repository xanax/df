package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.newdawn.slick.util.pathfinding.Path;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public abstract class Creature extends MovingObject implements TaskAssignable  {
    @NonFinal
    protected Point location;
    @NonFinal
    Set<TaskAssignment> taskAssignments = new HashSet<>();
    @NonFinal
    CreatureTask OLDcurrentTask; //TODO maybe delete
    @NonFinal
    Task currentTask;

    @Override
    public void update(final Game game) {
        if (this.getPath() != null && this.getPathStep() < this.getPath().getLength()) {
            this.setPathStep(this.getPathStep() + 1); // first one contains current
            //TODO check if blocked
            Point step = this.getPath().getStep(this.getPathStep());
            game.getChanges().add(new MoveBlockChange(this, step));
        }
    }
}
