import java.util.HashMap;

public class Manager {
    // Описание методов данного класса:

    // Метод addNewTask добавляет новую задачу любого из трёх типов (Task, Epic, или SubTask)
    // и возвращает 0, если добавление прошло успешно, или код ошибки - в случае неудачи.
    public int addNewTask (int newTaskNumber,
                           int newTaskNumberOfEpic,
                           String newTaskType,
                           String newTaskName,
                           String newTaskDescription,
                           String newTaskStatus,
                           HashMap<Integer, Task> listOfTasks,
                           HashMap<Integer, Epic> listOfEpics) {
        switch (newTaskType) {
            case ("task"):
                // Создание объекта (задачи) типа Task:
                Task task = new Task();
                // Устанавление значений полей объекта типа Task перед его добавлением в HashMap listOfTasks:
                task.setTaskId(newTaskNumber);
                task.setTaskName(newTaskName);
                task.setTaskDescription(newTaskDescription);
                task.setTaskStatus(newTaskStatus);
                // Добавление объекта (задачи) типа Task в HashMap listOfTasks:
                listOfTasks.put(newTaskNumber, task);
                // break;
                return 0;
            case ("epic"):
                // Создание объекта (задачи) типа Epic:
                Epic epic = new Epic();
                // Устанавление значений полей объекта типа Epic перед его добавлением в HashMap listOfEpics:
                epic.setTaskId(newTaskNumber);
                epic.setTaskName(newTaskName);
                epic.setTaskDescription(newTaskDescription);
                epic.setTaskStatus(newTaskStatus);
                // Добавление объекта (задачи) типа Epic в HashMap listOfEpics:
                listOfEpics.put(newTaskNumber, epic);
                // break;
                return 0;
            case ("subTask"):
                // Проверка на наличие (существование) задачи типа Epic с полученным от пользователя номером,
                // для которой создаётся данная подзадача:
                if (listOfEpics.containsKey(newTaskNumberOfEpic)) {
                    // Создание объекта (задачи) типа SubTask:
                    SubTask subTask = new SubTask();
                    // Устанавление значений полей объекта типа SubTask перед его добавлением в HashMap listOfSubTasks:
                    subTask.setTaskId(newTaskNumber);
                    subTask.setTaskName(newTaskName);
                    subTask.setTaskDescription(newTaskDescription);
                    subTask.setTaskStatus(newTaskStatus);
                    subTask.setNumberOfEpic(newTaskNumberOfEpic);
                    // Добавление объекта (задачи) типа SubTask в HashMap listOfSubTasks конкретной задачи в HashMap ListOfEpics:
                    listOfEpics.get(newTaskNumberOfEpic).getListOfSubTasks().put(newTaskNumber, subTask);
                    // break;
                    return 0;
                } else {
                    // Произошла ошибка: попытка добавить подзадачу (SubTask) несуществующей задаче (Epic).
                    // break;
                    return 1; // 1 - код ошибки
                }
            default:
                return 2;
        }
    }

    // Метод updateTask вносит изменения только в поля Name и Description
    // объекта любого из трёх типов (Task, Epic, или SubTask)
    // и возвращает 0, если обновление прошло успешно, или код ошибки - в случае неудачи.
    public int updateTask (int taskNumber,
                           String taskNewName,
                           String taskNewDescription,
                           HashMap<Integer, Task> listOfTasks,
                           HashMap<Integer, Epic> listOfEpics) {
        // Определение типа задачи (Task, Epic или SubTask):
        if (listOfTasks.containsKey(taskNumber)) {
            // Обновление задачи типа Task:
            listOfTasks.get(taskNumber).setTaskName(taskNewName);
            listOfTasks.get(taskNumber).setTaskDescription(taskNewDescription);
            return 0;
        } else if (listOfEpics.containsKey(taskNumber)) {
            // Обновление задачи типа Epic:
            listOfEpics.get(taskNumber).setTaskName(taskNewName);
            listOfEpics.get(taskNumber).setTaskDescription(taskNewDescription);
            return 0;
        } else {
            // Обновление задачи типа SubTask:
            // Поиск подзадачи SubTask с заданным номером (taskNumber) во всех задачах типа Epic:
            for (int keyEpic : listOfEpics.keySet()) {
                Epic epic = listOfEpics.get(keyEpic);
                if (epic.getListOfSubTasks().containsKey(taskNumber)) {
                    epic.getListOfSubTasks().get(taskNumber).setTaskName(taskNewName);
                    epic.getListOfSubTasks().get(taskNumber).setTaskDescription(taskNewDescription);
                    return 0;
                }
            }
            // Произошла ошибка: попытка изменить подзадачу (SubTask) у несуществующей задачи (Epic).
            return 1; // 1 - код ошибки
        }
    }

    // Метод deleteTask удаляет задачу любого из трёх типов (Task, Epic, или SubTask)
    // и возвращает 0, если удаление прошло успешно, или код ошибки - в случае неудачи.
    public int deleteTask (int taskNumber,
                           HashMap<Integer, Task> listOfTasks,
                           HashMap<Integer, Epic> listOfEpics) {
        // Определение типа задачи (Task, Epic или SubTask):
        if (listOfTasks.containsKey(taskNumber)) {
            // Удаление задачи типа Task:
            listOfTasks.remove(taskNumber);
            return 0;
        } else if (listOfEpics.containsKey(taskNumber)) {
            // Удаление задачи типа Epic:
            listOfEpics.remove(taskNumber);
            return 0;
        } else {
            // Удаление задачи типа SubTask:
            // Поиск подзадачи SubTask с заданным номером (taskNumber) во всех задачах типа Epic:
            for (int keyEpic : listOfEpics.keySet()) {
                Epic epic = listOfEpics.get(keyEpic);
                if (epic.getListOfSubTasks().containsKey(taskNumber)) {
                    epic.getListOfSubTasks().remove(taskNumber);

                    // Если статус всех оставшихся после удаления подзадач, входящих в состав данной задачи типа epic,
                    // является done, то статус этой задача типа Epic переводится в состояние done.
                    boolean isStatusDone = false;
                    for (int keySubTask : epic.getListOfSubTasks().keySet()) {
                        if (epic.getListOfSubTasks().get(keySubTask).getTaskStatus().equals("done")) {
                            isStatusDone = true;
                        }
                    }
                    if (isStatusDone) {
                        epic.setTaskStatus("done");
                    }
                    return 0;
                }
            }
            // Произошла ошибка: попытка удалить несуществующую задачу.
            return 1; // 1 - код ошибки
        }
    }

