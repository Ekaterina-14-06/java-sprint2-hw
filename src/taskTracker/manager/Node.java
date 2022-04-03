package taskTracker.manager;

import taskTracker.tasks.*;

// Node - узел двунаправленного связанного списка
public class Node {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА
    private Node previous;
    private Node next;
    private Task task;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРОВ ДАННОГО КЛАССА
    public Node(Node previous, Node next, Task task) {
        this.previous = previous;
        this.next = next;
        this.task = task;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    // --------------------------------------------------------
    public Task getTask() {
        return task;
    }

    // --------------------------------------------------------
    public Node getPrevious() {
        return previous;
    }

    // --------------------------------------------------------
    public Node getNext() {
        return next;
    }

    // --------------------------------------------------------
    public void setTask(Task task) {
        this.task = task;
    }

    // --------------------------------------------------------
    public void setPrevious(Node node) {
        this.previous = node;
    }

    // --------------------------------------------------------
    public void setNext(Node node) {
        this.next = node;
    }
}
