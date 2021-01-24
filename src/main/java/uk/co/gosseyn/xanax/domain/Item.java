package uk.co.gosseyn.xanax.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.util.pathfinding.Path;

@Getter
@Setter
public abstract class Item {
    abstract public int getCode();
    private int pathStep;
    private Vector3d location;
    private Path path;
}
