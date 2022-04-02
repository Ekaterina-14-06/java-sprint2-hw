// Задание выполнила: Лядова Екатерина В.

import taskTracker.manager.Managers;
import taskTracker.manager.TaskManager;
import taskTracker.manager.HistoryManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        String choice;
        System.out.println("Тестирование работы программы в соответствии заданному алгоритму\n");
        taskManager.test();
    }
}