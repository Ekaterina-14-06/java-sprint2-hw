package taskTracker.manager;

import taskTracker.tasks.Task;

public interface TaskManager {
    public void addTask();
    public void deleteAllTasks();
    public void removeTask();
    public void showAllTasks();
    public void showOneTask();
    public void changeTask();
    public void changeStatus();
}
