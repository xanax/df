package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Game {
    UUID gameId = UUID.randomUUID();
    BlockMap map;
    List<Action> actionLog = new ArrayList<>();
    @Singular Map<UUID, Player> players;



    private List<Task> tasks = new ArrayList<>();

}
