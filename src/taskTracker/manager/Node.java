package taskTracker.manager;

import taskTracker.tasks.*;

public class Node {
    // ----------------------------------------------------------------------------------------------------------------
    // Объявление локальных переменных
    private Node previous;
    private Node next;
    private Task task;

    // ----------------------------------------------------------------------------------------------------------------
    // Объявление конструктора данного класса
    public Node(Node previous, Node next, Task task) {
        this.previous = previous;
        this.next = next;
        this.task = task;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Описание методов данного класса

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
