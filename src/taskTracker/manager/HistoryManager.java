package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);
    void remove(Long id);
    List<Task> getHistory();
    void setListHead(Node listHead);
    void clearHistory();
    Map<Long, Node> getHistoryHashMap();
}
