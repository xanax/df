package uk.co.gosseyn.xanax.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Vector;

@Getter
@Setter
public class Game {
    private Map map;
    private List<Action> actionLog;

}
