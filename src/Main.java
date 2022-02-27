// Задание выполнила: Лядова Екатерина В.
import TaskTracker.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("\nВыберите пункт меню:\n" +
                    "1 - добавить задачу\n" +
                    "2 - изменить задачу\n" +
                    "3 - изменить статус задачи\n" +
                    "4 - удалить задачу\n" +
                    "5 - очистить список задач\n" +
                    "6 - показать задачу\n" +
                    "7 - показать все задачи\n" +
                    "0 - выход из программы");
            choice = scanner.next();
            switch (choice) {
                case ("1"):
                    manager.addTask();
                    break;
                case ("2"):
                    manager.changeTask();
                    break;
                case ("3"):
                    manager.changeStatus();
                    break;
                case ("4"):
                    manager.removeTask();
                    break;
                case ("5"):
                    manager.deleteAllTasks();
                    break;
                case ("6"):
                    manager.showOneTask();
                    break;
                case ("7"):
                    manager.showAllTasks();
                    break;
                case ("0"):
                    break;
                default:
                    System.out.println("Вы ввели неверную команду. Попробуйте снова.");
            }
        } while (!(choice.equals("0")));
    }
}