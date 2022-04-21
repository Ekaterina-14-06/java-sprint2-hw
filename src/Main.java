// Задание выполнила: Лядова Екатерина В.

import taskTracker.manager.Managers;
import taskTracker.manager.TaskManager;
import taskTracker.tasks.*;

public class Main {
    public static void main(String[] args) {
        //TaskManager taskManager = Managers.getDefault();
        TaskManager taskManager = Managers.getFileBackedTasksManager();

        taskManager.loadFromFile();

        System.out.println("\nТЕСТИРОВАНИЕ РАБОТЫ ПРОГРАММЫ\n");

        System.out.println("1) Создание задачи № 1 типа Task.");
        taskManager.newTask(new Task("Задача № 1",
                                     "Описание задачи № 1 типа Task"));

        System.out.println("2) Создание задачи № 2 типа Task.");
        taskManager.newTask(new Task("Задача № 2",
                                     "Описание задачи № 2 типа Task"));

        System.out.println("3) Создание задачи № 3 типа Epic.");
        taskManager.newEpic(new Epic("Задача № 3",
                                     "Описание задачи № 3 типа Epic"));

        System.out.println("4) Создание задачи № 4 типа SubTask для задачи № 3 типа Epic.");
        taskManager.newSubTask(new SubTask("Задача № 4",
                                           "Описание задачи № 4 типа SubTask для задачи № 3 типа Epic",
                                           3L, TaskStatus.NEW));

        System.out.println("5) Создание задачи № 5 типа SubTask для задачи № 3 типа Epic.");
        taskManager.newSubTask(new SubTask("Задача № 5",
                                           "Описание задачи № 5 типа SubTask для задачи № 3 типа Epic",
                                           3L, TaskStatus.NEW));

        System.out.println("6) Создание задачи № 6 типа SubTask для задачи № 3 типа Epic.");
        taskManager.newSubTask(new SubTask("Задача № 6",
                                           "Описание задачи № 4 типа SubTask для задачи № 3 типа Epic",
                                           3L, TaskStatus.NEW));

        System.out.println("7) Создание задачи № 7 типа Epic.");
        taskManager.newEpic(new Epic("Задача № 7",
                                     "Описание задачи № 7 типа Epic"));

        System.out.println("\n8) Вывод на экран всех созданных задач:\n");
        taskManager.showAllTasks();

        System.out.println("9) Вывод на экран значений полей задачи № 3:\n");
        taskManager.showTask(3L);

        System.out.println("\n10) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n11) Вывод на экран значений полей задачи № 1:");
        taskManager.showTask(1L);

        System.out.println("\n12) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n13) Вывод на экран значений полей задачи № 6:");
        taskManager.showTask(6L);

        System.out.println("\n14) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n15) Вывод на экран значений полей задачи № 2:");
        taskManager.showTask(2L);

        System.out.println("\n16) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n17) Вывод на экран значений полей задачи № 2:");
        taskManager.showTask(2L);

        System.out.println("\n18) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n19) Вывод на экран значений полей задачи № 1:");
        taskManager.showTask(1L);

        System.out.println("\n20) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n21) Удаление задачи № 2.");
        taskManager.deleteTaskById(2L);

        System.out.println("\n22) Вывод на экран всех задач:");
        taskManager.showAllTasks();

        System.out.println("\n23) Удаление задачи № 3.");
        taskManager.deleteEpicById(3L);

        System.out.println("\n24) Вывод на экран всех задач:");
        taskManager.showAllTasks();

        System.out.println("\n25) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\nТЕСТИРОВАНИЕ ЗАВЕРШЕНО");
    }
}