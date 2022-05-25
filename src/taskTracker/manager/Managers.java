package taskTracker.manager;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException {
        //return new InMemoryTaskManager();
        return new HttpTaskManager("http://localhost:8078/"); // <--- УКАЗАТЬ АДРЕС СЕРВЕРА KVServer
    }

    public static TaskManager getFileBackedTasksManager() {
        return new FileBackedTasksManager("task_manager.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
