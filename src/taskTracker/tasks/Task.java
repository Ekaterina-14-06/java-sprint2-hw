package taskTracker.tasks;
import taskTracker.manager.TaskStatus;

public class Task {
    private Long taskId;
    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;

    public Task() {}

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

    @Override
    public String toString() {
        return "Имя задачи: " + taskName + "\n" +
                "Описание задачи: " + taskDescription + "\n" +
                "Статус задачи: " + taskStatus + "\n";
    }
}