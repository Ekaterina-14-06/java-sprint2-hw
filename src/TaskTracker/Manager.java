package TaskTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {
    // Объявление локальных переменных:
    private HashMap<Long, Task> listOfTasks = new HashMap<>();
    // listOfTasks - список задач типа Task
    private HashMap<Long, Epic> listOfEpics = new HashMap<>();
    // listOfEpics - список задач типа Epic
    private HashMap<Long, SubTask> listOfSubTasks = new HashMap<>();
    // listOfSubTasks - список задач типа SubTask
    long countOfTasks = 0;
    // countOfTasks содержит номер последней созданной задачи
    long currentNumberOfTasks;
    // currentNumberOfTasks содержит номер создаваемой задачи
    ArrayList<Long> listOfFreeNumber = new ArrayList<>();
    // listOfFreeNumber - список, содержащий освободившиеся номера задач после их удаления
    Scanner scanner = new Scanner(System.in);

    // Описание методов данного класса:

    // Метод addNewTask создаёт новую задачу типа Task
    public void addNewTask(String name,
                           String description,
                           String status) {
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        // Создание объекта (задачи) типа Task:
        Task task = new Task();
        // Устанавление значений полей объекта типа Task перед его добавлением в HashMap listOfTasks:
        task.setTaskId(currentNumberOfTasks);
        task.setTaskName(name);
        task.setTaskDescription(description);
        task.setTaskStatus(status);
        // Добавление объекта (задачи) типа Task в HashMap listOfTasks:
        listOfTasks.put(currentNumberOfTasks, task);
    }

    // Метод addNewEpic создаёт новую задачу типа Epic
    public void addNewEpic(String name,
                           String description,
                           String status) {
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        // Создание объекта (задачи) типа Task:
        Epic epic = new Epic();
        // Устанавление значений полей объекта типа Task перед его добавлением в HashMap listOfTasks:
        epic.setTaskId(currentNumberOfTasks);
        epic.setTaskName(name);
        epic.setTaskDescription(description);
        epic.setTaskStatus(status);
        // Добавление объекта (задачи) типа Task в HashMap listOfTasks:
        listOfEpics.put(currentNumberOfTasks, epic);
    }

    // Метод addNewSubTask создаёт новую задачу типа SubTask
    public void addNewSubTask(String name,
                              String description,
                              String status,
                              Long numberOfEpic) {
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = ++countOfTasks;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        // Создание объекта (задачи) типа Task:
        SubTask subTask = new SubTask();
        // Устанавление значений полей объекта типа Task перед его добавлением в HashMap listOfTasks:
        subTask.setTaskId(currentNumberOfTasks);
        subTask.setTaskName(name);
        subTask.setTaskDescription(description);
        subTask.setTaskStatus(status);
        subTask.setNumberOfEpic(numberOfEpic);
        // Добавление объекта (задачи) типа Task в HashMap listOfTasks:
        listOfSubTasks.put(currentNumberOfTasks, subTask);
    }

    // Метод addTask создаёт новую задачу любого из трёх типов (Task, Epic или SubTask)
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
                addNewTask(name, description, "new");
                break;
            case ("2"):
                System.out.print("Введите имя задачи: ");
                do {
                    name = scanner.next();
                } while (name.length() == 0);

                System.out.print("Введите описание задачи: ");
                description = scanner.next();
                addNewEpic(name, description, "new");
                break;
            case ("3"):
                if (listOfEpics.size() == 0) {
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
                for (Long key : listOfEpics.keySet()) {
                    System.out.println(listOfEpics.get(key).getTaskId() + " - " + listOfEpics.get(key).getTaskName());
                }
                Long numberOfEpic;
                do {
                    System.out.print("Введите номер epic для данной subTask: ");
                    numberOfEpic = scanner.nextLong();
                    if (listOfEpics.containsKey(numberOfEpic)) {
                        addNewSubTask(name, description, "new", numberOfEpic);
                        break;
                    } else {
                        System.out.println("Такого epic пока нет.");
                    }
                } while (true);

                addNewSubTask(name, description, "new", numberOfEpic);
                break;
            default:
                System.out.println("Ошибка, неверный выбор меню. Задание не добавлено.");
        }
    }

    // Метод deleteAllTasks удаляет все задачи
    public void deleteAllTasks() {
        listOfTasks.clear();
        listOfEpics.clear();
        listOfSubTasks.clear();
    }

    // Метод deleteTask удаляет задачу типа Task
    public void deleteTask(Long numberOfTask) {
        listOfTasks.remove(numberOfTask);
    }

    // Метод deleteEpic удаляет задачу типа Epic
    public void deleteEpic(Long numberOfEpic) {
        // Удаление подзадач типа SubTask, относящихся к выбранной задаче типа Epic
        for (Long key : listOfSubTasks.keySet()) {
            if (listOfSubTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                listOfSubTasks.remove(key);
            }
        }
        // Удаление выбранной задачи типа Epic
        listOfEpics.remove(numberOfEpic);
    }

    // Метод deleteSubTask удаляет задачу типа SubTask
    public void deleteSubTask(Long numberOfSubTask) {
        listOfSubTasks.remove(numberOfSubTask);
    }

    // Метод removeTask удаляет задачу (любого из трёх типов - Task, Epic или SubTask), выбранную пользователем
    public void removeTask() {
        showAllTasks();
        System.out.print("Введите номер задачи, которую хотите удалить: ");
        Long numberOfTask = scanner.nextLong();
        if (listOfTasks.containsKey(numberOfTask)) {
            deleteTask(numberOfTask);
        } else if (listOfEpics.containsKey(numberOfTask)) {
            deleteEpic(numberOfTask);
        } else if (listOfSubTasks.containsKey(numberOfTask)) {
            Long numberOfEpic = listOfSubTasks.get(numberOfTask).getNumberOfEpic();
            deleteSubTask(numberOfTask);
            // Проверка на небходимость изменения статуса соответствующей задачи типа Epic,
            // если после удаления выбранной подзадачи (типа SubTask) остались только подзадачи,
            // принадлежащие той же задаче типа Epic, со статусом done
            boolean isDone = false;
            for (Long key : listOfSubTasks.keySet()) {
                if (listOfSubTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                    isDone = listOfSubTasks.get(key).getTaskStatus().equals("done");
                }
            }
            if (isDone) {
                listOfEpics.get(numberOfEpic).setTaskStatus("done");
            }
        }
    }

    // Метод showAllTasks выводит на экран список всех задач любого из трёх типов (Task, Epic или SubTask)
    public void showAllTasks() {
        if (listOfTasks.size() != 0) {
            System.out.println("Список задач типа Task:");
            for (Long key : listOfTasks.keySet()) {
                System.out.println(key + " - " + listOfTasks.get(key).getTaskName());
            }
        } else {
            System.out.println("Нет ни одной задачи типа Task.");
        }
        if (listOfEpics.size() != 0) {
            System.out.println("\nСписок задач типа Epic:");
            for (long key : listOfEpics.keySet()) {
                System.out.print("Задача " + listOfEpics.get(key).getTaskName() +
                                   " со следующими подзадачами типа subTask:");
                boolean flag = false;
                for (Long key2 : listOfSubTasks.keySet()) {
                    if (listOfSubTasks.get(key2).getNumberOfEpic().equals(key)) {
                        System.out.println("\n " + key2 + " - " + listOfSubTasks.get(key2).getTaskName());
                        flag = true;
                    }
                }
                if (!flag) {
                    System.out.println(" у этой задачи нет подзадач.");
                }
                //System.out.println("");
            }
        } else {
            System.out.println("Нет ни одной задачи типа Epic и SubTask.");
        }
    }

    // Метод showTask выводит на экран список задач типа Task
    public void showTask(Long numberOfTask) {
        System.out.println("Имя задачи: " + listOfTasks.get(numberOfTask).getTaskName());
        System.out.println("Описание задачи: " + listOfTasks.get(numberOfTask).getTaskDescription());
        System.out.println("Статус задачи: " + listOfTasks.get(numberOfTask).getTaskStatus());
    }

    // Метод showEpic выводит на экран список задач типа Epic
    public void showEpic(Long numberOfTask) {
        System.out.println("Имя задачи: " + listOfEpics.get(numberOfTask).getTaskName());
        System.out.println("Описание задачи: " + listOfEpics.get(numberOfTask).getTaskDescription());
        System.out.println("Статус задачи: " + listOfEpics.get(numberOfTask).getTaskStatus());
        System.out.print("Данный epic содержит следующие subTask: ");
        for (Long key : listOfSubTasks.keySet()) {
            if (listOfSubTasks.get(key).getNumberOfEpic().equals(numberOfTask)) {
                System.out.print(listOfSubTasks.get(key).getTaskId() + ", ");
            }
        }
        System.out.println("");
    }

    // Метод showSubTask выводит на экран список задач типа SubTask
    public void showSubTask(Long numberOfTask) {
        System.out.println("Имя задачи: " + listOfSubTasks.get(numberOfTask).getTaskName());
        System.out.println("Описание задачи: " + listOfSubTasks.get(numberOfTask).getTaskDescription());
        System.out.println("Статус задачи: " + listOfSubTasks.get(numberOfTask).getTaskStatus());
        System.out.println("Номер epic для задачи: " + listOfSubTasks.get(numberOfTask).getNumberOfEpic());
    }

    // Метод showOneTask выводит на экран задачу, выбранную пользователем
    public void showOneTask() {
        System.out.print("Введите номер задачи, параметры которой хотите увидеть: ");
        Long numberOfTask = scanner.nextLong();
        if (listOfTasks.containsKey(numberOfTask)) {
            showTask(numberOfTask);
        } else if (listOfEpics.containsKey(numberOfTask)) {
            showEpic(numberOfTask);
        } else if (listOfSubTasks.containsKey(numberOfTask)) {
            showSubTask(numberOfTask);
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    // Метод changeTask меняет значения параметров задачи на новые, полученные от пользователя
    public void changeTask() {
        showAllTasks();
        System.out.print("Ввведите номер задачи, которую хотите изменить: ");
        Long numberOfTask = scanner.nextLong();
        if (listOfTasks.containsKey(numberOfTask)) {
            System.out.print("Введите новое название задачи: ");
            listOfTasks.get(numberOfTask).setTaskName(scanner.next());
            System.out.print("Введите новое описание задачи: ");
            listOfTasks.get(numberOfTask).setTaskDescription(scanner.next());
        } else if (listOfEpics.containsKey(numberOfTask)) {
            System.out.print("Введите новое название задачи: ");
            listOfEpics.get(numberOfTask).setTaskName(scanner.next());
            System.out.print("Введите новое описание задачи: ");
            listOfEpics.get(numberOfTask).setTaskDescription(scanner.next());
        } else if (listOfSubTasks.containsKey(numberOfTask)) {
            System.out.print("Введите новое название задачи: ");
            listOfSubTasks.get(numberOfTask).setTaskName(scanner.next());
            System.out.print("Введите новое описание задачи: ");
            listOfSubTasks.get(numberOfTask).setTaskDescription(scanner.next());
        } else {
            System.out.println("Такой задачи нет.");
        }
    }


    // Метод changeStatus меняет статус (new, in progress, done) выбранной задачи
    public void changeStatus() {
        showAllTasks();
        System.out.print("Выберите номер задачи, статус которой хотите поменять: ");
        Long numberOfTask = scanner.nextLong();
        int status;
        if (listOfTasks.containsKey(numberOfTask)) {
            System.out.print("Введите новый статус (1 - in progress, 2 - done): ");
            status = scanner.nextInt();
            if (status == 1) {
                listOfTasks.get(numberOfTask).setTaskStatus("in progress");
            } else if (status == 2) {
                listOfTasks.get(numberOfTask).setTaskStatus("done");
            } else {
                System.out.println("Вы ввели неверное значение статуса.");
            }
        } else if (listOfEpics.containsKey(numberOfTask)) {
            System.out.println("Задачи типа Epic самостоятельно менять статус не могут.\n" +
                               "Их состояние меняется автоматически при смене статусов её подзадач (SubTask).");
        } else if (listOfSubTasks.containsKey(numberOfTask)) {
            System.out.println("Введите новый статус (1 - in progress, 2 - done): ");
            status = scanner.nextInt();
            if (status == 1) {
                listOfSubTasks.get(numberOfTask).setTaskStatus("in progress");
            } else if (status == 2) {
                listOfSubTasks.get(numberOfTask).setTaskStatus("done");
                // Проверка остальных задач типа SubTask данной задачи типа Epic на соответствие состоянию done
                Long numberOfEpic = listOfSubTasks.get(numberOfTask).getNumberOfEpic();
                boolean isDone = false;
                for (Long key : listOfSubTasks.keySet()) {
                    if (listOfSubTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                        isDone = listOfSubTasks.get(key).getTaskStatus().equals("done");
                    }
                }
                if (isDone) {
                    listOfEpics.get(numberOfEpic).setTaskStatus("done");
                }
            } else {
                System.out.println("Вы ввели неверное значение.");
            }
        }
    }
}