package taskTracker.manager;

import taskTracker.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    // mapOfTasks - список задач типа Task
    protected final HashMap<Long, Task> tasks = new HashMap<>();

    // mapOfEpics - список задач типа Epic
    protected final HashMap<Long, Epic> epics = new HashMap<>();

    // mapOfSubTasks - список задач типа SubTask
    protected final HashMap<Long, SubTask> subTasks = new HashMap<>();

    // comparator - для сравнения двух задач по полю startTime (для сортировки набора prioritizedTasks типа TreeSet)
    protected Comparator<? extends Task> comparator = new Comparator<>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    };

    // Набор prioritizedTasks типа TreeSet для хранения отсортированного по полю startTime (т.е. по приоритету) списка всех задач
    protected final Set<Task> prioritizedTasks = new TreeSet<>((Comparator<Task>) comparator);

    // countOfTasks содержит номер последней созданной задачи
    protected long countOfTasks = 0;

    // listOfFreeNumber - список, содержащий освободившиеся номера задач после их удаления
    protected ArrayList<Long> listOfFreeNumber = new ArrayList<>();

    protected final HistoryManager taskHistory;

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ КОНСТРУКТОРОВ ДАННОГО КЛАССА

    public InMemoryTaskManager() {
        this.taskHistory = Managers.getDefaultHistory();
    }

    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ МЕТОДОВ ДАННОГО КЛАССА

    // Метод newTask создаёт новую задачу типа Task
    @Override
    public void newTask(Task task) throws MyException {
        if (listOfFreeNumber.isEmpty()) {
            task.setTaskId(++countOfTasks);
        } else {
            task.setTaskId(listOfFreeNumber.get(0));
            listOfFreeNumber.remove(0);
        }

        // Проверка: Если задача является первой и у неё не задано время StartTime, то:
        if (getPrioritizedTasks().size() == 0 && task.getStartTime() == null) {
            System.out.println("Ошибка! Первая задача не может не содержать время старта.\n" +
                    "Задача не была добавлена.");
            return;
        }

        // Если время startTime у задачи не задано, то она ставится в конец.
        if (task.getStartTime() == null) {
            task.setStartTime(((TreeSet<Task>)getPrioritizedTasks()).last().getStartTime().plus(
                    ((TreeSet<Task>)getPrioritizedTasks()).last().getDuration()));
        } else {

            // Проверка на наличие подходящего свободного временного промежутка между задачами
            if (!timeCheckOfTask(task)) {
                System.out.println("Ошибка! Невозможно создать задачу с данными значениями полей startTime и duration\n" +
                        "(недопустимо пересечение задач по времени их выполнения).\n" +
                        "Задача не была добавлена.");
                return;
            }
        }
        tasks.put(task.getTaskId(), task);
        prioritizedTasks.add(task);
    }

    // Метод newEpic создаёт новую задачу типа Epic
    @Override
    public void newEpic(Epic epic) throws MyException {
        if (listOfFreeNumber.isEmpty()) {
            epic.setTaskId(++countOfTasks);
        } else {
            epic.setTaskId(listOfFreeNumber.get(0));
            listOfFreeNumber.remove(0);
        }

        if (getPrioritizedTasks().size() == 0) {
            epic.setStartTime(LocalDateTime.now());
            epic.setDuration(Duration.ofSeconds(1));
        } else {
            epic.setStartTime(((TreeSet<Task>)getPrioritizedTasks()).last().getStartTime().plus(
                    ((TreeSet<Task>)getPrioritizedTasks()).last().getDuration()));
            epic.setDuration(Duration.ofSeconds(1));
        }
        epics.put(epic.getTaskId(), epic);
        prioritizedTasks.add(epic);
        updateTaskStatusOfEpic(epics.get(epic.getTaskId()));
    }

    // Метод newSubTask создаёт новую задачу типа SubTask
    @Override
    public void newSubTask(SubTask subTask) throws MyException {
        Epic epic = epics.get(subTask.getNumberOfEpic());
        if (epic != null) {
            if (listOfFreeNumber.isEmpty()) {
                subTask.setTaskId(++countOfTasks);
            } else {
                subTask.setTaskId(listOfFreeNumber.get(0));
                listOfFreeNumber.remove(0);
            }

            // Проверка: Если задача является первой и у неё не задано время StartTime, то:
            if (getPrioritizedTasks().size() == 0 && subTask.getStartTime() == null) {
                System.out.println("Ошибка! Первая задача не может не содержать время старта.\n" +
                        "Задача не была добавлена.");
                return;
            }

            // Если время startTime у задачи не задано, то она ставится в конец.
            if (subTask.getStartTime() == null) {
                subTask.setStartTime(((TreeSet<Task>)getPrioritizedTasks()).last().getStartTime().plus(
                        ((TreeSet<Task>)getPrioritizedTasks()).last().getDuration()));
            } else {
                // Проверка на наличие подходящего свободного временного промежутка между задачами
                if (!timeCheckOfSubTask(subTask)) {
                    System.out.println("Ошибка! Невозможно создать задачу с данными значениями полей startTime и duration\n" +
                            "(недопустимо пересечение задач по времени их выполнения).\n" +
                            "Задача не была добавлена.");
                    return;
                }
            }

            subTasks.put(subTask.getTaskId(), subTask);
            epic.getMapOfSubTasks().put(subTask.getTaskId(), subTask);
            updateTaskStatusOfEpic(epic);
            prioritizedTasks.add(subTask);

            // Актуализация значения поля EndTime у соответствующего Epic
            setEndTimeOfEpic(epic);
            setStartTimeOfEpic(epic);
        } else {
            System.out.println("Ошибка. Не существует эпик, для которого создается сабтаск. Сабтаск не создан");
        }
    }

    @Override
    public Task getTaskById(Long taskId) {
        Task task = tasks.get(taskId);
        if(task!=null){
            taskHistory.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById (Long taskId) {
        Epic epic = epics.get(taskId);
        if(epic!=null){
            taskHistory.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById (Long taskId) {
        SubTask subTask = subTasks.get(taskId);
        if(subTask!=null){
            taskHistory.add(subTask);
        }
        return subTask;
    }

    @Override
    public void updateTask(Task task) throws MyException {
        Task updatableTask = getTaskById(task.getTaskId());
        // Проверка на существование задачи с заданным TaskId
        if (updatableTask == null) {
            System.out.println("Такой задачи нет.");
            return;
        }

        if (!(updatableTask.getStartTime().equals(task.getStartTime()) &&
            updatableTask.getDuration().equals(task.getDuration()))) {
            // Проверка на наличие подходящего свободного временного промежутка между задачами
            if (!timeCheckOfTask(task)) {
                System.out.println("Ошибка! Невозможно создать задачу с данными значениями полей startTime и duration\n" +
                        "(недопустимо пересечение задач по времени их выполнения).\n" +
                        "Задача не была добавлена.");
                return;
            }
        }

        updatableTask.setTaskName(task.getTaskName());
        updatableTask.setTaskDescription(task.getTaskDescription());
        updatableTask.setTaskStatus(task.getTaskStatus());
        updatableTask.setStartTime(task.getStartTime());
        updatableTask.setDuration(task.getDuration());
        taskHistory.add(updatableTask);
    }

    public void updateEpic (Epic epic) throws MyException {
        Epic updatableEpic = getEpicById(epic.getTaskId());
        if (updatableEpic == null) {
            System.out.println("Такой задачи нет.");
            return;
        }

        updatableEpic.setTaskName(epic.getTaskName());
        updatableEpic.setTaskDescription(epic.getTaskDescription());

        if (updatableEpic.getTaskStatus() != epic.getTaskStatus()) {
            System.out.println("Задачи типа Epic самостоятельно менять статус не могут.\n" +
                    "Их состояние меняется автоматически при смене статусов её подзадач (SubTask).");
        }

        taskHistory.add(updatableEpic);
    }

    public void updateSubtask(SubTask subTask) throws MyException {
        SubTask updatableSubTask = getSubTaskById(subTask.getTaskId());
        // Проверка на существование задачи с заданным TaskId
        if (updatableSubTask == null) {
            System.out.println("Такой задачи нет.");
            return;
        }

        if (!(updatableSubTask.getStartTime().equals(subTask.getStartTime()) &&
                updatableSubTask.getDuration().equals(subTask.getDuration()))) {
            // Проверка на наличие подходящего свободного временного промежутка между задачами
            if (!timeCheckOfSubTask(subTask)) {
                System.out.println("Ошибка! Невозможно создать задачу с данными значениями полей startTime и duration\n" +
                        "(недопустимо пересечение задач по времени их выполнения).\n" +
                        "Задача не была добавлена.");
                return;
            }
        }

        updatableSubTask.setTaskName(subTask.getTaskName());
        updatableSubTask.setTaskDescription(subTask.getTaskDescription());
        updatableSubTask.setTaskStatus(subTask.getTaskStatus());
        updateTaskStatusOfEpic(epics.get(updatableSubTask.getNumberOfEpic()));

        // Актуализация значения поля EndTime у соответствующего Epic
        setEndTimeOfEpic(epics.get(updatableSubTask.getNumberOfEpic()));
        setStartTimeOfEpic(epics.get(updatableSubTask.getNumberOfEpic()));

        //taskHistory.add(updatableSubTask);
    }

    // Метод deleteTaskById удаляет задачу типа Task
    @Override
    public void deleteTaskById(Long taskId) throws MyException {
        taskHistory.remove(taskId);
        taskHistory.getHistoryHashMap().remove(taskId);

        getPrioritizedTasks().remove(tasks.get(taskId));
        tasks.remove(taskId);
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteEpicById удаляет задачу типа Epic
    @Override
    public void deleteEpicById(Long taskId) throws MyException {
        // taskHistory.add(getEpicById(taskId));
        taskHistory.remove(taskId);
        taskHistory.getHistoryHashMap().remove(taskId);

        // Удаление подзадач типа SubTask, относящихся к выбранной задаче типа Epic
        for (Long key : getEpicById(taskId).getMapOfSubTasks().keySet()) {
            subTasks.remove(key);
        }
        getEpicById(taskId).getMapOfSubTasks().clear();
        getPrioritizedTasks().remove(epics.get(taskId));
        epics.remove(taskId);
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteSubTaskById удаляет задачу типа SubTask
    @Override
    public void deleteSubTaskById(Long taskId) throws MyException {
        // taskHistory.add(getSubTaskById(taskId));
        taskHistory.remove(taskId);
        taskHistory.getHistoryHashMap().remove(taskId);

        getPrioritizedTasks().remove(subTasks.get(taskId));
        subTasks.remove(taskId);
        listOfFreeNumber.add(taskId);
        epics.get(getSubTaskById(taskId).getNumberOfEpic()).getMapOfSubTasks().remove(taskId);
        updateTaskStatusOfEpic(epics.get(getSubTaskById(taskId).getNumberOfEpic()));

        // Актуализация значения поля EndTime у соответствующего Epic
        setEndTimeOfEpic(epics.get(getSubTaskById(taskId).getNumberOfEpic()));
        setStartTimeOfEpic(epics.get(getSubTaskById(taskId).getNumberOfEpic()));
    }

    // Метод deleteAllTasks удаляет все задачи и очищает историю
    @Override
    public void deleteAllTasks() throws MyException {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        taskHistory.clearHistory();
        getPrioritizedTasks().clear();
    }

    // Метод showTask выводит на экран список задач любого типа (Task, Epic, SubTask)
    @Override
    public void showTask(Long taskId) throws MyException {
        if (tasks.containsKey(taskId)){
            System.out.println("Имя задачи: " + getTaskById(taskId).getTaskName());
            System.out.println("Описание задачи: " + getTaskById(taskId).getTaskDescription());
            System.out.println("Статус задачи: " + getTaskById(taskId).getTaskStatus());
            System.out.println("Дата, когда предполагается приступить к выполнению задачи: " + getTaskById(taskId).getStartTime());
            System.out.println("Продолжительность задачи, оценка того, сколько времени она займёт: " + getTaskById(taskId).getDuration());
            taskHistory.add(getTaskById(taskId));
        } else if (epics.containsKey(taskId)) {
            System.out.println("Имя задачи: " + getEpicById(taskId).getTaskName());
            System.out.println("Описание задачи: " + getEpicById(taskId).getTaskDescription());
            System.out.println("Статус задачи: " + getEpicById(taskId).getTaskStatus());
            System.out.println("Дата, когда предполагается приступить к выполнению задачи: " + getEpicById(taskId).getStartTime());
            System.out.println("Продолжительность задачи, оценка того, сколько времени она займёт: " + getEpicById(taskId).getDuration());
            taskHistory.add(getEpicById(taskId));
            System.out.print("Данная задача типа Epic содержит подзадачи типа subTask со следующими номерами: ");
            for (Long key : subTasks.keySet()) {
                if (subTasks.get(key).getNumberOfEpic().equals(taskId)) {
                    System.out.print(subTasks.get(key).getTaskId() + ", ");
                }
            }
        } else if (subTasks.containsKey(taskId)) {
                System.out.println("Имя задачи: " + getSubTaskById(taskId).getTaskName());
                System.out.println("Описание задачи: " + getSubTaskById(taskId).getTaskDescription());
                System.out.println("Статус задачи: " + getSubTaskById(taskId).getTaskStatus());
                System.out.println("Дата, когда предполагается приступить к выполнению задачи: " + getSubTaskById(taskId).getStartTime());
                System.out.println("Продолжительность задачи, оценка того, сколько времени она займёт: " + getSubTaskById(taskId).getDuration());
                System.out.println("Номер epic для задачи: " + subTasks.get(taskId).getNumberOfEpic());
                taskHistory.add(getSubTaskById(taskId));
            } else {
                System.out.println("Ошибка! Текущая задача не подходит ни к одному из трех типов.");
        }
    }


    // Метод showAllTasks выводит на экран список всех задач любого из трёх типов (Task, Epic или SubTask)
    @Override
    public void showAllTasks() {
        if (tasks.size() != 0) {
            System.out.println("Список задач типа Task:");
            for (Long key : tasks.keySet()) {
                System.out.println(key + " - " + tasks.get(key).getTaskName());
            }
        } else {
            System.out.println("Нет ни одной задачи типа Task.");
        }
        if (epics.size() != 0) {
            System.out.println("\nСписок задач типа Epic:");
            for (long key : epics.keySet()) {
                System.out.println("\nЗадача \"" + epics.get(key).getTaskName() +
                                   "\" со следующими подзадачами типа subTask:");
                boolean flag = false;
                for (Long key2 : subTasks.keySet()) {
                    if (subTasks.get(key2).getNumberOfEpic().equals(key)) {
                        System.out.println("  " + key2 + " - " + subTasks.get(key2).getTaskName());
                        flag = true;
                    }
                }
                if (!flag) {
                    System.out.println("  У этой задачи нет подзадач.\n");
                }
            }
        } else {
            System.out.println("Нет ни одной задачи типа Epic и SubTask.");
        }
    }

    // Метод history выводит на экран упорядоченный список задач без повторов (историю вызова задач)
    @Override
    public List<Task> history() {
        return taskHistory.getHistory();
    }

    // Метод updateTaskStatusOfEpic меняет статус задачи типа Epic,
    // основываясь на статусах всех относящихся к ней подзадач (задач типа SubTask),
    // по следующему правилу:
    // - если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    // - если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    // - во всех остальных случаях статус должен быть IN_PROGRESS.
    public void updateTaskStatusOfEpic(Epic epic) {
        // Если у эпика нет подзадач, то статус должен быть NEW.
        if (epic.getMapOfSubTasks().size() == 0) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        boolean isAllStatusesEqualsNew = true;
        for (Long key : epic.getMapOfSubTasks().keySet()) {
            if (epic.getMapOfSubTasks().get(key).getTaskStatus() != TaskStatus.NEW) {
                isAllStatusesEqualsNew = false;
                break;
            }
        }
        if (isAllStatusesEqualsNew) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        boolean isAllStatusesEqualsDone = true;
        for (Long key : epic.getMapOfSubTasks().keySet()) {
            if (epic.getMapOfSubTasks().get(key).getTaskStatus() != TaskStatus.DONE) {
                isAllStatusesEqualsDone = false;
                break;
            }
        }
        if (isAllStatusesEqualsDone) {
            epic.setTaskStatus(TaskStatus.DONE);
            return;
        }

        // Во всех остальных случаях статус должен быть IN_PROGRESS.
        epic.setTaskStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public LocalDateTime getEndTimeOfTask(Task task) {
        return task.getStartTime().plusHours(task.getDuration().toHours())
                                  .plusMinutes(task.getDuration().toMinutes());
    }

    @Override
    public void setEndTimeOfEpic(Epic epic) {
        LocalDateTime max = LocalDateTime.of(1970, 1, 1, 0, 0);
        Duration lastDuration = Duration.ofSeconds(0);
        for (Long key : epic.getMapOfSubTasks().keySet()) {
            if (epic.getMapOfSubTasks().get(key).getStartTime().isAfter(max)) {
                max = epic.getMapOfSubTasks().get(key).getStartTime();
                lastDuration = epic.getMapOfSubTasks().get(key).getDuration();
            }
        }
        epic.setEndTime(max.plusHours(lastDuration.toHours())
                           .plusMinutes(lastDuration.toMinutes()));
    }

    @Override
    public void setStartTimeOfEpic(Epic epic) {
        LocalDateTime min = LocalDateTime.of(3000, 1, 1, 0, 0);
        for (Long key : epic.getMapOfSubTasks().keySet()) {
            if (epic.getMapOfSubTasks().get(key).getStartTime().isBefore(min)) {
                min = epic.getMapOfSubTasks().get(key).getStartTime();
            }
        }
        epic.setStartTime(min);
    }

    @Override
    public LocalDateTime getEndTimeOfSubTask(SubTask subTask) {
        return subTask.getStartTime().plusHours(subTask.getDuration().toHours())
                                     .plusMinutes(subTask.getDuration().toMinutes());
    }

    // Метод getPrioritizedTasks возвращает отсортированный список задач и подзадач в заданном порядке
    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    // Метод timeCheckOfTask проверяет наличие подходящего свободного временного промежутка между задачами
    @Override
    public boolean timeCheckOfTask (Task task) {
        Task previous = ((TreeSet<Task>)getPrioritizedTasks()).floor(task);
        Task next = ((TreeSet<Task>)getPrioritizedTasks()).ceiling(task);

        if (previous != null) {
            if (next != null) {
                return task.getStartTime().isAfter(previous.getStartTime().plus(previous.getDuration())) &&
                        task.getStartTime().plus(task.getDuration()).isBefore(next.getStartTime());
            } else {
                return task.getStartTime().isAfter(previous.getStartTime().plus(previous.getDuration()));
            }
        } else if (next != null) {
            return task.getStartTime().plus(task.getDuration()).isBefore(next.getStartTime());
        } else {
            return true;
        }
    }

    // Метод timeCheckOfSubTask проверяет наличие подходящего свободного временного промежутка между задачами
    @Override
    public boolean timeCheckOfSubTask (SubTask task) {
        Task previous = ((TreeSet<Task>)getPrioritizedTasks()).floor(task);
        Task next = ((TreeSet<Task>)getPrioritizedTasks()).ceiling(task);

        if (previous != null) {
            if (next != null) {
                return task.getStartTime().isAfter(previous.getStartTime().plus(previous.getDuration())) &&
                        task.getStartTime().plus(task.getDuration()).isBefore(next.getStartTime());
            } else {
                return task.getStartTime().isAfter(previous.getStartTime().plus(previous.getDuration()));
            }
        } else if (next != null) {
            return task.getStartTime().plus(task.getDuration()).isBefore(next.getStartTime());
        } else {
            return true;
        }
    }
}