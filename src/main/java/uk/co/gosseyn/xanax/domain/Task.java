package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    Status status = Status.ASSIGNED;



    public abstract void perform(final Game game);

    public enum Status {
        ASSIGNED, IN_PROGRESS, COMPLETE
    }
}
