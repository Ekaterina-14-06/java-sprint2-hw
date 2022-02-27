package TaskTracker;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, SubTask> listOfSubTasks = new HashMap<>();

    public HashMap<Integer, SubTask> getListOfSubTasks() {
        return listOfSubTasks;
    }

    public void setListOfSubTasks(HashMap<Integer, SubTask> listOfSubTasks) {
        this.listOfSubTasks = listOfSubTasks;
    }

    // Переопределение метода toString класса Task: добавление вывода номеров задач типа SubTask данной задачи
    @Override
    public String toString() {
        String result = "";
        for (int subTaskKey : listOfSubTasks.keySet()) {
            result = result + listOfSubTasks.get(subTaskKey).getTaskId() + ", ";
        }
        return "Имя задачи: " + getTaskName() + "\n" +
                "Описание задачи: " + getTaskDescription() + "\n" +
                "Статус задачи: " + getTaskStatus() + "\n" +
                "В состав данной задачи входят подзадачи типа SubTask с номерами " + result + "\n";

    }
}