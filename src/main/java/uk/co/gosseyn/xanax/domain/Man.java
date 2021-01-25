package uk.co.gosseyn.xanax.domain;

import lombok.Getter;
import lombok.Setter;
import uk.co.gosseyn.xanax.domain.Item;


public class Man extends Item implements TaskAssignable {
    @Override
    public int getCode() {
        return 1; // is this needed here?
    }
}
