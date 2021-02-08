package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.newdawn.slick.util.pathfinding.Path;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class TraversePathCreatureTask extends CreatureTask {
    Creature creature;
    @NonFinal
    Path path;
    @NonFinal
    int pathStep;

    @Override
    public void update(final Game game) {
        if (this.getPath() != null && this.getPathStep() < this.getPath().getLength()) {
            this.setPathStep(this.getPathStep() + 1); // first one contains current
            //TODO check if blocked
            Point step = this.getPath().getStep(this.getPathStep());
            game.getChanges().add(new MoveBlockChange(creature, step));
        }
    }
}
