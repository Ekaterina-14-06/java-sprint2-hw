package taskTracker.tasks;

public class SubTask extends Task {
    private Long numberOfEpic;

    public SubTask() {}

    public Long getNumberOfEpic() {
        return numberOfEpic;
    }

    public void setNumberOfEpic(Long numberOfEpic) {
        this.numberOfEpic = numberOfEpic;
    }

    // Переопределение метода toString класса Task: добавление вывода номера задачи типа Epic
    @Override
    public String toString() {
        return "Имя задачи: " + getTaskName() + "\n" +
                "Описание задачи: " + getTaskDescription() + "\n" +
                "Статус задачи: " + getTaskStatus() + "\n" +
                "Входит в состав задачи № " + numberOfEpic + " типа Epic.\n";
    }
}