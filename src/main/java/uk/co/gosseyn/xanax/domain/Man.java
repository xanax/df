package uk.co.gosseyn.xanax.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import uk.co.gosseyn.xanax.domain.Item;

import java.util.List;


@Data
public class Man extends Item implements TaskAssignable, HasBehaviour, Moveable, Alliable {
    private Path path;
    private int pathStep;

    // Location man will come back to after wandering a little.
    private Vector3d baseLocation;
    @Override
    public int getCode() {
        return 1; // is this needed here?
    }

    @Override
    public void perform() {
        System.out.println();
    }

    @Override
    public List<Alliance> getAlliances() {
        return null;
    }
}
