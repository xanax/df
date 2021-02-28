package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.math.BigInteger;
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

    BigInteger repeatFrequency = BigInteger.ZERO;

    @NonFinal
    BigInteger lastRan = BigInteger.ZERO;

    public void perform(final Game game) {
        if(game.getFrame().subtract(lastRan).compareTo(repeatFrequency) < 0) {
            return;
        }
        status = Status.CREATED;
        lastRan = game.getFrame();
    }

    public abstract boolean canDo(TaskAssignable taskAssignee);

    public abstract float suitability(TaskAssignable taskAssignee);

    public enum Status {
        CREATED, IN_PROGRESS, COMPLETE
    }
}
