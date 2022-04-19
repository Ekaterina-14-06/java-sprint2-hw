package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    // mapOfTasks - список задач типа Task
    public final HashMap<Long, Task> mapOfTasks = new HashMap<>();

    // mapOfEpics - список задач типа Epic
    public final HashMap<Long, Epic> mapOfEpics = new HashMap<>();

    // mapOfSubTasks - список задач типа SubTask
    public final HashMap<Long, SubTask> mapOfSubTasks = new HashMap<>();

    // countOfTasks содержит номер последней созданной задачи
    long countOfTasks = 0;

    // listOfFreeNumber - список, содержащий освободившиеся номера задач после их удаления
    ArrayList<Long> listOfFreeNumber = new ArrayList<>();

    private final HistoryManager taskHistory;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРОВ ДАННОГО КЛАССА

    public InMemoryTaskManager() {
        this.taskHistory = Managers.getDefaultHistory();
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    // Метод newTask создаёт новую задачу типа Task
    @Override
    public void newTask(Task task) {
        if (listOfFreeNumber.isEmpty()) {
            task.setTaskId(++countOfTasks);
        } else {
            task.setTaskId(listOfFreeNumber.get(0));
            listOfFreeNumber.remove(0);
        }
        mapOfTasks.put(task.getTaskId(), task);
        taskHistory.add(task);
    }

    // Метод newEpic создаёт новую задачу типа Epic
    @Override
    public void newEpic(Epic epic) {
        if (listOfFreeNumber.isEmpty()) {
            epic.setTaskId(++countOfTasks);
        } else {
            epic.setTaskId(listOfFreeNumber.get(0));
            listOfFreeNumber.remove(0);
        }
        mapOfEpics.put(epic.getTaskId(), epic);
        taskHistory.add(epic);
    }

    // Метод newSubTask создаёт новую задачу типа SubTask
    @Override
    public void newSubTask(SubTask subTask) {
        if (listOfFreeNumber.isEmpty()) {
            subTask.setTaskId(++countOfTasks);
        } else {
            subTask.setTaskId(listOfFreeNumber.get(0));
            listOfFreeNumber.remove(0);
        }
        mapOfSubTasks.put(subTask.getTaskId(), subTask);
        taskHistory.add(subTask);
    }

    // Метод getTaskById возвращает задачу по её номеру
    @Override
    public Task getTaskById(Long tasId){
        if (mapOfTasks.containsKey(tasId)) {
            return mapOfTasks.get(tasId);
        } else if (mapOfEpics.containsKey(tasId)) {
            return mapOfEpics.get(tasId);
        } else if (mapOfSubTasks.containsKey(tasId)) {
            return mapOfSubTasks.get(tasId);
        } else {
            return null;
        }
    }

    // Метод changeTask меняет значения параметров задачи на новые, полученные от пользователя
    @Override
    public void changeTask(Long taskId, String name, String description) {
        getTaskById(taskId).setTaskName(name);
        getTaskById(taskId).setTaskDescription(description);
        taskHistory.add(getTaskById(taskId));
        if (getTaskById(taskId) == null) {
            System.out.println("Такой задачи нет.");
        }
    }

    // Метод changeStatus меняет статус (new, in progress, done) выбранной задачи
    @Override
    public void changeStatus(Long taskId, TaskStatus newTaskStatus) {
        if (mapOfTasks.containsKey(taskId)) {
            getTaskById(taskId).setTaskStatus(newTaskStatus);
        } else if (mapOfEpics.containsKey(taskId)) {
            System.out.println("Задачи типа Epic самостоятельно менять статус не могут.\n" +
                    "Их состояние меняется автоматически при смене статусов её подзадач (SubTask).");
        } else if (mapOfSubTasks.containsKey(taskId)) {
            if (newTaskStatus == TaskStatus.IN_PROGRESS) {
                getTaskById(taskId).setTaskStatus(TaskStatus.IN_PROGRESS);
            } else if (newTaskStatus == TaskStatus.DONE) {
                getTaskById(taskId).setTaskStatus(TaskStatus.DONE);
                // Проверка остальных задач типа SubTask данной задачи типа Epic на соответствие состоянию done
                Long numberOfEpic = mapOfSubTasks.get(taskId).getNumberOfEpic();
                boolean isDone = false;
                for (Long key : mapOfSubTasks.keySet()) {
                    if (mapOfSubTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                        isDone = mapOfSubTasks.get(key).getTaskStatus().equals(TaskStatus.DONE);
                    }
                }
                if (isDone) {
                    mapOfEpics.get(numberOfEpic).setTaskStatus(TaskStatus.DONE);
                }
            } else {
                System.out.println("Вы ввели неверное значение.");
            }
            taskHistory.add(getTaskById(taskId));
        }
    }

    // Метод deleteTask удаляет задачу любого типа (Task, Epic, SubTask)
    @Override
    public void deleteTask(Long taskId) {
        taskHistory.add(getTaskById(taskId));
        if (mapOfTasks.containsKey(taskId)) {
            mapOfTasks.remove(taskId);
        } else if (mapOfEpics.containsKey(taskId)) {
            // Удаление подзадач типа SubTask, относящихся к выбранной задаче типа Epic
            boolean flag;
            do {
                Set<Long> keys = mapOfSubTasks.keySet();
                if (keys.isEmpty()) break;
                flag = false;
                for (Long key : keys) {
                    if (mapOfSubTasks.get(key).getNumberOfEpic().equals(taskId)) {
                        mapOfSubTasks.remove(key);
                        listOfFreeNumber.add(key);
                        flag = true;
                        break;
                    }
                }
            } while (flag);
            mapOfEpics.remove(taskId);
        } else if (mapOfSubTasks.containsKey(taskId)) {
            mapOfSubTasks.remove(taskId);
        }
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteAllTasks удаляет все задачи
    @Override
    public void deleteAllTasks() {
        mapOfTasks.clear();
        mapOfEpics.clear();
        mapOfSubTasks.clear();
    }

    // Метод showTask выводит на экран список задач любого типа (Task, Epic, SubTask)
    @Override
    public void showTask(Long taskId) {
        System.out.println("Имя задачи: " + getTaskById(taskId).getTaskName());
        System.out.println("Описание задачи: " + getTaskById(taskId).getTaskDescription());
        System.out.println("Статус задачи: " + getTaskById(taskId).getTaskStatus());
        if (mapOfEpics.containsKey(taskId)) {
            System.out.print("Данная задача типа Epic содержит подзадачи типа subTask со следующими номерами: ");
            for (Long key : mapOfSubTasks.keySet()) {
                if (mapOfSubTasks.get(key).getNumberOfEpic().equals(taskId)) {
                    System.out.print(mapOfSubTasks.get(key).getTaskId() + ", ");
                }
            }
            System.out.println("");
        } else {
            if (mapOfSubTasks.containsKey(taskId)) {
                System.out.println("Номер epic для задачи: " + mapOfSubTasks.get(taskId).getNumberOfEpic());
            }
        }
        taskHistory.add(getTaskById(taskId));
    }

    // Метод showAllTasks выводит на экран список всех задач любого из трёх типов (Task, Epic или SubTask)
    @Override
    public void showAllTasks() {
        if (mapOfTasks.size() != 0) {
            System.out.println("Список задач типа Task:");
            for (Long key : mapOfTasks.keySet()) {
                System.out.println(key + " - " + mapOfTasks.get(key).getTaskName());
            }
        } else {
            System.out.println("Нет ни одной задачи типа Task.");
        }
        if (mapOfEpics.size() != 0) {
            System.out.println("\nСписок задач типа Epic:");
            for (long key : mapOfEpics.keySet()) {
                System.out.println("\nЗадача \"" + mapOfEpics.get(key).getTaskName() +
                                   "\" со следующими подзадачами типа subTask:");
                boolean flag = false;
                for (Long key2 : mapOfSubTasks.keySet()) {
                    if (mapOfSubTasks.get(key2).getNumberOfEpic().equals(key)) {
                        System.out.println("  " + key2 + " - " + mapOfSubTasks.get(key2).getTaskName());
                        flag = true;
                    }
                }
                if (!flag) {
                    System.out.println("  У этой задачи нет подзадач.\n");
                }
            }
        } else {
            System.out.println("Нет ни одной задачи типа Epic и SubTask.");
        }
    }

    // Метод history выводит на экран упорядоченный список задач без повторов (историю вызова задач)
    @Override
    public List<Task> history() {
        return taskHistory.getHistory();
    }

    @Override
    public void loadFromFile(){

    }
}