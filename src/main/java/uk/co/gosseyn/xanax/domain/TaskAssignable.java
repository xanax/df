package uk.co.gosseyn.xanax.domain;

import java.util.Collection;

public interface TaskAssignable {
    Collection<TaskAssignment> getTaskAssignments();

    Task getCurrentTask();
    void setCurrentTask(Task task);
}
