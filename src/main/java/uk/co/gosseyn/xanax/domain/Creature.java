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
public abstract class Creature extends GameObject implements Moveable, TaskAssignable  {
    @NonFinal
    protected Point location;
    @NonFinal
    Path path;
    @NonFinal
    int pathStep;
    Set<TaskAssignment> taskAssignments = new HashSet<>();

    @Override
    public void update(final Game game) {
        if (this.getPath() != null && this.getPathStep() < this.getPath().getLength()) {
            this.setPathStep(this.getPathStep() + 1); // first one contains current
            //TODO check if blocked
            Path.Step step = this.getPath().getStep(this.getPathStep());
            game.getChanges().add(new MoveBlockChange(this, new Point(step.getX(), step.getY(),
                    this.getLocation().getZ())));
        }
    }
}
