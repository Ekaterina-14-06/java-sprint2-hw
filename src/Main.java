// Задание выполнила: Лядова Екатерина В.
import taskTracker.manager.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        //InMemoryTaskManager manager = new InMemoryTaskManager();
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("\nВыберите пункт меню (его номер):\n" +
                    "1 - добавить задачу\n" +
                    "2 - изменить задачу\n" +
                    "3 - изменить статус задачи\n" +
                    "4 - удалить задачу\n" +
                    "5 - очистить список задач\n" +
                    "6 - показать задачу\n" +
                    "7 - показать все задачи\n" +
                    "8 - показать историю просмотра задач (10 последних)\n" +
                    "0 - выход из программы");
            choice = scanner.next();
            switch (choice) {
                case ("1"):
                    taskManager.addTask();
                    break;
                case ("2"):
                    taskManager.changeTask();
                    break;
                case ("3"):
                    taskManager.changeStatus();
                    break;
                case ("4"):
                    taskManager.removeTask();
                    break;
                case ("5"):
                    taskManager.deleteAllTasks();
                    break;
                case ("6"):
                    taskManager.showOneTask();
                    break;
                case ("7"):
                    taskManager.showAllTasks();
                    break;
                case ("8"):
                    historyManager.history();
                    break;
                case ("0"):
                    break;
                default:
                    System.out.println("Вы ввели неверную команду. Попробуйте снова.");
            }
        } while (!(choice.equals("0")));
    }
}