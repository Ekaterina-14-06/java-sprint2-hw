package taskTracker.manager;

import taskTracker.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    static final int MAX_COUNT_OF_TASKS_IN_HISTORY = 10;
    static List<Task> history;
    static Map<Long, Integer> historyHashMap = new HashMap<>(); //Integer - номер узла связанного списка
    static LinkedList<Task> historyLinkedList = new LinkedList<>();


    public InMemoryHistoryManager(){
        this.history = new ArrayList<>();
    }

    public void add(Task task){
        /*
        if (history.size() == MAX_COUNT_OF_TASKS_IN_HISTORY) {
            history.remove(0);
        }
        history.add(task);
        */
        linkLast(task);
        removeNode(task);
    }

    @Override
    public List<Task> getHistory () {
        return history;
    }

    @Override
    public void history(){
        System.out.println("История просмотров:");
        for (Task element : getTasks()){
            System.out.println(element.getTaskName());
        }
    }

    public void linkLast(Task task){
        historyLinkedList.addLast(task);
    }

    public LinkedList<Task> getTasks() {
        return historyLinkedList;
    }

    @Override
    public void removeNode(Task removableTask) {
        if (historyLinkedList.size() > MAX_COUNT_OF_TASKS_IN_HISTORY) {
            historyHashMap.remove(historyLinkedList.get(0).getTaskId());
            historyLinkedList.remove(0);
        }

        if (historyHashMap.containsKey(removableTask.getTaskId())) {
            historyLinkedList.remove(historyHashMap.get(removableTask.getTaskId()));
        }
        historyHashMap.put(removableTask.getTaskId(), historyLinkedList.size() - 1);
    }
}