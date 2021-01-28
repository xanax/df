package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import uk.co.gosseyn.xanax.domain.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Man extends Item implements TaskAssignable, HasBehaviour, Moveable, Alliable, HasNeeds {
    @NonFinal
    Path path;
    @NonFinal
    int pathStep;
    Collection<Alliance> alliances = new ArrayList<>();
    Collection<Need> needs = new ArrayList<>();

    // Location man will come back to after wandering a little.
    Vector3d baseLocation = new Vector3d();
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
