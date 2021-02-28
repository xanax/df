package uk.co.gosseyn.xanax.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.SocialGroup;
import uk.co.gosseyn.xanax.domain.Task;
import uk.co.gosseyn.xanax.domain.TaskAssignable;
import uk.co.gosseyn.xanax.domain.TaskAssignment;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TaskService {
    public void assignTasks(Game game) {
        for(SocialGroup group : game.getSocialGroups()) {

            // Remove non-repeatable and completed tasks (one time tasks)
            group.getTasks().removeIf(t -> t.getRepeatFrequency().equals(BigInteger.ZERO)
                    && t.getStatus() == Task.Status.COMPLETE);

            // Get all tasks sorted by number of assignees
            List<Task> tasks = group.getTasks().stream()
                    .sorted(Comparator
                            .comparingInt(t -> t.getTaskAssignments().size())).collect(Collectors.toList());

            if(!CollectionUtils.isEmpty(tasks)) {
                final AtomicInteger taskIndex = new AtomicInteger();

                //TODO separate into task types so can check suitablity



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
