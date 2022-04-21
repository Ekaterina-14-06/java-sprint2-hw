package taskTracker.tasks;

public class SubTask extends Task {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    private Long numberOfEpic;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРОВ ДАННОГО КЛАССА

    public SubTask(String taskName, String taskDescription, Long numberOfEpic, TaskStatus taskStatus) {
        super(taskName, taskDescription);
        this.numberOfEpic = numberOfEpic;
        this.taskStatus = taskStatus;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

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