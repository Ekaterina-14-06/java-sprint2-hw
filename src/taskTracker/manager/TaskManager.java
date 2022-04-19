package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;

public interface TaskManager {
    void newTask(Task task);
    void newEpic(Epic epic);
    void newSubTask(SubTask subTask);
    void changeTask(Long taskId, String name, String description);
    void changeStatus(Long taskId, TaskStatus newTaskStatus);
    void deleteTask(Long taskId);
    void deleteAllTasks();
    void showTask(Long taskId);
    void showAllTasks();
    List<Task> history();
    Task getTaskById(Long taskId);
    public void loadFromFile();
}
