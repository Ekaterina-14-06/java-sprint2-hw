package taskTracker.manager;

import taskTracker.tasks.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface TaskManager {
    void newTask(Task task) throws MyException;
    void newEpic(Epic epic) throws MyException;
    void newSubTask(SubTask subTask) throws MyException;
    void deleteTaskById(Long taskId) throws MyException;
    void deleteEpicById(Long taskId) throws MyException;
    void deleteSubTaskById(Long taskId) throws MyException;
    void deleteAllTasks() throws MyException;
    void showTask(Long taskId) throws MyException;
    void showAllTasks();
    List<Task> history();
    //void loadFromFile();
    Task getTaskById (Long taskId);
    Epic getEpicById (Long taskId);
    SubTask getSubTaskById (Long taskId);
    void updateTask(Task task) throws MyException;
    void updateEpic (Epic epic) throws MyException;
    void updateSubtask(SubTask subTask) throws MyException;
    LocalDateTime getEndTimeOfTask(Task task);
    void setEndTimeOfEpic(Epic epic);
    void setStartTimeOfEpic(Epic epic);
    LocalDateTime getEndTimeOfSubTask(SubTask subTask);
    Set<Task> getPrioritizedTasks();
    boolean timeCheckOfTask (Task task);
    boolean timeCheckOfSubTask (SubTask task);
}
