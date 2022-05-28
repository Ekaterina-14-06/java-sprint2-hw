package taskTracker.manager;

import taskTracker.tasks.*;
import java.util.List;
import java.io.IOException;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient kvTaskClient;

    // Объявление КОНСТРУКТОРА КЛАССА
    // Объект класса HttpTaskManager принимает URL к серверу KVServer вместо имени файла
    public HttpTaskManager(String kvServerAddress, int kvServerPort) throws IOException {
        // Класс HttpTaskManager создаёт kvTaskClient, из которого можно получить исходное состояние менеджера
        KVTaskClient kvTaskClient = new KVTaskClient(kvServerAddress, kvServerPort);
    }

    // Переопределение метода save (замена вызова сохранения в файл на вызов клиента).
    // Метод save будет сохранять историю и все типы задач в метод put класса KVTaskClient.
    @Override
    public void save() throws MyException {
        String result = "id,type,name,status,description,startTime,duration,epic\n";
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

        kvTaskClient.put("", result);  // <--- НЕ ЗНАЮ, что такое key!
    }

    // Метод load будет получать с помощью метода load из класса KVTaskClient все типы задач
    // и добавлять их в хранилище с помощью метода addTasks, реализованного ранее в классе HttpTaskManager,
    // и заполнять историю.
    public void load() throws MyException {
        addTasks(kvTaskClient.load(""));  // <--- НЕ ЗНАЮ, что такое key!
    }

    // Метод addTasks будет загружать в наше хранилище задачи, подзадачи, эпики.
    public void addTasks(String contentKVServer) {
        // contentKVServer - строка, содержащая все задачи, полученные из KVServer (аналог метода loadFromFile класса FileBackedTasksManager).
        String[] lines = contentKVServer.split("\n");
        for (int i = 1; i < lines.length; i++) {
            if (lines[i] == null) break;
            if (lines[i].equals("")) break;

            String[] split = lines[i].split(",");
            if (split[1].equals("TASK")) {
                tasks.put(taskFromString(lines[i]).getTaskId(), taskFromString(lines[i]));
            } else if (split[1].equals("EPIC")) {
                epics.put(epicFromString(lines[i]).getTaskId(), epicFromString(lines[i]));
            } else if (split[1].equals("SUB_TASK")) {
                subTasks.put(subTaskFromString(lines[i]).getTaskId(), subTaskFromString(lines[i]));
            }
        }
    }
}
