package taskTracker.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import taskTracker.tasks.Epic;
import taskTracker.tasks.SubTask;
import taskTracker.tasks.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest {

    static String fileName = "task_manager.csv";

    TaskManager test1 = new FileBackedTasksManager(fileName);

    FileBackedTasksManager test = (FileBackedTasksManager) test1;

    @AfterEach
    void afterEach() {
        test.deleteAllTasks();
    }

    @Test
    void save() {
        Epic epic = new Epic("Эпик № 1", "Описание Эпик № 1: все подзадачи со статусом NEW");
        test.newEpic(epic);

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Описание подзадачи № 1: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask1);

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Описание подзадачи № 2: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask2);
        try {
            test.save();
        } catch (MyException e) {
            e.printStackTrace();
        }

        assertTrue(test.fileExists(fileName));
    }

    @Test
    void loadFromFile() {
        Epic epic = new Epic("Эпик № 1", "Описание Эпик № 1: все подзадачи со статусом NEW");
        test.newEpic(epic);

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Описание подзадачи № 1: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask1);

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Описание подзадачи № 2: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask2);
        try {
            test.save();
        } catch (MyException e) {
            e.printStackTrace();
        }
        assertNotEquals(0, test.getPrioritizedTasks());
    }

    @Test
    void fileExists() {
        Epic epic = new Epic("Эпик № 1", "Описание Эпик № 1: все подзадачи со статусом NEW");
        test.newEpic(epic);

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Описание подзадачи № 1: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask1);

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Описание подзадачи № 2: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask2);
        try {
            test.save();
        } catch (MyException e) {
            e.printStackTrace();
        }

        assertTrue(test.fileExists(fileName));
            }
}