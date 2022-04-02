package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
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

    Scanner scanner = new Scanner(System.in);

    private final HistoryManager taskHistory;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРА ДАННОГО КЛАССА
    public InMemoryTaskManager() {
        this.taskHistory = Managers.getDefaultHistory();
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    // Метод addTask создаёт новую задачу любого из трёх типов (Task, Epic или SubTask)
    @Override
    public void addTask() {
        System.out.print("Выберите тип задачи (1 - task, 2 - epic, 3 - subtask): ");
        String choice;
        do {
            choice = scanner.next();
        } while (!(choice.equals("1") || choice.equals("2") || choice.equals("3")));

        String name;
        String description;
        switch (choice) {
            case ("1"):
                do {
                    System.out.print("Введите имя задачи: ");
                    name = scanner.next();
                    if (name.length() == 0) {
                        System.out.print("Имя не может быть пустым.");
                    } else {
                        break;
                    }
                } while (true);

                System.out.print("Введите описание задачи: ");
                description = scanner.next();

                if (listOfFreeNumber.isEmpty()) {
                    currentNumberOfTasks = ++countOfTasks;
                } else {
                    currentNumberOfTasks = listOfFreeNumber.get(0);
                    listOfFreeNumber.remove(0);
                }
                // Создание объекта (задачи) типа Task и
                // устанавка значений полей этого объекта перед его добавлением в HashMap mapOfTasks:
                Task task = new Task(currentNumberOfTasks, name, description, TaskStatus.NEW);
                // Добавление объекта (задачи) типа Task в HashMap mapOfTasks:
                newTask(task);
                break;
            case ("2"):
                System.out.print("Введите имя задачи: ");
                do {
                    name = scanner.next();
                } while (name.length() == 0);

                System.out.print("Введите описание задачи: ");
                description = scanner.next();

                if (listOfFreeNumber.isEmpty()) {
                    currentNumberOfTasks = ++countOfTasks;
                } else {
                    currentNumberOfTasks = listOfFreeNumber.get(0);
                    listOfFreeNumber.remove(0);
                }
                // Создание объекта (задачи) типа Epic и
                // устанавление значений полей этого объекта перед его добавлением в HashMap mapOfEpics:
                Epic epic = new Epic(currentNumberOfTasks, name, description, TaskStatus.NEW);
                // Добавление объекта (задачи) типа Epic в HashMap mapOfEpics:
                newEpic(epic);
                break;
            case ("3"):
                if (mapOfEpics.size() == 0) {
                    System.out.println("Нет ни одного epic для добавления subTask.");
                    break;
                }
                System.out.print("Введите имя задачи: ");
                do {
                    name = scanner.next();
                } while (name.length() == 0);

                System.out.print("Введите описание задачи: ");
                description = scanner.next();

                System.out.println("Выберите номер задачи типа Epic для данной задачи из следующего списка:");
                for (Long key : mapOfEpics.keySet()) {
                    System.out.println(mapOfEpics.get(key).getTaskId() + " - " + mapOfEpics.get(key).getTaskName());
                }
                Long numberOfEpic;
                do {
                    System.out.print("Введите номер epic для данной subTask: ");
                    numberOfEpic = scanner.nextLong();
                    if (mapOfEpics.containsKey(numberOfEpic)) {
                        if (listOfFreeNumber.isEmpty()) {
                            currentNumberOfTasks = ++countOfTasks;
                        } else {
                            currentNumberOfTasks = listOfFreeNumber.get(0);
                            listOfFreeNumber.remove(0);
                        }
                        // Создание объекта (задачи) типа SubTask и
                        // устанавление значений полей этого объекта перед его добавлением в HashMap mapOfSubTasks:
                        SubTask subTask = new SubTask(currentNumberOfTasks, name, description, TaskStatus.NEW, numberOfEpic);
                        // Добавление объекта (задачи) типа SubTask в HashMap mapOfSubTasks:
                        newSubTask(subTask);
                        break;
                    } else {
                        System.out.println("Такого epic пока нет.");
                    }
                } while (true);
                break;
            default:
                System.out.println("Ошибка, неверный выбор меню. Задание не добавлено.");
        }
    }

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

    // Метод changeTask меняет значения параметров задачи на новые, полученные от пользователя
    @Override
    public void changeTask() {
        showAllTasks();
        System.out.print("Ввведите номер задачи, которую хотите изменить: ");
        Long numberOfTask = scanner.nextLong();
        if (mapOfTasks.containsKey(numberOfTask)) {
            System.out.print("Введите новое название задачи: ");
            mapOfTasks.get(numberOfTask).setTaskName(scanner.next());

            System.out.print("Введите новое описание задачи: ");
            mapOfTasks.get(numberOfTask).setTaskDescription(scanner.next());

            taskHistory.add(mapOfTasks.get(numberOfTask));
        } else if (mapOfEpics.containsKey(numberOfTask)) {
            System.out.print("Введите новое название задачи: ");
            mapOfEpics.get(numberOfTask).setTaskName(scanner.next());

            System.out.print("Введите новое описание задачи: ");
            mapOfEpics.get(numberOfTask).setTaskDescription(scanner.next());

            taskHistory.add(mapOfEpics.get(numberOfTask));
        } else if (mapOfSubTasks.containsKey(numberOfTask)) {
            System.out.print("Введите новое название задачи: ");
            mapOfSubTasks.get(numberOfTask).setTaskName(scanner.next());

            System.out.print("Введите новое описание задачи: ");
            mapOfSubTasks.get(numberOfTask).setTaskDescription(scanner.next());

            taskHistory.add(mapOfSubTasks.get(numberOfTask));
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    // Метод changeStatus меняет статус (new, in progress, done) выбранной задачи
    @Override
    public void changeStatus() {
        showAllTasks();
        System.out.print("Выберите номер задачи, статус которой хотите поменять: ");
        Long numberOfTask = scanner.nextLong();
        int status;
        if (mapOfTasks.containsKey(numberOfTask)) {
            System.out.print("Введите новый статус (1 - in progress, 2 - done): ");
            status = scanner.nextInt();
            if (status == 1) {
                mapOfTasks.get(numberOfTask).setTaskStatus(TaskStatus.IN_PROGRESS);
            } else if (status == 2) {
                mapOfTasks.get(numberOfTask).setTaskStatus(TaskStatus.DONE);
            } else {
                System.out.println("Вы ввели неверное значение статуса.");
            }
            taskHistory.add(mapOfTasks.get(numberOfTask));
        } else if (mapOfEpics.containsKey(numberOfTask)) {
            System.out.println("Задачи типа Epic самостоятельно менять статус не могут.\n" +
                    "Их состояние меняется автоматически при смене статусов её подзадач (SubTask).");
        } else if (mapOfSubTasks.containsKey(numberOfTask)) {
            System.out.println("Введите новый статус (1 - in progress, 2 - done): ");
            status = scanner.nextInt();
            if (status == 1) {
                mapOfSubTasks.get(numberOfTask).setTaskStatus(TaskStatus.IN_PROGRESS);
            } else if (status == 2) {
                mapOfSubTasks.get(numberOfTask).setTaskStatus(TaskStatus.DONE);
                // Проверка остальных задач типа SubTask данной задачи типа Epic на соответствие состоянию done
                Long numberOfEpic = mapOfSubTasks.get(numberOfTask).getNumberOfEpic();
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
            taskHistory.add(mapOfSubTasks.get(numberOfTask));
        }
    }

    // Метод removeTask удаляет задачу любого из трёх типов (Task, Epic или SubTask), выбранную пользователем
    @Override
    public void removeTask() {
        showAllTasks();
        System.out.print("Введите номер задачи, которую хотите удалить: ");
        Long numberOfTask = scanner.nextLong();
        if (mapOfTasks.containsKey(numberOfTask)) {
            deleteTask(numberOfTask);
        } else if (mapOfEpics.containsKey(numberOfTask)) {
            deleteEpic(numberOfTask);
        } else if (mapOfSubTasks.containsKey(numberOfTask)) {
            Long numberOfEpic = mapOfSubTasks.get(numberOfTask).getNumberOfEpic();
            deleteSubTask(numberOfTask);
            // Проверка на небходимость изменения статуса соответствующей задачи типа Epic,
            // если после удаления выбранной подзадачи (типа SubTask) остались только подзадачи,
            // принадлежащие той же задаче типа Epic, со статусом done
            boolean isDone = false;
            for (Long key : mapOfSubTasks.keySet()) {
                if (mapOfSubTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                    isDone = mapOfSubTasks.get(key).getTaskStatus().equals(TaskStatus.DONE);
                }
            }
            if (isDone) {
                mapOfEpics.get(numberOfEpic).setTaskStatus(TaskStatus.DONE);
            }
        }
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

        // Следующая (закомментированная) реализация вызывает ошибку, поэтому сделан вариант, описанный ранее (выше)
        /*
        for (Long key : mapOfSubTasks.keySet()) {
            if (mapOfSubTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                mapOfSubTasks.remove(key);   // <---- Вызывает ошибку на второй итерации (при удалении второго элемента)
                listOfFreeNumber.add(key);
            }
        }
        */

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

    // Метод deleteAllTasks удаляет все задачи
    @Override
    public void deleteAllTasks() {
        mapOfTasks.clear();
        mapOfEpics.clear();
        mapOfSubTasks.clear();
    }

    // Метод showOneTask выводит на экран задачу, выбранную пользователем
    @Override
    public void showOneTask() {
        System.out.print("Введите номер задачи, параметры которой хотите увидеть: ");
        Long numberOfTask = scanner.nextLong();
        if (mapOfTasks.containsKey(numberOfTask)) {
            showTask(numberOfTask);
        } else if (mapOfEpics.containsKey(numberOfTask)) {
            showEpic(numberOfTask);
        } else if (mapOfSubTasks.containsKey(numberOfTask)) {
            showSubTask(numberOfTask);
        } else {
            System.out.println("Такой задачи нет.");
        }
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