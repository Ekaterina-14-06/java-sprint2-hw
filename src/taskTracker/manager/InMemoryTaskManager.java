package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    // mapOfTasks - список задач типа Task
    private final HashMap<Long, Task> mapOfTasks = new HashMap<>();

    // mapOfEpics - список задач типа Epic
    private final HashMap<Long, Epic> mapOfEpics = new HashMap<>();

    // mapOfSubTasks - список задач типа SubTask
    private final HashMap<Long, SubTask> mapOfSubTasks = new HashMap<>();

    // countOfTasks содержит номер последней созданной задачи
    long countOfTasks = 0;

    // currentNumberOfTasks содержит номер создаваемой задачи
    long currentNumberOfTasks;

    // listOfFreeNumber - список, содержащий освободившиеся номера задач после их удаления
    ArrayList<Long> listOfFreeNumber = new ArrayList<>();

    private final HistoryManager taskHistory;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРА ДАННОГО КЛАССА
    public InMemoryTaskManager() {
        this.taskHistory = Managers.getDefaultHistory();
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    // Метод newTask создаёт новую задачу типа Task
    public void newTask(Task task) {
        mapOfTasks.put(task.getTaskId(), task);
        taskHistory.add(task);
    }

    // Метод newEpic создаёт новую задачу типа Epic
    public void newEpic(Epic epic) {
        mapOfEpics.put(epic.getTaskId(), epic);
        taskHistory.add(epic);
    }

    // Метод newSubTask создаёт новую задачу типа SubTask
    public void newSubTask(SubTask subTask) {
        mapOfSubTasks.put(subTask.getTaskId(), subTask);
        taskHistory.add(subTask);
    }

    // Метод deleteTask удаляет задачу типа Task
    public void deleteTask(Long numberOfTask) {
        taskHistory.add(mapOfTasks.get(numberOfTask));
        mapOfTasks.remove(numberOfTask);
        listOfFreeNumber.add(numberOfTask);
    }

    // Метод deleteEpic удаляет задачу типа Epic
    public void deleteEpic(Long numberOfEpic) {
        // Удаление подзадач типа SubTask, относящихся к выбранной задаче типа Epic
        do {
            Set<Long> keys = mapOfSubTasks.keySet();
            if (keys.isEmpty()) break;
            boolean flag = false;
            for (Long key : keys) {
                if (mapOfSubTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                    mapOfSubTasks.remove(key);
                    listOfFreeNumber.add(key);
                    flag = true;
                    break;
                }
            }
            if (!flag) break;
        } while (true);

        taskHistory.add(mapOfEpics.get(numberOfEpic));
        // Удаление выбранной задачи типа Epic
        mapOfEpics.remove(numberOfEpic);
        listOfFreeNumber.add(numberOfEpic);
    }

    // Метод deleteSubTask удаляет задачу типа SubTask
    public void deleteSubTask(Long numberOfSubTask) {
        taskHistory.add(mapOfSubTasks.get(numberOfSubTask));
        mapOfSubTasks.remove(numberOfSubTask);
        listOfFreeNumber.add(numberOfSubTask);
    }

    // Метод showTask выводит на экран список задач типа Task
    public void showTask(Long numberOfTask) {
        System.out.println("Имя задачи: " + mapOfTasks.get(numberOfTask).getTaskName());
        System.out.println("Описание задачи: " + mapOfTasks.get(numberOfTask).getTaskDescription());
        System.out.println("Статус задачи: " + mapOfTasks.get(numberOfTask).getTaskStatus());
        taskHistory.add(mapOfTasks.get(numberOfTask));
    }

    // Метод showEpic выводит на экран список задач типа Epic
    public void showEpic(Long numberOfTask) {
        System.out.println("Имя задачи: " + mapOfEpics.get(numberOfTask).getTaskName());
        System.out.println("Описание задачи: " + mapOfEpics.get(numberOfTask).getTaskDescription());
        System.out.println("Статус задачи: " + mapOfEpics.get(numberOfTask).getTaskStatus());
        System.out.print("Данная задача типа Epic содержит подзадачи типа subTask со следующими номерами: ");
        for (Long key : mapOfSubTasks.keySet()) {
            if (mapOfSubTasks.get(key).getNumberOfEpic().equals(numberOfTask)) {
                System.out.print(mapOfSubTasks.get(key).getTaskId() + ", ");
            }
        }
        System.out.println("");
        taskHistory.add(mapOfEpics.get(numberOfTask));
    }

    // Метод showSubTask выводит на экран список задач типа SubTask
    public void showSubTask(Long numberOfTask) {
        System.out.println("Имя задачи: " + mapOfSubTasks.get(numberOfTask).getTaskName());
        System.out.println("Описание задачи: " + mapOfSubTasks.get(numberOfTask).getTaskDescription());
        System.out.println("Статус задачи: " + mapOfSubTasks.get(numberOfTask).getTaskStatus());
        System.out.println("Номер epic для задачи: " + mapOfSubTasks.get(numberOfTask).getNumberOfEpic());
        taskHistory.add(mapOfSubTasks.get(numberOfTask));
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

    // Метод test выполняет тестирование работы программы согласно заданному алгоритму и заданному набору входных данных
    @Override
    public void test() {
        System.out.println("\nТЕСТИРОВАНИЕ РАБОТЫ ПРОГРАММЫ\n");

        countOfTasks = 0;
        mapOfTasks.clear();
        mapOfEpics.clear();
        mapOfSubTasks.clear();
        listOfFreeNumber.clear();

        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        System.out.println("1) Создание задачи № " + currentNumberOfTasks + " - задача типа Task.");
        newTask(new Task(currentNumberOfTasks,
                "Задача № " + currentNumberOfTasks,
                "Описание задачи № " + currentNumberOfTasks,
                TaskStatus.NEW));

        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        System.out.println("2) Создание задачи № " + currentNumberOfTasks + " - задача типа Task.");
        newTask(new Task(currentNumberOfTasks,
                "Задача № " + currentNumberOfTasks,
                "Описание задачи № " + currentNumberOfTasks,
                TaskStatus.NEW));

        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        System.out.println("3) Создание задачи № " + currentNumberOfTasks + " - задача типа Epic.");
        newEpic(new Epic(currentNumberOfTasks,
                "Задача № " + currentNumberOfTasks,
                "Описание задачи № " + currentNumberOfTasks,
                TaskStatus.NEW));

        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        System.out.println("4) Создание задачи № " + currentNumberOfTasks + " - задача типа SubTask для задачи № 3.");
        newSubTask(new SubTask(currentNumberOfTasks,
                "Задача № " + currentNumberOfTasks,
                "Описание задачи № " + currentNumberOfTasks,
                TaskStatus.NEW,
                3L));

        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        System.out.println("5) Создание задачи № " + currentNumberOfTasks + " - задача типа SubTask для задачи № 3.");
        newSubTask(new SubTask(currentNumberOfTasks,
                "Задача № " + currentNumberOfTasks,
                "Описание задачи № " + currentNumberOfTasks,
                TaskStatus.NEW,
                3L));

        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        System.out.println("6) Создание задачи № " + currentNumberOfTasks + " - задача типа SubTask для задачи № 3.");
        newSubTask(new SubTask(currentNumberOfTasks,
                "Задача № " + currentNumberOfTasks,
                "Описание задачи № " + currentNumberOfTasks,
                TaskStatus.NEW,
                3L));

        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        System.out.println("7) Создание задачи № " + currentNumberOfTasks + " - задача типа Epic.");
        newEpic(new Epic(currentNumberOfTasks,
                "Задача № " + currentNumberOfTasks,
                "Описание задачи № " + currentNumberOfTasks,
                TaskStatus.NEW));

        System.out.println("\n8) Вывод на экран всех созданных задач:\n");
        showAllTasks();

        System.out.println("9) Вывод на экран значений полей задачи № 3:\n");
        showEpic(3L);

        System.out.println("\n10) Вывод на экран истории вызова задач:\n");
        taskHistory.history();

        System.out.println("\n11) Вывод на экран значений полей задачи № 1:");
        showTask(1L);

        System.out.println("\n12) Вывод на экран истории вызова задач:\n");
        taskHistory.history();

        System.out.println("\n13) Вывод на экран значений полей задачи № 6:");
        showSubTask(6L);

        System.out.println("\n14) Вывод на экран истории вызова задач:\n");
        taskHistory.history();

        System.out.println("\n15) Вывод на экран значений полей задачи № 2:");
        showTask(2L);

        System.out.println("\n16) Вывод на экран истории вызова задач:\n");
        taskHistory.history();

        System.out.println("\n17) Вывод на экран значений полей задачи № 2:");
        showTask(2L);

        System.out.println("\n18) Вывод на экран истории вызова задач:\n");
        taskHistory.history();

        System.out.println("\n19) Вывод на экран значений полей задачи № 1:");
        showTask(1L);

        System.out.println("\n20) Вывод на экран истории вызова задач:\n");
        taskHistory.history();

        System.out.println("\n21) Удаление задачи № 2.");
        deleteTask(2L);

        System.out.println("\n22) Вывод на экран всех задач:");
        showAllTasks();

        System.out.println("\n23) Удаление задачи № 3.");
        deleteEpic(3L);

        System.out.println("\n24) Вывод на экран всех задач:");
        showAllTasks();

        System.out.println("\n25) Вывод на экран истории вызова задач:\n");
        taskHistory.history();

        System.out.println("\nТЕСТИРОВАНИЕ РАБОТЫ ПРОГРАММЫ ЗАВЕРШЕНО");
    }
}