package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import uk.co.gosseyn.xanax.view.GameFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Player implements HasGroups {

    // The facade in use by the player (eg web)
    @NonFinal
    GameFacade gameFacade;

    @NonFinal
    // Volatile ensures in atomicity and visibility of changes across threads. (remember cores have their own
    // cache/registers)
    volatile long lastUpdate;
    String playerId;
    @NonFinal
    Game game;
    Collection<SocialGroup> socialGroups = new ArrayList<>();
}
