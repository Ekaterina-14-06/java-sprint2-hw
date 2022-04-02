package taskTracker.tasks;

public class SubTask extends Task {
    private Long numberOfEpic;

    public SubTask(Long taskId, String taskName, String taskDescription, TaskStatus taskStatus, Long numberOfEpic) {
        super(taskId, taskName, taskDescription, taskStatus);
        this.numberOfEpic = numberOfEpic;
    }

    public Long getNumberOfEpic() {
        return numberOfEpic;
    }

    public void setNumberOfEpic(Long numberOfEpic) {
        this.numberOfEpic = numberOfEpic;
    }

    // Переопределение метода toString класса Task: добавление вывода номера задачи типа Epic
    @Override
    public String toString() {
        return super.toString() +
               "Входит в состав задачи № " + numberOfEpic + " типа Epic.\n";
    }
}