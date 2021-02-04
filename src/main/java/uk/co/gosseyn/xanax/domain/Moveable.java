package uk.co.gosseyn.xanax.domain;

import org.newdawn.slick.util.pathfinding.Path;

public interface Moveable extends Locatable, Active {
    Path getPath();
    int getPathStep();
    void setPathStep(int pathStep);
    void setPath(Path path);
}
