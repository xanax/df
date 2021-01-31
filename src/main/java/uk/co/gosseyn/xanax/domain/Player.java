package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Player implements HasGroups {
    UUID playerId = UUID.randomUUID();
    @NonFinal
    Game game;
    Collection<SocialGroup> socialGroups = new ArrayList<>();
    public Player() {
        this.socialGroups.add(new SocialGroup(this));
    }
}
