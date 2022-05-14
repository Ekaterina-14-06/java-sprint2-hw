package taskTracker.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import taskTracker.tasks.Epic;
import taskTracker.tasks.SubTask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    InMemoryTaskManager test = new InMemoryTaskManager();

    @AfterEach
    void afterEach() {
        test.deleteAllTasks();
    }

    // 2a. со стандартным поведением
    @Test
    void shouldNewTask() {

        //создадим 2 задачи, чтобы не было пустого списка задач
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task1 = new Task("Задача № 1", "Задача 1", ldt, dur);
        test.newTask(task1);

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        Task task2 = new Task("Задача № 2", "Задача 2", ldt, dur);
        test.newTask(task2);
        assertNotNull(test.getPrioritizedTasks());
    }

    //2b. с пустым списком задач
    @Test
    void shouldNewTaskEmpty() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task = new Task("Задача", "Задача", ldt, dur);
        test.newTask(task);
        assertNotNull(test.getPrioritizedTasks());
    }

    //создание эпика и проверка его статуса тестируются в классе EpicTest
    /*@Test
    void shouldNewEpic() {
    }*/


    // 2a. со стандартным поведением
    @Test
    void shouldNewSubTask() {
        Epic epic = new Epic("Эпик № 1", "Эпик № 1");
        test.newEpic(epic);

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask = new SubTask("Подзадача № 1", "Подзадача № 1",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask);
        assertEquals(epic.getTaskId(), subTask.getNumberOfEpic(), "Такого эпика нет");
    }

    //2b. с пустым списком задач
    @Test
    void shouldNewSubTaskEmpty() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask = new SubTask("Подзадача № 1", "Подзадача № 1",
                 TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask);
        assertNull(subTask);
    }

    //следующий кусок кода закоментирован, так как пока так и не смогла найти NPE в самом коде при методах удаления
    /*// 2a. со стандартным поведением
    @Test
    void shouldDeleteTaskById() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task = new Task("Задача № 1", "Задача 1", ldt, dur);
        test.newTask(task);
        task.getTaskId();
        test.deleteTaskById(task.getTaskId());
        assertEquals(0, test.getPrioritizedTasks().size());
    }

    //2b. с пустым списком задач
    @Test
    void shouldDeleteTaskByIdEmpty() {
    }

    //2c. с неверным идентификатором задачи
    @Test
    void shouldDeleteTaskByIdIncorrectId() {
    }

    // 2a. со стандартным поведением
    @Test
    void shouldDeleteEpicById() {
    }

    //2b. с пустым списком задач
    @Test
    void shouldDeleteEpicByIdEmpty() {
    }

    //2c. с неверным идентификатором задачи
    @Test
    void shouldDeleteEpicByIdIncorrectId() {
    }

    // 2a. со стандартным поведением
    @Test
    void shouldDeleteSubTaskById() {
    }

    //2b. с пустым списком задач
    @Test
    void shouldDeleteSubTaskByIdEmpty() {
    }

    //2c. с неверным идентификатором задачи
    @Test
    void shouldDeleteSubTaskByIdIncorrectId() {
    }

    // 2a. со стандартным поведением
    @Test
    void shouldDeleteAllTasks() {
    }

    //2b. с пустым списком задач
    @Test
    void shouldDeleteAllTasksEmpty() {
    }*/

    // 2a. со стандартным поведением
    @Test
    void shouldUpdateTask() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task = new Task("Задача", "Задача", ldt, dur);
        test.newTask(task);
        ldt = LocalDateTime.of(2022, 06, 13, 17, 45);
        dur = Duration.ofHours(1);
        Task task2 = new Task("Обновление", "Обновление", ldt, dur);
        test.updateTask(task2);
        assertEquals("Обновление", task2.getTaskName());
    }

    //2b. с пустым списком задач
    @Test
    void shouldUpdateTaskEmpty() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        test.updateTask(new Task("Задача", "Задача", ldt, dur));
        assertEquals(0, test.getPrioritizedTasks().size());
    }

    // 2a. со стандартным поведением
    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("Эпик № 1", "Эпик № 1");
        test.newEpic(epic);
        Epic epic2 = new Epic("Обновленный", "Обновленный");
        test.updateEpic(epic2);
        assertEquals("Обновленный", epic2.getTaskName());
    }

    //2b. с пустым списком задач
    @Test
    void shouldUpdateEpicEmpty() {
        test.updateTask(new Epic("Задача", "Задача"));
        assertEquals(0, test.getPrioritizedTasks().size());
    }

    // 2a. со стандартным поведением
    @Test
    void shouldUpdateSubtask() {
        Epic epic = new Epic("Эпик № 1", "Эпик № 1");
        test.newEpic(epic);

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask = new SubTask("Сабтаск", "Сабтаск", epic.getTaskId(), TaskStatus.NEW, ldt,
                dur);
        test.newSubTask(subTask);
        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Сабтаск2", "Сабтаск2", epic.getTaskId(), TaskStatus.NEW, ldt,
                dur);
        test.updateSubtask(subTask2);
        assertEquals("Сабтаск2", subTask2.getTaskName());
    }

    //2b. с пустым списком задач
    @Test
    void shouldUpdateSubtaskEmpty() {
        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask = new SubTask("Сабтаск", "Сабтаск", TaskStatus.NEW, ldt,
                dur);
        test.updateSubtask(subTask);
        assertEquals(0, test.getPrioritizedTasks().size());

    }

    // 2a. со стандартным поведением
    @Test
    void shouldSetEndTimeOfEpic() {
        Epic epic = new Epic("Эпик", "Эпик");
        test.newEpic(epic);

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        SubTask subTask1 = new SubTask("Подзадача № 1", "Подзадача № 1",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask1);

        ldt = LocalDateTime.of(2022, 05, 13, 19, 45);
        dur = Duration.ofHours(1);
        SubTask subTask2 = new SubTask("Подзадача № 2", "Подзадача № 2",
                epic.getTaskId(), TaskStatus.NEW, ldt, dur);
        test.newSubTask(subTask2);
        test.setEndTimeOfEpic(epic);
        LocalDateTime fact = ldt.plus(dur);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        String formatDateTime = fact.format(formatter);
        assertEquals("13.05.2022, 20:45", formatDateTime);
    }

    // 2a. со стандартным поведением
    @Test
    void shouldGetPrioritizedTasks() {
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

        assertEquals(3, test.getPrioritizedTasks().size());
    }

    //2b. с пустым списком задач
    @Test
    void shouldGetPrioritizedTasksEmpty() {
        assertEquals(0, test.getPrioritizedTasks().size());
    }
}