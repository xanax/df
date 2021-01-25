package uk.co.gosseyn.xanax.domain;

import lombok.Data;

import java.util.Set;

@Data
public class Task {
    private Set<TaskAssignable> assignees;
}
