package taskTracker.tasks;

import java.util.HashMap;

public class Epic extends Task {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    private HashMap<Integer, SubTask> mapOfSubTasks = new HashMap<>();

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРОВ ДАННОГО КЛАССА

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    public HashMap<Integer, SubTask> getMapOfSubTasks() {
        return mapOfSubTasks;
    }

    public void setMapOfSubTasks(HashMap<Integer, SubTask> mapOfSubTasks) {
        this.mapOfSubTasks = mapOfSubTasks;
    }

    // Переопределение метода toString класса Task: добавление вывода номеров задач типа SubTask данной задачи
    @Override
    public String toString() {
        String result = "";
        for (int subTaskKey : mapOfSubTasks.keySet()) {
            result = result + mapOfSubTasks.get(subTaskKey).getTaskId() + ", ";
        }
        return super.toString() +
               "В состав данной задачи входят подзадачи типа SubTask с номерами " + result + "\n";
    }
}