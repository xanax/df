package uk.co.gosseyn.xanax.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

@Data
public class Game {
    private UUID gameId = UUID.randomUUID();
    private Map map;
    private List<Action> actionLog = new ArrayList<>();


    private List<Task> tasks = new ArrayList<>();

}
