package taskTracker.manager;

import taskTracker.tasks.Epic;
import taskTracker.tasks.SubTask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.TreeSet;

public class FileBackedTasksManager extends InMemoryTaskManager {

    static String fileName;
    private final HistoryManager taskHistory;

    public FileBackedTasksManager(String fileName) {
        this.taskHistory = Managers.getDefaultHistory();
        this.fileName = fileName;
    }

    @Override
    public void newTask(Task task) {
        super.newTask(task);
        save();
    }

    @Override
    public void newEpic(Epic epic) {
        super.newEpic(epic);
        save();
    }

    @Override
    public void newSubTask(SubTask subTask) {
        super.newSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    public void updateEpic (Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(Long taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(Long taskId) {
        super.deleteEpicById(taskId);
        save();
    }

    @Override
    public void deleteSubTaskById(Long taskId) {
        super.deleteSubTaskById(taskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void showTask(Long taskId) {
        super.showTask(taskId);
        save();
    }

    @Override
    public void showAllTasks() {
        super.showAllTasks();
    }

    @Override
    public List<Task> history() {
        return super.history();
    }

    @Override
    public Task getTaskById(Long taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public LocalDateTime getEndTimeOfTask(Task task) {
        return super.getEndTimeOfTask(task);
    }

    @Override
    public void setEndTimeOfEpic(Epic epic) {
        super.setEndTimeOfEpic(epic);
    }

    @Override
    public void setStartTimeOfEpic(Epic epic) {
        super.setStartTimeOfEpic(epic);
    }

    @Override
    public LocalDateTime getEndTimeOfSubTask(SubTask subTask) {
        return super.getEndTimeOfSubTask(subTask);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public boolean timeCheckOfTask (Task task) {
        return super.timeCheckOfTask(task);
    }

    @Override
    public boolean timeCheckOfSubTask (SubTask task) {
        return super.timeCheckOfSubTask(task);
    }

    // Метод save - автосохранение в файл формата *.CSV
    public void save() {
        // проверка на существование файла
        if (!fileExists(fileName)){
            try {
                File file = new File(fileName);
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println("Ошибка! Невозможно создать файл.");
                return;
            }
        }

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                throw new MyFileNotFoungException("Ошибка! Файл не найден.");
            }
            BufferedWriter bw = new BufferedWriter(
                                new FileWriter(fileName));
            String result = "";
            result = "id,type,name,status,description,startTime,duration,epic\n";
            for (Long key : tasks.keySet()) {
                result = result + toString(tasks.get(key));
            }
            for (Long key : epics.keySet()) {
                result = result + toString(epics.get(key));
            }
            for (Long key : subTasks.keySet()) {
                result = result + toString(subTasks.get(key));
            }

            result = result + "\n";

            List<Task> listOfTasks = taskHistory.getHistory();
            for (int i = 0; i < listOfTasks.size(); i++) {
                result = result + listOfTasks.get(i).getTaskId();
                if (i < (listOfTasks.size() - 1)) {
                    result = result + ",";
                }
            }

            bw.write(result);
            bw.close();
        } catch (MyFileNotFoungException e) {
            System.out.println(e.getMessage());
        }catch (IOException e) {
            System.out.println("Ошибка! Файл не найден.");
        }
    }

    // Метод сохранения задачи (любого типа) в строку
    public String toString(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            return taskToString(task);
        } else if (epics.containsKey(task.getTaskId())) {
            return epicToString(task);
        } else if (subTasks.containsKey(task.getTaskId())) {
            return subTaskToString(task);
        } else {
            System.out.println("Ошибка! Задача неизвестного типа.");
            return "";
        }
    }

    // Метод сохранения задачи (типа Task) в строку
    public String taskToString(Task task) {
        return task.getTaskId() + "," +
                TaskTipe.TASK + "," +
                task.getTaskName() + "," +
                task.getTaskStatus() + "," +
                task.getTaskDescription() + "," +
                task.getStartTime() + "," +
                task.getDuration() + "\n";
    }

    // Метод сохранения задачи (типа Epic) в строку
    public String epicToString(Task task) {
        return task.getTaskId() + "," +
                TaskTipe.EPIC + "," +
                task.getTaskName() + "," +
                task.getTaskStatus() + "," +
                task.getTaskDescription() + "," +
                task.getStartTime() + "," +
                task.getDuration() + "\n";
    }

    // Метод сохранения задачи (типа SubTask) в строку
    public String subTaskToString(Task task) {
        return task.getTaskId() + "," +
                TaskTipe.SUB_TASK + "," +
                task.getTaskName() + "," +
                task.getTaskStatus() + "," +
                task.getTaskDescription() + "," +
                task.getStartTime() + "," +
                task.getDuration() + "," +
                subTasks.get(task.getTaskId()).getNumberOfEpic() + "\n";
    }

    // Метод создания задачи типа Task из строки
    public static Task taskFromString(String value) {
        String[] split = value.split(",");
        return new Task(Long.parseLong(split[0]),
                                       split[2],
                                       split[4],
                    TaskStatus.valueOf(split[3]),
                   LocalDateTime.parse(split[5]),
                        Duration.parse(split[6]));
    }

    // Метод создания задачи типа Epic из строки
    public static Epic epicFromString(String value) {
        String[] split = value.split(",");
        Epic epic = new Epic(split[2], split[4]);
        epic.setTaskId(Long.parseLong(split[0]));
        epic.setTaskStatus(TaskStatus.valueOf(split[3]));
        return epic;
    }

    // Метод создания задачи типа SubTask из строки
    public static SubTask subTaskFromString(String value) {
        String[] split = value.split(",");

        // преобразование типа enum в String
        TaskStatus taskStatus;
        switch (split[3]){
            case "NEW":
                taskStatus = TaskStatus.NEW;
                break;
            case "IN_PROGRESS" :
                taskStatus = TaskStatus.IN_PROGRESS;
                break;
            case "DONE" :
                taskStatus = TaskStatus.DONE;
                break;
            default:
                taskStatus = TaskStatus.NEW;
        }

        SubTask subTask = new SubTask(split[2], split[4], Long.parseLong(split[7]), taskStatus);
        subTask.setTaskId(Long.parseLong(split[0]));
        subTask.setTaskStatus(TaskStatus.valueOf(split[3]));
        subTask.setStartTime(LocalDateTime.parse(split[5]));
        subTask.setDuration(Duration.parse(split[6]));
        return subTask;
    }

    public static void loadFromFile(){
        // проверка на существование файла
        if (!fileExists(fileName)){
            return;
        }

        try {
            BufferedReader br = new BufferedReader (new FileReader(fileName));
            String str;
            // Чтение первой строки (заголовок таблицы)
            str = br.readLine();
            // Чтение строк, содержащих задачи (их содержимое)
            while (true) {
                str = br.readLine();
                // Если достигнута пустая строка или конец файла, то конец чтения файла
                if (str == null) {
                    break;
                }
                if (str.equals("")) {
                    break;
                }

                String[] split = str.split(",");
                if (split[1].equals("TASK")) {
                    tasks.put(taskFromString(str).getTaskId(), taskFromString(str));
                } else if (split[1].equals("EPIC")) {
                    epics.put(epicFromString(str).getTaskId(), epicFromString(str));
                } else if (split[1].equals("SUB_TASK")) {
                    subTasks.put(subTaskFromString(str).getTaskId(), subTaskFromString(str));
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean fileExists (String fileName){
        boolean result = false;
        File file = null;
        try {
            file = new File(fileName);
            result = file.exists();
        } catch (SecurityException exception) {
             // если возникло исключение, значит понадобились права, следовательно файл существует
            result = true;
        } finally {
            if (file != null) {
                file = null;
            }
        }
        return result;
    }

    //=============================== ТЕСТИРОВАНИЕ РАБОТЫ ПРОГРАММЫ ===================================================
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getFileBackedTasksManager();

        loadFromFile();

        System.out.println("\nТЕСТИРОВАНИЕ РАБОТЫ ПРОГРАММЫ\n");

        LocalDateTime ldt;
        Duration dur;

        ldt = LocalDateTime.of(2022, 05, 13, 10, 45);
        dur = Duration.ofHours(1);
        System.out.println("1) Создание задачи № 1 типа Task.");
        taskManager.newTask(new Task("Задача № 1",
                "Описание задачи № 1 типа Task", ldt, dur));

        ldt = LocalDateTime.of(2022, 05, 13, 13, 45);
        dur = Duration.ofHours(1);
        System.out.println("2) Создание задачи № 2 типа Task.");
        taskManager.newTask(new Task("Задача № 2",
                "Описание задачи № 2 типа Task", ldt, dur));

        System.out.println("3) Создание задачи № 3 типа Epic.");
        taskManager.newEpic(new Epic("Задача № 3",
                "Описание задачи № 3 типа Epic"));

        ldt = null;
        dur = Duration.ofHours(1);
        System.out.println("4) Создание задачи № 4 типа SubTask для задачи № 3 типа Epic.");
        taskManager.newSubTask(new SubTask("Задача № 4",
                "Описание задачи № 4 типа SubTask для задачи № 3 типа Epic",
                3L, TaskStatus.NEW, ldt, dur));

        ldt = LocalDateTime.of(2022, 05, 13, 18, 45);
        dur = Duration.ofHours(1);
        System.out.println("5) Создание задачи № 5 типа SubTask для задачи № 3 типа Epic.");
        taskManager.newSubTask(new SubTask("Задача № 5",
                "Описание задачи № 5 типа SubTask для задачи № 3 типа Epic",
                3L, TaskStatus.NEW, ldt, dur));

        ldt = LocalDateTime.of(2022, 05, 13, 17, 45);
        dur = Duration.ofHours(1);
        System.out.println("6) Создание задачи № 6 типа SubTask для задачи № 3 типа Epic.");
        taskManager.newSubTask(new SubTask("Задача № 6",
                "Описание задачи № 4 типа SubTask для задачи № 3 типа Epic",
                3L, TaskStatus.NEW, ldt, dur));

        System.out.println("7) Создание задачи № 7 типа Epic.");
        taskManager.newEpic(new Epic("Задача № 7",
                "Описание задачи № 7 типа Epic"));

        System.out.println("\n8) Вывод на экран всех созданных задач:\n");
        taskManager.showAllTasks();

        System.out.println("9) Вывод на экран значений полей задачи № 3:\n");
        taskManager.showTask(3L);

        System.out.println("\n10) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n11) Вывод на экран значений полей задачи № 1:");
        taskManager.showTask(1L);

        System.out.println("\n12) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n13) Вывод на экран значений полей задачи № 6:");
        taskManager.showTask(6L);

        System.out.println("\n14) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n15) Вывод на экран значений полей задачи № 2:");
        taskManager.showTask(2L);

        System.out.println("\n16) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n17) Вывод на экран значений полей задачи № 2:");
        taskManager.showTask(2L);

        System.out.println("\n18) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n19) Вывод на экран значений полей задачи № 1:");
        taskManager.showTask(1L);

        System.out.println("\n20) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        //System.out.println("\n21) Удаление задачи № 2.");
        //taskManager.deleteTaskById(2L);

        System.out.println("\n22) Вывод на экран всех задач:");
        taskManager.showAllTasks();

        //System.out.println("\n23) Удаление задачи № 3.");
        //taskManager.deleteEpicById(3L);

        System.out.println("\n24) Вывод на экран всех задач:");
        taskManager.showAllTasks();

        System.out.println("\n25) Вывод на экран истории вызова задач:\n");
        System.out.println(taskManager.history());

        System.out.println("\n26) Вывод на экран упорядоченного по времени (поле startTime) списка задач:\n");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("\nТЕСТИРОВАНИЕ ЗАВЕРШЕНО");
    }
}