package uk.co.gosseyn.xanax.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Item {
    abstract public int getCode();
}
