package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static uk.co.gosseyn.xanax.domain.BlockMap.GRASS;


@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class MineBlockChange extends Change {
    ForestZone zone;
    Point location;

    @Override
    public void perform(final Game game) {
        game.getMap().setBlock(location, GRASS);
        zone.getTreeLocations().remove(location);
    }
}
