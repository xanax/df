package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class TaskAssignment {
    Task task;
    TaskAssignable taskAssignable;

    @NonFinal
    TaskStatus status;
}
