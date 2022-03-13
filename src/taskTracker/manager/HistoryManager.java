package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.ArrayList;

public interface HistoryManager {
    public void add(Task task);
    public void getHistory();
}
