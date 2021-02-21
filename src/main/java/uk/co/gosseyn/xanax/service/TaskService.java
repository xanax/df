package uk.co.gosseyn.xanax.service;

import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.SocialGroup;
import uk.co.gosseyn.xanax.domain.Task;
import uk.co.gosseyn.xanax.domain.TaskAssignable;
import uk.co.gosseyn.xanax.domain.TaskAssignment;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TaskService {
    public void assignTasks(Game game) {
        for(SocialGroup group : game.getSocialGroups()) {
            group.getTasks().removeIf(t -> t.getRepeatFrequency() == 0
                    && t.getStatus() == Task.Status.COMPLETE);

            List<Task> tasks = group.getTasks().stream()
                    .sorted(Comparator
                            .comparingInt(t -> t.getTaskAssignments().size())).collect(Collectors.toList());

            if(tasks.size() > 0) {
                final AtomicInteger taskIndex = new AtomicInteger();

                group.getMembers().stream()
                        .filter(m -> m instanceof TaskAssignable).map(TaskAssignable.class::cast)
                        .filter(a -> a.getCurrentTask() == null)
                        .forEach(t -> {
                            Task task = tasks.get(taskIndex.get());
                            TaskAssignment assignment = new TaskAssignment(task, t);
                            task.getTaskAssignments().add(assignment);
                            t.setCurrentTask(task);
                            if (taskIndex.incrementAndGet() >= tasks.size()) {
                                taskIndex.set(0);
                            }
                        });
                group.getTasks().forEach(t -> t.perform(game));
            }

        }
    }
}
