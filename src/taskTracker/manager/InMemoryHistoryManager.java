package taskTracker.manager;

import taskTracker.tasks.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    //список для history
    ArrayList<Task> history = new ArrayList<>();

    public void add(Task task){
        if (history.size() == 10) { history.remove(0); }
        history.add(task);
    }

    //public ArrayList<Task> getHistory() {
    //    return history;
    //}

    public void getHistory() {
        System.out.println("История просмотров:");
        for (Task element : history) {
            System.out.println(element.getTaskName());
        }
    }
}
