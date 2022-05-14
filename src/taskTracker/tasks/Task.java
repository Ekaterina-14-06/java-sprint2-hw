package taskTracker.tasks;

import taskTracker.manager.TaskTipe;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    protected Long taskId;
    protected String taskName;
    protected String taskDescription;
    protected TaskStatus taskStatus;
    protected LocalDateTime startTime;
    protected Duration duration;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРОВ ДАННОГО КЛАССА

    public Task() {}

    public Task(Long taskId,
                String taskName,
                String taskDescription,
                TaskStatus taskStatus,
                LocalDateTime startTime,
                Duration duration) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Long taskId, String taskName, String taskDescription, TaskStatus taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public Task(Long taskId, String taskName, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
    }

    public Task(String taskName, String taskDescription, LocalDateTime ldt, Duration dur) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
        this.startTime = ldt;
        this.duration = dur;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Имя задачи: " + taskName + "\n" +
                "Описание задачи: " + taskDescription + "\n" +
                "Статус задачи: " + taskStatus + "\n" +
                "Дата, когда предполагается приступить к выполнению задачи: " + startTime + "\n" +
                "Продолжительность задачи, оценка того, сколько времени она займёт: " + duration + "\n";
    }
}