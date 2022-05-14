package taskTracker.manager;

import taskTracker.tasks.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

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
    LocalDateTime getEndTimeOfTask(Task task);
    void setEndTimeOfEpic(Epic epic);
    void setStartTimeOfEpic(Epic epic);
    LocalDateTime getEndTimeOfSubTask(SubTask subTask);
    TreeSet<Task> getPrioritizedTasks();
    boolean timeCheckOfTask (Task task);
    boolean timeCheckOfSubTask (SubTask task);
}
