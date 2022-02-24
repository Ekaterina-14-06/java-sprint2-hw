// Задание выполнила: Лядова Екатерина В.
// Пояснение:
// Все виды задач имеют единую сквозную нумерацию.
// Диапазон номеров задач входит в положительный диапазон типа int.

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        // Объявление локальных переменных:
        HashMap<Integer, Task> listOfTasks = new HashMap<>();
        HashMap<Integer, Epic> listOfEpics = new HashMap<>();
        int countOfTasks = 0; // countOfTasks содержит номер последней созданной задачи
        Integer currentNumberOfTasks; // currentNumberOfTasks содержит номер создаваемой задачи
        Manager manager = new Manager();
        ArrayList<Integer> listOfFreeNumber = new ArrayList<>(); // listOfFreeNumber - список, содержащий
        // освободившиеся номера задач после их удаления
        boolean flag; // flag == true, если новая задача создавалась под новым номером
        // flag == fulse, если новая задача создавалась под освободившимся номером

        // ------------------------------------------------------------------------------------------------------------
        // Наполнение "Трекера задач" тестовыми значениями:
        System.out.println(">>>>>>>>>> Проверка выполнения метода addNewTask <<<<<<<<<<\n");

        // -----------------------------------------------------
        // Задача № 1
        // Проверка наличия высвобождённых номеров задач в списке
        flag = false;
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = countOfTasks + 1;
            flag = true;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        switch (manager.addNewTask(currentNumberOfTasks,
                0,
                "task",
                "Задача № " + currentNumberOfTasks + " типа Task",
                "Подробное описание задачи № " + currentNumberOfTasks,
                "new",
                listOfTasks,
                listOfEpics)) {
            case (0):
                if (flag) {
                    ++countOfTasks;
                }
                System.out.println("Задание " + currentNumberOfTasks + " добавлено успешно.");
                break;
            case (1):
                System.out.println("Ошибка добавления подзадачи:\n" +
                        "указан номер несуществующей задачи типа Epic, " +
                        "для которой создаётся подзадача типа SubTask!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Новая задача не была добавлена.");
                break;
        }

        // -----------------------------------------------------
        // Задача № 2
        // Проверка наличия высвобождённых номеров задач в списке
        flag = false;
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = countOfTasks + 1;
            flag = true;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        switch (manager.addNewTask(currentNumberOfTasks,
                0,
                "epic",
                "Задача № " + currentNumberOfTasks + " типа Epic",
                "Подробное описание задачи № " + currentNumberOfTasks,
                "new",
                listOfTasks,
                listOfEpics)) {
            case (0):
                if (flag) {++countOfTasks;}
                System.out.println("Задание " + currentNumberOfTasks + " добавлено успешно.");
                break;
            case (1):
                System.out.println("Ошибка добавления подзадачи:\n" +
                        "указан номер несуществующей задачи типа Epic, " +
                        "для которой создаётся подзадача типа SubTask!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Новая задача не была добавлена.");
                break;
        }

        // -----------------------------------------------------
        // Задача № 3
        // Проверка наличия высвобождённых номеров задач в списке
        flag = false;
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = countOfTasks + 1;
            flag = true;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        switch (manager.addNewTask(currentNumberOfTasks,
                2,
                "subTask",
                "Задача № " + currentNumberOfTasks + " типа SubTask",
                "Подробное описание задачи № " + currentNumberOfTasks,
                "new",
                listOfTasks,
                listOfEpics)) {
            case (0):
                if (flag) {++countOfTasks;}
                System.out.println("Задание " + currentNumberOfTasks + " добавлено успешно.");
                break;
            case (1):
                System.out.println("Ошибка добавления подзадачи:\n" +
                        "указан номер несуществующей задачи типа Epic, " +
                        "для которой создаётся подзадача типа SubTask!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Новая задача не была добавлена.");
                break;
        }

        // -----------------------------------------------------
        // Задача № 4
        // Проверка наличия высвобождённых номеров задач в списке
        flag = false;
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = countOfTasks + 1;
            flag = true;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        switch (manager.addNewTask(currentNumberOfTasks,
                2,
                "subTask",
                "Задача № " + currentNumberOfTasks + " типа SubTask",
                "Подробное описание задачи № " + currentNumberOfTasks,
                "new",
                listOfTasks,
                listOfEpics)) {
            case (0):
                if (flag) {++countOfTasks;}
                System.out.println("Задание " + currentNumberOfTasks + " добавлено успешно.");
                break;
            case (1):
                System.out.println("Ошибка добавления подзадачи:\n" +
                        "указан номер несуществующей задачи типа Epic, " +
                        "для которой создаётся подзадача типа SubTask!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Новая задача не была добавлена.");
                break;
        }

        // -----------------------------------------------------
        // Задача № 5
        // Проверка наличия высвобождённых номеров задач в списке
        flag = false;
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = countOfTasks + 1;
            flag = true;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        switch (manager.addNewTask(currentNumberOfTasks,
                0,
                "epic",
                "Задача № " + currentNumberOfTasks + " типа Epic",
                "Подробное описание задачи № " + currentNumberOfTasks,
                "new",
                listOfTasks,
                listOfEpics)) {
            case (0):
                if (flag) {++countOfTasks;}
                System.out.println("Задание " + currentNumberOfTasks + " добавлено успешно.");
                break;
            case (1):
                System.out.println("Ошибка добавления подзадачи:\n" +
                        "указан номер несуществующей задачи типа Epic, " +
                        "для которой создаётся подзадача типа SubTask!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Новая задача не была добавлена.");
                break;
        }

        // -----------------------------------------------------
        // Задача № 6
        // Проверка наличия высвобождённых номеров задач в списке
        flag = false;
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = countOfTasks + 1;
            flag = true;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        switch (manager.addNewTask(currentNumberOfTasks,
                5,
                "subTask",
                "Задача № " + currentNumberOfTasks + " типа SubTask",
                "Подробное описание задачи № " + currentNumberOfTasks,
                "new",
                listOfTasks,
                listOfEpics)) {
            case (0):
                if (flag) {++countOfTasks;}
                System.out.println("Задание " + currentNumberOfTasks + " добавлено успешно.");
                break;
            case (1):
                System.out.println("Ошибка добавления подзадачи:\n" +
                        "указан номер несуществующей задачи типа Epic, " +
                        "для которой создаётся подзадача типа SubTask!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Новая задача не была добавлена.");
                break;
        }

        // ------------------------------------------------------------------------------------------------------------
        // Вывод на экран содержимого всего "Трекера задач":
        System.out.println("\n>>>>>>>>>> Проверка выполнения метода showAllTasks <<<<<<<<<<\n");

        manager.showAllTasks(listOfTasks, listOfEpics);

        // ------------------------------------------------------------------------------------------------------------
        // Обновление значений полей задач:
        System.out.println(">>>>>>>>>> Проверка выполнения метода updateTask <<<<<<<<<<\n");

        // Задача № 1
        switch (manager.updateTask(1,
                "Новое имя задачи № 1",
                "Новое описание задачи № 1",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Поля задания № " + 1 + " успешно обновлены.");
                break;
            case (1):
                System.out.println("Ошибка обновления задачи:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Задача не была обновлена.");
                break;
        }

        System.out.println("");
        // -----------------------------------------------------
        // Задача № 7 - Тестовый вариант, заранее ошибочный, когда указан номер несуществующей задачи
        switch (manager.updateTask(7,
                "Новое имя задачи № 7",
                "Новое описание задачи № 7",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Поля задания № " + 7 + " успешно обновлены.");
                break;
            case (1):
                System.out.println("Ошибка обновления задачи:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Задача не была обновлена.");
                break;
        }

        System.out.println("");
        // ------------------------------------------------------------------------------------------------------------
        // Вывод на экран значений полей конкретных задач:
        System.out.println(">>>>>>>>>> Проверка выполнения метода showTask <<<<<<<<<<\n");

        // Задача № 1 - Вывод на экран задания типа Task
        switch (manager.showTask(1,
                listOfTasks,
                listOfEpics)) {
            case (0):
                // Значение заданной задачи успешно выведено на экран.
                break;
            case (1):
                System.out.println("Ошибка вывода заданной задачи на экран:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера задачи должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
        }

        // -----------------------------------------------------
        // Задача № 2 - Вывод на экран задания типа Epic
        switch (manager.showTask(2,
                listOfTasks,
                listOfEpics)) {
            case (0):
                // Значение заданной задачи успешно выведено на экран.
                break;
            case (1):
                System.out.println("Ошибка вывода заданной задачи на экран:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера задачи должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
        }

        // -----------------------------------------------------
        // Задача № 3 - Вывод на экран задания типа SubTask
        switch (manager.showTask(3,
                listOfTasks,
                listOfEpics)) {
            case (0):
                // Значение заданной задачи успешно выведено на экран.
                break;
            case (1):
                System.out.println("Ошибка вывода заданной задачи на экран:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера задачи должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
        }


        // -----------------------------------------------------
        // Задача № 7 - Тестовый вариант, заранее ошибочный, когда указан номер несуществующей задачи
        switch (manager.showTask(7,
                listOfTasks,
                listOfEpics)) {
            case (0):
                // Значение заданной задачи успешно выведено на экран.
                break;
            case (1):
                System.out.println("Ошибка вывода заданной задачи на экран:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера задачи должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
        }

        // ------------------------------------------------------------------------------------------------------------
        // Изменение статуса задач:
        System.out.println("\n>>>>>>>>> Проверка выполнения метода changeTaskStatus <<<<<<<<<\n");

        // Задача № 1
        switch (manager.changeTaskStatus(1,
                "in progress",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Статус задача " + 1 + " изменён успешно.");
                break;
            case (1):
                System.out.println("Ошибка: самостоятельно менять статус задачи типа epic запрещено!\n" +
                        "Её статус меняется автоматически при изменении статуса входящих в неё подзадач типа subTask.");
                break;
            case (2):
                System.out.println("Ошибка: указан номер несуществующей задачи!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Статус задачи не был изменён.");
                break;
        }

        System.out.println("");
        // Задача № 2
        switch (manager.changeTaskStatus(2,
                "in progress",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Статус задача " + 2 + " изменён успешно.");
                break;
            case (1):
                System.out.println("Ошибка: самостоятельно менять статус задачи типа epic запрещено!\n" +
                        "Её статус меняется автоматически при изменении статуса входящих в неё подзадач типа subTask.");
                break;
            case (2):
                System.out.println("Ошибка: указан номер несуществующей задачи!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Статус задачи не был изменён.");
                break;
        }

        System.out.println("");
        // Задача № 3
        switch (manager.changeTaskStatus(3,
                "in progress",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Статус задача " + 3 + " изменён успешно.");
                break;
            case (1):
                System.out.println("Ошибка: самостоятельно менять статус задачи типа epic запрещено!\n" +
                        "Её статус меняется автоматически при изменении статуса входящих в неё подзадач типа subTask.");
                break;
            case (2):
                System.out.println("Ошибка: указан номер несуществующей задачи!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Статус задачи не был изменён.");
                break;
        }

        System.out.println("");
        // Задача № 4
        switch (manager.changeTaskStatus(4,
                "in progress",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Статус задача " + 4 + " изменён успешно.");
                break;
            case (1):
                System.out.println("Ошибка: самостоятельно менять статус задачи типа epic запрещено!\n" +
                        "Её статус меняется автоматически при изменении статуса входящих в неё подзадач типа subTask.");
                break;
            case (2):
                System.out.println("Ошибка: указан номер несуществующей задачи!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Статус задачи не был изменён.");
                break;
        }

        System.out.println("");
        // Задача № 5
        switch (manager.changeTaskStatus(5,
                "in progress",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Статус задача " + 5 + " изменён успешно.");
                break;
            case (1):
                System.out.println("Ошибка: самостоятельно менять статус задачи типа epic запрещено!\n" +
                        "Её статус меняется автоматически при изменении статуса входящих в неё подзадач типа subTask.");
                break;
            case (2):
                System.out.println("Ошибка: указан номер несуществующей задачи!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Статус задачи не был изменён.");
                break;
        }

        System.out.println("");
        // Задача № 6
        switch (manager.changeTaskStatus(6,
                "in progress",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Статус задача " + 6 + " изменён успешно.");
                break;
            case (1):
                System.out.println("Ошибка: самостоятельно менять статус задачи типа epic запрещено!\n" +
                        "Её статус меняется автоматически при изменении статуса входящих в неё подзадач типа subTask.");
                break;
            case (2):
                System.out.println("Ошибка: указан номер несуществующей задачи!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Статус задачи не был изменён.");
                break;
        }

        System.out.println("");
        // Задача № 7
        switch (manager.changeTaskStatus(7,
                "in progress",
                listOfTasks,
                listOfEpics)) {
            case (0):
                System.out.println("Статус задача " + 7 + " изменён успешно.");
                break;
            case (1):
                System.out.println("Ошибка: самостоятельно менять статус задачи типа epic запрещено!\n" +
                        "Её статус меняется автоматически при изменении статуса входящих в неё подзадач типа subTask.");
                break;
            case (2):
                System.out.println("Ошибка: указан номер несуществующей задачи!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Статус задачи не был изменён.");
                break;
        }

        // ------------------------------------------------------------------------------------------------------------
        // Вывод на экран содержимого всего "Трекера задач":
        System.out.println("\n>>>>>>>>> Проверка выполнения метода showAllTask <<<<<<<<<\n");

        manager.showAllTasks(listOfTasks, listOfEpics);

        // ------------------------------------------------------------------------------------------------------------
        // Удаление заданных задач:
        System.out.println(">>>>>>>>> Проверка выполнения метода deleteTask <<<<<<<<\n");

        // Задача № 3
        switch (manager.deleteTask(3,
                listOfTasks,
                listOfEpics)) {
            case (0):
                // Заданная задача удалена успешно.
                // Возвращаем номер удалённой задачи в список свободных номеров
                System.out.println("Задача № " + 3 + " успешно удалена.");
                listOfFreeNumber.add(3);
                break;
            case (1):
                System.out.println("Ошибка удаления заданной задачи:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Задача не была удалена.");
                break;
        }

        System.out.println("");
        // -----------------------------------------------------
        // Задача № 7 - Тестовый вариант, заранее ошибочный, когда указан номер несуществующей задачи
        switch (manager.deleteTask(7,
                listOfTasks,
                listOfEpics)) {
            case (0):
                // Заданная задача удалена успешно.
                // Возвращаем номер удалённой задачи в список свободных номеров
                System.out.println("Задача № " + 7 + " успешно удалена.");
                listOfFreeNumber.add(7);
                break;
            case (1):
                System.out.println("Ошибка удаления заданной задачи:\n" +
                        "указан номер несуществующей задачи. \n" +
                        "Значение номера должно быть в диапазоне от 0 до " + countOfTasks + " включительно.");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Задача не была удалена.");
                break;
        }

        // ------------------------------------------------------------------------------------------------------------
        // Вывод на экран содержимого всего "Трекера задач":
        System.out.println("\n>>>>>>>> Проверка выполнения метода showAllTasks <<<<<<<<\n");

        manager.showAllTasks(listOfTasks, listOfEpics);

        // ------------------------------------------------------------------------------------------------------------
        // Наполнение "Трекера задач" тестовыми значениями:
        System.out.println(">>>>>>>>>> Проверка выполнения метода addNewTask <<<<<<<<<<\n");

        // -----------------------------------------------------
        // Проверка наличия высвобождённых номеров задач в списке
        flag = false;
        if (listOfFreeNumber.isEmpty()) {
            currentNumberOfTasks = countOfTasks + 1;
            flag = true;
        } else {
            currentNumberOfTasks = listOfFreeNumber.get(0);
            listOfFreeNumber.remove(0);
        }
        switch (manager.addNewTask(currentNumberOfTasks,
                0,
                "task",
                "Задача № " + currentNumberOfTasks + " типа Task",
                "Подробное описание задачи № " + currentNumberOfTasks,
                "new",
                listOfTasks,
                listOfEpics)) {
            case (0):
                if (flag) {++countOfTasks;}
                System.out.println("Задание " + currentNumberOfTasks + " добавлено успешно.");
                break;
            case (1):
                System.out.println("Ошибка добавления подзадачи:\n" +
                        "указан номер несуществующей задачи типа Epic, " +
                        "для которой создаётся подзадача типа SubTask!");
                break;
            default:
                System.out.println("Произошла непредвиденная ошибка!\n" +
                        "Новая задача не была добавлена.");
                break;
        }

        // ------------------------------------------------------------------------------------------------------------
        // Вывод на экран содержимого всего "Трекера задач":
        System.out.println("\n>>>>>>>> Проверка выполнения метода showAllTasks <<<<<<<<\n");

        manager.showAllTasks(listOfTasks, listOfEpics);

        // ------------------------------------------------------------------------------------------------------------
        // Очистка содержимого всего "Трекера задач":
        System.out.println(">>>>>>>>> Проверка выполнения метода deleteAllTasks <<<<<<<<\n");

        manager.deleteAllTasks(listOfTasks, listOfEpics);
        countOfTasks = 0;
        listOfFreeNumber.clear();

        // ------------------------------------------------------------------------------------------------------------
        // Вывод на экран содержимого всего "Трекера задач":
        System.out.println(">>>>>>>> Проверка выполнения метода showAllTasks <<<<<<<<\n");

        manager.showAllTasks(listOfTasks, listOfEpics);

        // ------------------------------------------------------------------------------------------------------------
    }
}