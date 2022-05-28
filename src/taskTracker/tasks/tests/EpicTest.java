import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import taskTracker.manager.InMemoryTaskManager;
import taskTracker.manager.MyException;
import taskTracker.manager.TaskTipe;
import taskTracker.tasks.Epic;
import taskTracker.tasks.SubTask;
import taskTracker.tasks.TaskStatus;
import java.time.LocalDateTime;
import java.time.Duration;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {
    InMemoryTaskManager test = new InMemoryTaskManager();

    @AfterEach
    void afterEach() {
        try {
            test.deleteAllTasks();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    // 1a) Расчёт статуса Epic. Граничное условие: пустой список подзадач.
    @Test
    void shouldUpdateTaskStatusOfEpicNoSubtask() {
        //InMemoryTaskManager test = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик № 1", "Описание Эпик № 1: без подзадач");
        try {
            test.newEpic(epic);
        } catch (MyException e) {
            e.printStackTrace();
        }
        test.updateTaskStatusOfEpic(epic);
        assertEquals("NEW", epic.getTaskStatus().toString());

    }

    //  1b) Расчёт статуса Epic. Граничное условие: все подзадачи со статусом NEW.
    @Test
    void shouldUpdateTaskStatusOfEpicAllSubtasksNew() {
        //InMemoryTaskManager test = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик № 2", "Описание Эпик № 2: все подзадачи со статусом NEW");
        try {
            test.newEpic(epic);
        } catch (MyException e) {
            e.printStackTrace();
        }

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Описание подзадачи № 1: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        try {
            test.newSubTask(subTask1);
        } catch (MyException e) {
            e.printStackTrace();
        }

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Описание подзадачи № 2: Сабтаск для Эпик № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        try {
            test.newSubTask(subTask2);
        } catch (MyException e) {
            e.printStackTrace();
        }

        test.updateTaskStatusOfEpic(epic);
        assertEquals("NEW", epic.getTaskStatus().toString());
    }

    // 1c) Расчёт статуса Epic. Граничное условие: все подзадачи со статусом DONE.
    @Test
    void shouldUpdateTaskStatusOfEpicAllSubtasksDone() {
        //InMemoryTaskManager test = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик № 3", "Описание Эпик № 3: все подзадачи со статусом DONE");
        try {
            test.newEpic(epic);
        } catch (MyException e) {
            e.printStackTrace();
        }

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Описание подзадачи № 1: Сабтаск для Эпик № 3",
                epic.getTaskId(), TaskStatus.DONE, ldt, dur);
        try {
            test.newSubTask(subTask1);
        } catch (MyException e) {
            e.printStackTrace();
        }

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Описание подзадачи № 2: Сабтаск для Эпик № 3",
                epic.getTaskId(), TaskStatus.DONE, ldt, dur);
        try {
            test.newSubTask(subTask2);
        } catch (MyException e) {
            e.printStackTrace();
        }

        test.updateTaskStatusOfEpic(epic);
        assertEquals("DONE", epic.getTaskStatus().toString());
    }

    // 1d) Расчёт статуса Epic. Граничное условие: подзадачи со статусами NEW и DONE.
    @Test
    void shouldUpdateTaskStatusOfEpicSubtasksNewDone() {
        //InMemoryTaskManager test = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик № 4", "Описание Эпик № 4: подзадачи со статусами NEW и DONE");
        try {
            test.newEpic(epic);
        } catch (MyException e) {
            e.printStackTrace();
        }

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Описание подзадачи № 1: Сабтаск для Эпик № 4",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        try {
            test.newSubTask(subTask1);
        } catch (MyException e) {
            e.printStackTrace();
        }

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Описание подзадачи № 2: Сабтаск для Эпик № 4",
                epic.getTaskId(), TaskStatus.DONE, ldt, dur);
        try {
            test.newSubTask(subTask2);
        } catch (MyException e) {
            e.printStackTrace();
        }

        test.updateTaskStatusOfEpic(epic);
        assertEquals("IN_PROGRESS", epic.getTaskStatus().toString());
    }

    // 1e) Расчёт статуса Epic. Граничное условие: подзадачи со статусом IN_PROGRESS.
    @Test
    void shouldUpdateTaskStatusOfEpicSubtasksInProgress() {
        //InMemoryTaskManager test = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик № 5", "Описание Эпик № 5: все подзадачи со статусом IN_PROGRESS");
        try {
            test.newEpic(epic);
        } catch (MyException e) {
            e.printStackTrace();
        }

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Описание подзадачи № 1: Сабтаск для Эпик № 5",
                epic.getTaskId(), TaskStatus.IN_PROGRESS, ldt, dur);
        try {
            test.newSubTask(subTask1);
        } catch (MyException e) {
            e.printStackTrace();
        }

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Описание подзадачи № 2: Сабтаск для Эпик № 5",
                epic.getTaskId(), TaskStatus.IN_PROGRESS, ldt, dur);
        try {
            test.newSubTask(subTask2);
        } catch (MyException e) {
            e.printStackTrace();
        }

        test.updateTaskStatusOfEpic(epic);
        assertEquals("IN_PROGRESS", epic.getTaskStatus().toString());
    }
}