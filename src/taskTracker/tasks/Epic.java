package taskTracker.tasks;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, SubTask> mapOfSubTasks = new HashMap<>();

    public Epic(Long taskId, String taskName, String taskDescription, TaskStatus taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
    }

    public HashMap<Integer, SubTask> getListOfSubTasks() {
        return mapOfSubTasks;
    }

    public void setListOfSubTasks(HashMap<Integer, SubTask> listOfSubTasks) {
        this.mapOfSubTasks = listOfSubTasks;
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