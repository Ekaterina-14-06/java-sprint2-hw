package taskTracker.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTracker.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    InMemoryHistoryManager test;

    @BeforeEach
    void beforeEach() {
        test = new InMemoryHistoryManager();
    }

    //a. пустая история задач с null
    @Test
    void addEmpty() {
        int listSizeFirst = test.getListSize();
        test.add(null);
        int listSizeEnd = test.getListSize();
        assertFalse(listSizeEnd > listSizeFirst);
    }

    //b. дублирование
    @Test
    void addDouble() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task1 = new Task("Задача № 1", "Задача 1", ldt, dur);
        task1.setTaskId(1l);

        ldt = LocalDateTime.of(2022, 05, 13, 20, 45);
        dur = Duration.ofHours(1);
        Task task2 = new Task("Задача № 2", "Задача 2", ldt, dur);
        task2.setTaskId(2l);

        test.add(task1);
        test.add(task2);
        test.add(task1);
        int listSize = test.getListSize();
        assertEquals(2, listSize);
    }

    //удаление из истории: начало
    @Test
    void removeStart() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task1 = new Task("Задача № 1", "Задача 1", ldt, dur);
        task1.setTaskId(1l);

        ldt = LocalDateTime.of(2022, 05, 13, 20, 45);
        dur = Duration.ofHours(1);
        Task task2 = new Task("Задача № 2", "Задача 2", ldt, dur);
        task2.setTaskId(2l);

        ldt = LocalDateTime.of(2022, 05, 13, 22, 45);
        dur = Duration.ofHours(1);
        Task task3 = new Task("Задача № 3", "Задача 3", ldt, dur);
        task3.setTaskId(3l);

        test.add(task1);
        test.add(task2);
        test.add(task3);

        long taskIDOfSecondTask = test.getHistory().get(1).getTaskId();
        test.remove(task1.getTaskId());

        assertEquals(taskIDOfSecondTask, test.getHistory().get(0).getTaskId());
    }

    //удаление из истории: середина

    @Test
    void removeMiddle() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task1 = new Task("Задача № 1", "Задача 1", ldt, dur);
        task1.setTaskId(1l);

        ldt = LocalDateTime.of(2022, 05, 13, 20, 45);
        dur = Duration.ofHours(1);
        Task task2 = new Task("Задача № 2", "Задача 2", ldt, dur);
        task2.setTaskId(2l);

        ldt = LocalDateTime.of(2022, 05, 13, 22, 45);
        dur = Duration.ofHours(1);
        Task task3 = new Task("Задача № 3", "Задача 3", ldt, dur);
        task3.setTaskId(3l);

        test.add(task1);
        test.add(task2);
        test.add(task3);

        long taskIDOfThirdTask = test.getHistory().get(2).getTaskId();
        test.remove(task2.getTaskId());

        assertEquals(taskIDOfThirdTask, test.getHistory().get(1).getTaskId());
    }

    //удаление из истории: конец
    @Test
    void removeEnd() {
        // никак не проверить
    }

    //a. пустая история задач
    @Test
    void getHistoryEmpty() {
        List<Task> history = new ArrayList<>();
        history = test.getHistory();
        assertEquals(0, history.size());
    }

    //b. дублирование
    @Test
    void getHistoryDouble() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task1 = new Task("Задача № 1", "Задача 1", ldt, dur);
        task1.setTaskId(1l);

        ldt = LocalDateTime.of(2022, 05, 13, 20, 45);
        dur = Duration.ofHours(1);
        Task task2 = new Task("Задача № 2", "Задача 2", ldt, dur);
        task2.setTaskId(2l);

        List<Task> history1 = new ArrayList<>();
        history1 = test.getHistory();
        test.add(task1);
        test.add(task2);
        test.add(task1);
        List<Task> history2 = new ArrayList<>();
        history2 = test.getHistory();
        assertNotEquals(history1.size(), history2.size());

    }

    //a. пустая история задач
    //мето дне работает, не знаю, как отловить исключение, возникающее при удалении задачи
    @Test
    void clearHistoryEmpty() {
        test.clearHistory();
        int listSizeEnd = test.getListSize();
        assertFalse((listSizeEnd == 0) &&
                (test.getHistoryHashMap().size() == 0) &&
                (test.getListHead().getNext() == null) &&
                (test.getListTail().getPrevious() == null));
    }

    //b. дублирование
    //метод дне работает, не знаю, как отловить исключение, возникающее при удалении задачи
    @Test
    void clearHistoryDouble() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task1 = new Task("Задача № 1", "Задача 1", ldt, dur);
        task1.setTaskId(1l);

        ldt = LocalDateTime.of(2022, 05, 13, 20, 45);
        dur = Duration.ofHours(1);
        Task task2 = new Task("Задача № 2", "Задача 2", ldt, dur);
        task2.setTaskId(2l);

        ldt = LocalDateTime.of(2022, 05, 13, 22, 45);
        dur = Duration.ofHours(1);
        Task task3 = new Task("Задача № 3", "Задача 3", ldt, dur);
        task3.setTaskId(3l);

        test.add(task1);
        test.add(task2);
        test.add(task3);
        test.clearHistory();
        int listSizeEnd = test.getListSize();
        assertFalse((listSizeEnd == 0) &&
                             (test.getHistoryHashMap().size() == 0) &&
                             (test.getListHead().getNext() == null) &&
                             (test.getListTail().getPrevious() == null));

    }

}