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
public abstract class MovingObject extends GameObject implements Active, Locatable {
    @NonFinal
    Path path;
    @NonFinal
    int pathStep;

    public void  setPath(Path path) {
        this.path = path;
        this.setPathStep(0);
    }
}
