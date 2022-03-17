package taskTracker.manager;

import taskTracker.tasks.Task;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    static final int MAX_COUNT_OF_TASKS_IN_HISTORY = 10;
    static LinkedList<Task> history;

    public InMemoryHistoryManager(){
        this.history = new LinkedList<>();
    }

    public void add(Task task){
        if (history.size() == MAX_COUNT_OF_TASKS_IN_HISTORY) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public void history () {
        System.out.println("История просмотров:");
        for (Task element : history) {
            System.out.println(element.getTaskName());
        }
    }
}
