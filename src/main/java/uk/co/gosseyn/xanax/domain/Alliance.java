package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Alliance {
    @NonFinal
    float strength; // Strength of the alliance with the socialGroup
    private Alliable alliable;
    private SocialGroup socialGroup;
}
