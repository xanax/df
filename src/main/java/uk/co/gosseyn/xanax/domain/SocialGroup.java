package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.Collection;

@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class SocialGroup implements HasZones {
    @NonFinal
    Collection<Zone> zones;
}
