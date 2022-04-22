package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;

public interface TaskManager {
    void newTask(Task task);
    void newEpic(Epic epic);
    void newSubTask(SubTask subTask);
    void deleteTaskById(Long taskId);
    void deleteEpicById(Long taskId);
    void deleteSubTaskById(Long taskId);
    void deleteAllTasks();
    void showTask(Long taskId);
    void showAllTasks();
    List<Task> history();
    //void loadFromFile();
    Task getTaskById (Long taskId);
    Epic getEpicById (Long taskId);
    SubTask getSubTaskById (Long taskId);
    void updateTask(Task task);
    void updateEpic (Epic epic);
    void updateSubtask(SubTask subTask);
}
