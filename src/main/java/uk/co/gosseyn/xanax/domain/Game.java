package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@Builder
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
//@Entity
public class Game {
    //@Id
    UUID gameId = UUID.randomUUID();

    BlockMap map;
    List<Change> changes = new ArrayList<>();



    List<Active> activeItems = new ArrayList<>();
    List<Action> actionLog = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    Collection<SocialGroup> socialGroups = new ArrayList<>();


    private List<Task> tasks = new ArrayList<>();
    private List<TaskAssignment> taskAssignments = new ArrayList<>();

}
