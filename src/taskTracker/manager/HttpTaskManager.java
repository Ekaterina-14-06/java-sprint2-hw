package taskTracker.manager;

import taskTracker.tasks.Epic;
import taskTracker.tasks.SubTask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

// В этом классе (HttpTaskManager) необходимо заменить вызовы сохранения в файл на вызов клиента.

// Класс HttpTaskManager наследуется от класса FileBackedTasksManager.
public class HttpTaskManager extends FileBackedTasksManager {

    // Объявление КОНСТРУКТОРА КЛАССА
    // Объект класса принимает URL к серверу KVServer вместо имени файла
    public HttpTaskManager(String pathOfKVServer) throws IOException{
        // Класс HttpTaskManager создаёт kvTaskClient, из которого можно получить исходное состояние менеджера
        KVTaskClient kvTaskClient = new KVTaskClient();
    }

    @Override
    public void newTask(Task task) {
        super.newTask(task);
    }

    @Override
    public void newEpic(Epic epic) {
        super.newEpic(epic);
    }

    @Override
    public void newSubTask(SubTask subTask) {
        super.newSubTask(subTask);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    public void updateEpic (Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
    }

    @Override
    public void deleteTaskById(Long taskId) {
        super.deleteTaskById(taskId);
    }

    @Override
    public void deleteEpicById(Long taskId) {
        super.deleteEpicById(taskId);
    }

    @Override
    public void deleteSubTaskById(Long taskId) {
        super.deleteSubTaskById(taskId);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public void showTask(Long taskId) {
        super.showTask(taskId);
    }

    @Override
    public void showAllTasks() {
        super.showAllTasks();
    }

    @Override
    public List<Task> history() {
        return super.history();
    }

    @Override
    public Task getTaskById(Long taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public LocalDateTime getEndTimeOfTask(Task task) {
        return super.getEndTimeOfTask(task);
    }

    @Override
    public void setEndTimeOfEpic(Epic epic) {
        super.setEndTimeOfEpic(epic);
    }

    @Override
    public void setStartTimeOfEpic(Epic epic) {
        super.setStartTimeOfEpic(epic);
    }

    @Override
    public LocalDateTime getEndTimeOfSubTask(SubTask subTask) {
        return super.getEndTimeOfSubTask(subTask);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public boolean timeCheckOfTask (Task task) {
        return super.timeCheckOfTask(task);
    }

    @Override
    public boolean timeCheckOfSubTask (SubTask task) {
        return super.timeCheckOfSubTask(task);
    }

    @Override
    public void save() {
        super.save();
    }

    @Override
    public String toString(Task task) {
        return super.toString(task);
    }

    @Override
    public String taskToString(Task task) {
        return super.taskToString(task);
    }

    @Override
    public String epicToString(Task task) {
        return super.epicToString(task);
    }

    @Override
    public String subTaskToString(Task task) {
        return super.subTaskToString(task);
    }

    @Override
    public Task taskFromString(String value) {
        return super.taskFromString(value);
    }

    @Override
    public Epic epicFromString(String value) {
        return super.epicFromString(value);
    }

    @Override
    public SubTask subTaskFromString(String value) {
        return super.subTaskFromString(value);
    }

    @Override
    public void loadFromFile(){
        super.loadFromFile();
    }

    @Override
    public boolean fileExists(String fileName) {
        return super.fileExists(fileName);
    }
}
