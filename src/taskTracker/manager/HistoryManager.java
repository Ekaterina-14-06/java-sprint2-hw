package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void history();
    List<Task> getHistory();

    void removeNode(Task historyNode);
}
