package taskTracker.manager;

import java.io.File;
import java.io.IOException;

public class Managers {
    static String kvServerAddress = "http://localhost";
    static int kvServerPort = 8078;

    public static TaskManager getDefault() throws IOException {
        // Объект класса HttpTaskManager принимает URL к серверу KVServer вместо имени файла
        return new HttpTaskManager(kvServerAddress, kvServerPort);
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTasksManager() {
        return new FileBackedTasksManager("task_manager.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
