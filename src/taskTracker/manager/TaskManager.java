package taskTracker.manager;

import taskTracker.tasks.Epic;
import taskTracker.tasks.SubTask;
import taskTracker.tasks.Task;

public interface TaskManager {
    // Описание методов данного класса:
    void addTask();
    void deleteAllTasks();
    void removeTask();
    void showAllTasks();
    void showOneTask();
    void changeTask();
    void changeStatus();
    void newTask(Task task);
    void newEpic(Epic epic);
    void newSubTask(SubTask subTask);
}
