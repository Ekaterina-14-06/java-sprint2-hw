package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    // ----------------------------------------------------------------------------------------------------------------
    // ОБЪЯВЛЕНИЕ ЛОКАЛЬНЫХ ПЕРЕМЕННЫХ ДАННОГО КЛАССА

    // mapOfTasks - список задач типа Task
    protected final HashMap<Long, Task> tasks = new HashMap<>();

    // mapOfEpics - список задач типа Epic
    protected final HashMap<Long, Epic> epics = new HashMap<>();

    // mapOfSubTasks - список задач типа SubTask
    protected final HashMap<Long, SubTask> subTasks = new HashMap<>();

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
        //taskHistory.add(task);
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
       // taskHistory.add(epic);
    }

    // Метод newSubTask создаёт новую задачу типа SubTask
    @Override
    public void newSubTask(SubTask subTask) {
        boolean flag = false;
        for (Long key : epics.keySet()) {
            if (epics.get(key).getTaskId() == subTask.getNumberOfEpic()) {
                flag = true;
                break;
            }
        } if (flag) {

            if (listOfFreeNumber.isEmpty()) {
                subTask.setTaskId(++countOfTasks);
            } else {
                subTask.setTaskId(listOfFreeNumber.get(0));
                listOfFreeNumber.remove(0);
            }
            subTasks.put(subTask.getTaskId(), subTask);
            // taskHistory.add(subTask);
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
    //public void updateTask(Long taskId, String name, String description, TaskStatus newTaskStatus) {
    public void updateTask(Long taskId, Task task) {
        if (getTaskById(taskId) == null) {
            System.out.println("Такой задачи нет.");
            return;
        }

        if (taskId != task.getTaskId()) {
            if (tasks.containsKey(task.getTaskId()) ||
                epics.containsKey(task.getTaskId()) ||
                subTasks.containsKey(task.getTaskId())) {
                System.out.println("Ошибка! Задача с таким taskId уже существует. Операция обновления не произведена");
                return;
            } else {
                getTaskById(taskId).setTaskId(task.getTaskId());
            }
        }

        getTaskById(taskId).setTaskName(task.getTaskName());
        getTaskById(taskId).setTaskDescription(task.getTaskDescription());
        getTaskById(taskId).setTaskStatus(task.getTaskStatus());
        taskHistory.add(getTaskById(taskId));
    }

    public void updateEpic (Long taskId, Epic epic) {
        if (getEpicById(taskId) == null) {
            System.out.println("Такой задачи нет.");
            return;
        }

        if (taskId != epic.getTaskId()) {
            if (tasks.containsKey(epic.getTaskId()) ||
                epics.containsKey(epic.getTaskId()) ||
                subTasks.containsKey(epic.getTaskId())) {
                System.out.println("Ошибка! Задача с таким taskId уже существует. Операция обновления не произведена");
                return;
            } else {
                getTaskById(taskId).setTaskId(epic.getTaskId());
            }
        }

        getEpicById(taskId).setTaskName(epic.getTaskName());
        getEpicById(taskId).setTaskDescription(epic.getTaskDescription());

        if (getEpicById(taskId).getTaskStatus() != epic.getTaskStatus()) {
            System.out.println("Задачи типа Epic самостоятельно менять статус не могут.\n" +
                    "Их состояние меняется автоматически при смене статусов её подзадач (SubTask).");
        }

        taskHistory.add(getEpicById(taskId));
    }

    public void updateSubtask(Long taskId, SubTask subTask) {
        if (getSubTaskById(taskId) == null) {
            System.out.println("Такой задачи нет.");
            return;
        }

        if (taskId != subTask.getTaskId()) {
            if (tasks.containsKey(subTask.getTaskId()) ||
                    epics.containsKey(subTask.getTaskId()) ||
                    subTasks.containsKey(subTask.getTaskId())) {
                System.out.println("Ошибка! Задача с таким taskId уже существует. Операция обновления не произведена");
                return;
            } else {
                getTaskById(taskId).setTaskId(subTask.getTaskId());
            }
        }

        getSubTaskById(taskId).setTaskName(subTask.getTaskName());
        getSubTaskById(taskId).setTaskDescription(subTask.getTaskDescription());
        getSubTaskById(taskId).setTaskStatus(subTask.getTaskStatus());

        // Проверка остальных задач типа SubTask данной задачи типа Epic на соответствие состоянию DONE
        if (getSubTaskById(taskId).getTaskStatus() == TaskStatus.DONE) {
            Long numberOfEpic = subTasks.get(taskId).getNumberOfEpic();
            boolean isDone = false;
            boolean singleSubTaskIsDone = false;
            for (Long key : subTasks.keySet()) {
                if (subTasks.get(key).getNumberOfEpic().equals(numberOfEpic)) {
                    singleSubTaskIsDone = true;
                    isDone = subTasks.get(key).getTaskStatus().equals(TaskStatus.DONE);
                    if (!isDone) break;
                }
            }
            if (singleSubTaskIsDone) {
                epics.get(numberOfEpic).setTaskStatus(TaskStatus.DONE);
            } else if (isDone) {
                epics.get(numberOfEpic).setTaskStatus(TaskStatus.DONE);
            }
        }

        taskHistory.add(getSubTaskById(taskId));
    }

    // Метод deleteTaskById удаляет задачу типа Task
    @Override
    public void deleteTaskById(Long taskId) {
        taskHistory.add(getTaskById(taskId));
        tasks.remove(taskId);
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteEpicById удаляет задачу типа Epic
    @Override
    public void deleteEpicById(Long taskId) {
        taskHistory.add(getEpicById(taskId));
        // Удаление подзадач типа SubTask, относящихся к выбранной задаче типа Epic
        boolean flag;
        do {
           Set<Long> keys = subTasks.keySet();
           if (keys.isEmpty()) break;
           flag = false;
           for (Long key : keys) {
               if (subTasks.get(key).getNumberOfEpic().equals(taskId)) {
                   subTasks.remove(key);
                   listOfFreeNumber.add(key);
                   flag = true;
                   break;
               }
            }
        } while (flag);
        epics.remove(taskId);
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteSubTaskById удаляет задачу типа SubTask
    @Override
    public void deleteSubTaskById(Long taskId) {
        taskHistory.add(getSubTaskById(taskId));
        subTasks.remove(taskId);
        listOfFreeNumber.add(taskId);
    }

    // Метод deleteAllTasks удаляет все задачи
    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        taskHistory.setListHead(null);
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

    @Override
    public void loadFromFile(){

    }
}