package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@Builder
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class Game {
    UUID gameId = UUID.randomUUID();

    BlockMap map;
    List<Action> actionLog = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    Collection<SocialGroup> socialGroups = new ArrayList<>();
    Collection<TaskAssignable> taskDoers = new ArrayList<>();


    private List<Task> tasks = new ArrayList<>();

}
