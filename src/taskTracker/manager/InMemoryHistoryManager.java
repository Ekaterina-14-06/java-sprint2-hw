package taskTracker.manager;

import taskTracker.tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    // ----------------------------------------------------------------------------------------------------------------
    // Объявление локальных констант
    static final int MAX_COUNT_OF_TASKS_IN_HISTORY = 5;

    // ----------------------------------------------------------------------------------------------------------------
    // Объявление локальных переменных

    // Таблица historyHashMap необходима для быстрого поиска соответствующей задачи (узла) в упорядоченном списке historyLinkedList.
    // Её поля: Long - это taskID задачи, а Integer - это номер индекса (место в списке) узла списка historyLinkedList
    static Map<Long, Node> historyHashMap = new HashMap<>();

    // listHead указывает на первый элемент (узел) двунаправленного связанного списка
    static Node listHead = new Node(null, null, null);

    // listTail указывает на последний элемент (узел) двунаправленного связанного списка
    static Node listTail = new Node(null, null, null);

    // listSize количество элементов (узлов) двунаправленного связанного списка
    static int listSize = 0;

    // ----------------------------------------------------------------------------------------------------------------
    // Описание методов данного класса

    // Метод add добавляет просмотренную задачу в конец двунаправленного связанного списка historyLinkedList
    @Override
    public void add(Task task){
        Node node = new Node(null, null, task);
        if (listSize == 0) {
            listHead.setNext(node);
            listTail.setPrevious(node);
            // Увеличение значения размера двунаправленного связанного списка
            ++listSize;
        } else {
            // Добавление нового узла в конец historyLinkedList
            linkLast(node);
            // Проверка на повтор узла (наличие дублирования задач в истории задач)
            if (historyHashMap.containsKey(task.getTaskId())) {
                // Удаление узла из historyLinkedList
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
    // Метод remove удаляет узел из historyLinkedList
    @Override
    public void remove(Long id) {
        Node previous = historyHashMap.get(id).getPrevious();
        Node next = historyHashMap.get(id).getNext();
        if (previous != null) {
            previous.setNext(next);
        } else {
            listHead.setNext(next);
        }
        next.setPrevious(previous);
    }

    // --------------------------------------------------------
    // Метод history выводит на экран историю просмотров
    @Override
    public void history(){
        // Можно предусмотреть выбор отображения истории задач:
        // "от первой к последней" и "от последней к первой"

        System.out.println("История вызова задач (последние " + MAX_COUNT_OF_TASKS_IN_HISTORY + " шт.):");
        //List<Task> history = new ArrayList<>();
        //history = getHistory();
        List<Task> history = getHistory();
        if (history.size() != 0) {
            for (Task task : history) {
                System.out.println(task.getTaskName());
            }
        } else {
            System.out.println("Ни с одной задачей ещё не работали.");
        }
    }

    // --------------------------------------------------------
    // Метод getHistory возвращает упорядоченный список задач без повторов (историю)
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
    // Метод linkLast добавляет узел в конец historyLinkedList
    public void linkLast(Node node) {
        // Добавление нового узла в конец historyLinkedList
        node.setPrevious(listTail.getPrevious());
        listTail.getPrevious().setNext(node);
        listTail.setPrevious(node);
    }
}