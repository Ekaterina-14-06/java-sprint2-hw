package taskTracker.manager;

import taskTracker.tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТАНТ ДАННОГО КЛАССА

    static final int MAX_COUNT_OF_TASKS_IN_HISTORY = 10;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА
    @Override
    public Map<Long, Node> getHistoryHashMap() {
        return historyHashMap;
    }

    // Таблица historyHashMap необходима для быстрого поиска соответствующей задачи - узла двунаправленного связанного списка.
    // Её поля: Long - это taskID задачи, а Node - это ссылка на узел двунаправленного связанного списка.
    static Map<Long, Node> historyHashMap = new HashMap<>();
    @Override
    public void setListHead(Node listHead) {
        this.listHead = listHead;
    }

    // listHead указывает на первый узел двунаправленного связанного списка
    static Node listHead = new Node(null, null, null);

    // listTail указывает на последний узел двунаправленного связанного списка
    static Node listTail = new Node(null, null, null);

    // listSize - количество узлов двунаправленного связанного списка
    static int listSize = 0;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    // Метод add добавляет просмотренную задачу в конец двунаправленного связанного списка
    @Override
    public void add(Task task){
        Node node = new Node(null, null, task);
        if (listSize == 0) {
            listHead.setNext(node);
            listTail.setPrevious(node);
            ++listSize;
        } else {
            // Добавление нового узла в конец двунаправленного связанного списка
            linkLast(node);
            // Проверка на повтор узла (наличие дублирования задач в истории задач)
            if (historyHashMap.containsKey(task.getTaskId())) {
                // Удаление узла из двунаправленного связанного списка
                remove(task.getTaskId());
            } else {
                // Увеличение значения размера двунаправленного связанного списка
                ++listSize;
            }
        }
        // Добавление или актуализация значения в historyHashMap (ссылки на новый добавленный узел)
        historyHashMap.put(node.getTask().getTaskId(), node);
        // Проверка размера истории задач на превышение максимального значения
        if (listSize > MAX_COUNT_OF_TASKS_IN_HISTORY) {
            // Удаление первого элемента двунаправленного связанного списка
            // и соответствующей записи в historyHashMap
            historyHashMap.remove(listHead.getNext().getTask().getTaskId());
            listHead.setNext(listHead.getNext().getNext());
            listHead.getNext().setPrevious(null);
            --listSize;
        }
    }

    // --------------------------------------------------------
    // Метод remove удаляет узел из двунаправленного связанного списка
    @Override
    public void remove(Long id) {
        Node previous = historyHashMap.get(id).getPrevious();
        Node next = historyHashMap.get(id).getNext();

        if (next == null) {
            listTail.setPrevious(previous);
        }

        if (previous != null) {
            previous.setNext(next);
        } else {
            listHead.setNext(next);
        }
        next.setPrevious(previous);
    }

    // --------------------------------------------------------
    // Метод getHistory возвращает упорядоченный список задач без повторов (историю вызова задач)
    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node node = listHead.getNext();
        int index = 1;
        while (index <= listSize) {
            history.add(node.getTask());
            node = node.getNext();
            ++index;
        }
        return history;
    }

    // --------------------------------------------------------
    // Метод linkLast добавляет новый узел в конец двунаправленного связанного списка
    public void linkLast(Node node) {
        node.setPrevious(listTail.getPrevious());
        listTail.getPrevious().setNext(node);
        listTail.setPrevious(node);
    }

    @Override
    public void clearHistory() {
        for (Long key : historyHashMap.keySet()) {
            remove(key);
        }
        historyHashMap.clear();
        listHead = new Node(null, null, null);
        listTail = new Node(null, null, null);
    }
}