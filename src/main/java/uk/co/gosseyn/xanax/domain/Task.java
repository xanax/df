package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public abstract class Task extends GameObject {
    @NonFinal
    int progress = 0;
    Set<TaskAssignable> assignees = new HashSet<>();
    List<Task> subTasks = new ArrayList<>();

    public abstract void perform(final Game game);

}
