package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(Long id);
    List<Task> getHistory();
    void setListHead(Node listHead);
    void clearHistory();
}
