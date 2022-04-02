package taskTracker.manager;

import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

public interface TaskManager {
    void changeTask(Long taskId, String name, String description);
    void changeStatus(Long taskId, TaskStatus newTaskStatus);
    void deleteAllTasks();
    void showAllTasks();
    void test();
}