    // Метод deleteAllTasks удаляет все задачи.
    public void deleteAllTasks (HashMap<Integer, Task> listOfTasks,
                                HashMap<Integer, Epic> listOfEpics) {
        listOfTasks.clear();
        for (int keyEpic : listOfEpics.keySet()) {
            listOfEpics.get(keyEpic).getListOfSubTasks().clear();
        }
        listOfEpics.clear();
    }

    // Метод showAllTasks выводит на экран список всех задач.
    public void showAllTasks (HashMap<Integer, Task> listOfTasks,
                              HashMap<Integer, Epic> listOfEpics) {
        System.out.println("Список задач типа Task:\n");
        if (listOfTasks.isEmpty()) {
            System.out.println("Список пуст.\n");
        } else {
            for (int keyTask : listOfTasks.keySet()) {
                System.out.println(listOfTasks.get(keyTask));
            }
        }
        System.out.println("================================\n");

        System.out.println("Список задач типа Epic:\n");
        if (listOfEpics.isEmpty()) {
            System.out.println("Список пуст.\n");
        } else {
            for (int keyEpic : listOfEpics.keySet()) {
                System.out.println(listOfEpics.get(keyEpic));
            }
        }
        System.out.println("================================\n");

        if (!listOfEpics.isEmpty()) {
            System.out.println("\nСписок задач типа SubTask:\n");
            for (int keyEpic : listOfEpics.keySet()) {
                System.out.println("У задачи '" + listOfEpics.get(keyEpic).getTaskName() + "' есть следующие подзадачи:\n");
                if (listOfEpics.get(keyEpic).getListOfSubTasks().isEmpty()) {
                    System.out.println("Список пуст.\n");
                } else {
                    for (int keySubTask : listOfEpics.get(keyEpic).getListOfSubTasks().keySet()) {
                        System.out.println(listOfEpics.get(keyEpic).getListOfSubTasks().get(keySubTask));
                    }
                }
                System.out.println("--------------------------------\n");
            }
        }
    }

    // Метод showTask выводит на экран выбранную задачу.
    public int showTask (int taskNumber,
                         HashMap<Integer, Task> listOfTasks,
                         HashMap<Integer, Epic> listOfEpics) {
        if (listOfTasks.containsKey(taskNumber)) {
            System.out.println(listOfTasks.get(taskNumber));
            return 0;
        } else if (listOfEpics.containsKey(taskNumber)) {
            System.out.println(listOfEpics.get(taskNumber));
            return 0;
        } else {
            for (int keyEpic : listOfEpics.keySet()) {
                Epic epic = listOfEpics.get(keyEpic);
                if (epic.getListOfSubTasks().containsKey(taskNumber)) {
                    System.out.println(epic.getListOfSubTasks().get(taskNumber));
                    return 0;
                }
            }
            // Произошла ошибка: попытка вывести на экран несуществующую задачу,
            // т.е. задачи с заданным номером не существует.
            return 1; // 1 - код ошибки
        }
    }

    // Метод, обеспечивающий изменение статуса выбранной задачи (любой из трёх типов: Task, Epic, SubTask):
    public int changeTaskStatus(int taskNumber,
                                String newTaskStatus,
                                HashMap<Integer, Task> listOfTasks,
                                HashMap<Integer, Epic> listOfEpics) {
        // Определение типа задачи (Task, Epic или SubTask):
        if (listOfTasks.containsKey(taskNumber)) {
            listOfTasks.get(taskNumber).setTaskStatus(newTaskStatus);
            return 0;
        } else if (listOfEpics.containsKey(taskNumber)) {
            return 1;
        } else {
            // Сначала меняется статус выбранной подзадачи (SubTask):
            boolean isExistSubTask = false;
            int keyEpic = 0;
            for (int keyEpic2 : listOfEpics.keySet()) {
                Epic epic = listOfEpics.get(keyEpic2);
                if (epic.getListOfSubTasks().containsKey(taskNumber)) {
                    isExistSubTask = true;
                    epic.getListOfSubTasks().get(taskNumber).setTaskStatus(newTaskStatus);
                    keyEpic = keyEpic2;
                    break;
                }
            }
            if (!isExistSubTask) {
                return 2;
            } else {
                // Затем, в случае необходимости, меняем статус её epic':
                boolean isStatusDone = false;
                for (int keySubTask : listOfEpics.get(keyEpic).getListOfSubTasks().keySet()) {
                    if (listOfEpics.get(keyEpic).getListOfSubTasks().get(keySubTask).getTaskStatus().equals("done")) {
                        isStatusDone = true;
                    }
                }
                if (isStatusDone) {
                    listOfEpics.get(keyEpic).setTaskStatus("done");
                } else {
                    listOfEpics.get(keyEpic).setTaskStatus("in progress");
                }
                return 0;
            }
        }
    }
}