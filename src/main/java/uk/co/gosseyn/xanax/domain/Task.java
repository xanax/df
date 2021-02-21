package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public abstract class Task extends GameObject {
    @NonFinal
    int progress = 0;
    Set<TaskAssignment> taskAssignments = new HashSet<>();

    @NonFinal
    Status status = Status.CREATED;

    long repeatFrequency = 0;

    @NonFinal
    long lastRan;



    public void perform(final Game game) {
        if(game.getFrame() - lastRan < repeatFrequency) {
            return;
        }
        status = Status.CREATED;
        lastRan = game.getFrame();
    }

    public enum Status {
        CREATED, IN_PROGRESS, COMPLETE
    }
}
