package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    // mapOfTasks - список задач типа Task
    protected static final HashMap<Long, Task> tasks = new HashMap<>();

    // mapOfEpics - список задач типа Epic
    protected static final HashMap<Long, Epic> epics = new HashMap<>();

    // mapOfSubTasks - список задач типа SubTask
    protected static final HashMap<Long, SubTask> subTasks = new HashMap<>();

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
    public void newTask(Task task) {
        if (listOfFreeNumber.isEmpty()) {
            task.setTaskId(++countOfTasks);
        } else {
            task.setTaskId(listOfFreeNumber.get(0));
            listOfFreeNumber.remove(0);
        }
        tasks.put(task.getTaskId(), task);
    }

    // Метод newEpic создаёт новую задачу типа Epic
    @Override
    public void newEpic(Epic epic) {
        if (listOfFreeNumber.isEmpty()) {
            epic.setTaskId(++countOfTasks);
        } else {
            epic.setTaskId(listOfFreeNumber.get(0));
            listOfFreeNumber.remove(0);
        }
        epics.put(epic.getTaskId(), epic);
    }

    // Метод newSubTask создаёт новую задачу типа SubTask
    @Override
    public void newSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getNumberOfEpic());
        if (epic != null) {
            if (listOfFreeNumber.isEmpty()) {
                subTask.setTaskId(++countOfTasks);
            } else {
                subTask.setTaskId(listOfFreeNumber.get(0));
                listOfFreeNumber.remove(0);
            }
            subTasks.put(subTask.getTaskId(), subTask);
            epic.getMapOfSubTasks().put(subTask.getTaskId(), subTask);
            updateTaskStatusOfEpic(epic);
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
    public void updateTask(Task task) {
        Task updatableTask = getTaskById(task.getTaskId());
        if (updatableTask == null) {
            System.out.println("Такой задачи нет.");
            return;
        }
        updatableTask.setTaskName(task.getTaskName());
        updatableTask.setTaskDescription(task.getTaskDescription());
        updatableTask.setTaskStatus(task.getTaskStatus());
        taskHistory.add(updatableTask);
    }

    public void updateEpic (Epic epic) {
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

    public void updateSubtask(SubTask subTask) {
        SubTask updatableSubTask = getSubTaskById(subTask.getTaskId());
        if (updatableSubTask == null) {
            System.out.println("Такой задачи нет.");
            return;
        }

        updatableSubTask.setTaskName(subTask.getTaskName());
        updatableSubTask.setTaskDescription(subTask.getTaskDescription());
        updatableSubTask.setTaskStatus(subTask.getTaskStatus());
        updateTaskStatusOfEpic(epics.get(updatableSubTask.getNumberOfEpic()));
        //taskHistory.add(updatableSubTask);
    }

    // Метод deleteTaskById удаляет задачу типа Task
    @Override
    public void deleteTaskById(Long taskId) {
        taskHistory.remove(taskId);
        taskHistory.getHistoryHashMap().remove(taskId);

        tasks.remove(taskId);
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteEpicById удаляет задачу типа Epic
    @Override
    public void deleteEpicById(Long taskId) {
        // taskHistory.add(getEpicById(taskId));
        taskHistory.remove(taskId);
        taskHistory.getHistoryHashMap().remove(taskId);

        // Удаление подзадач типа SubTask, относящихся к выбранной задаче типа Epic
        for (Long key : getEpicById(taskId).getMapOfSubTasks().keySet()) {
            subTasks.remove(key);
        }
        getEpicById(taskId).getMapOfSubTasks().clear();
        epics.remove(taskId);
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteSubTaskById удаляет задачу типа SubTask
    @Override
    public void deleteSubTaskById(Long taskId) {
        // taskHistory.add(getSubTaskById(taskId));
        taskHistory.remove(taskId);
        taskHistory.getHistoryHashMap().remove(taskId);

        subTasks.remove(taskId);
        listOfFreeNumber.add(taskId);
        epics.get(getSubTaskById(taskId).getNumberOfEpic()).getMapOfSubTasks().remove(taskId);
        updateTaskStatusOfEpic(epics.get(getSubTaskById(taskId).getNumberOfEpic()));
    }

    // Метод deleteAllTasks удаляет все задачи и очищает историю
    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        taskHistory.clearHistory();
    }

    // Метод showTask выводит на экран список задач любого типа (Task, Epic, SubTask)
    @Override
    public void showTask(Long taskId) {
        if (tasks.containsKey(taskId)){
            System.out.println("Имя задачи: " + getTaskById(taskId).getTaskName());
            System.out.println("Описание задачи: " + getTaskById(taskId).getTaskDescription());
            System.out.println("Статус задачи: " + getTaskById(taskId).getTaskStatus());
            taskHistory.add(getTaskById(taskId));
        } else if (epics.containsKey(taskId)) {
            System.out.println("Имя задачи: " + getEpicById(taskId).getTaskName());
            System.out.println("Описание задачи: " + getEpicById(taskId).getTaskDescription());
            System.out.println("Статус задачи: " + getEpicById(taskId).getTaskStatus());
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

        // Если у эпика все подзадачи имеют статус NEW, то статус должен быть NEW.
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

        // Если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
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
}