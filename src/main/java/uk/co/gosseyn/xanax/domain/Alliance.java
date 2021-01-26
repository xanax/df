package uk.co.gosseyn.xanax.domain;

import lombok.Data;

@Data
public class Alliance {
    float strength; // Strength of the alliance with the group
    private Alliable alliable;
    private HasGroups group;
}
