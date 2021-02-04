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


    public abstract void perform(final Game game);


}
