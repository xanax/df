package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.newdawn.slick.util.pathfinding.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Man extends Creature implements HasBehaviour, Alliable, HasNeeds, CanJoinSocialGroup, Nameable {
    String name;
    Collection<Alliance> alliances = new ArrayList<>();
    Collection<Need> needs = new ArrayList<>();

    // Location man will come back to after wandering a little.
    Point baseLocation = new Point();
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
