package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.Collection;
import java.util.UUID;

@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Player implements HasGroups {
    UUID playerId = UUID.randomUUID();
    @NonFinal
    Game game;
    @NonFinal
    Collection<Zone> zones;
}
