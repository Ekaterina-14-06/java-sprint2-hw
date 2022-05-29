package taskTracker.manager;

import com.google.gson.reflect.TypeToken;
import taskTracker.tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.google.gson.Gson;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();

    // Объявление КОНСТРУКТОРА КЛАССА
    // Объект класса HttpTaskManager принимает URL к серверу KVServer вместо имени файла
    public HttpTaskManager(String kvServerAddress, int kvServerPort, boolean load) throws IOException {
        // Класс HttpTaskManager создаёт kvTaskClient, из которого можно получить исходное состояние менеджера
        KVTaskClient kvTaskClient = new KVTaskClient(kvServerAddress, kvServerPort);
        if (load) {
            load();
        }
    }

    // Переопределение метода save (замена вызова сохранения в файл на вызов клиента).
    // Метод save будет сохранять историю и все типы задач в метод put класса KVTaskClient.
    @Override
    public void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        kvTaskClient.put("tasks", jsonTasks);

        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        kvTaskClient.put("epics", jsonEpics);

        String jsonSubTasks = gson.toJson(new ArrayList<>(subTasks.values()));
        kvTaskClient.put("subtasks", jsonSubTasks);

        String jsonHistory = gson.toJson(new ArrayList<>(taskHistory.getHistory()));
        kvTaskClient.put("history", jsonHistory);
    }

    // Метод load будет получать с помощью метода load из класса KVTaskClient все типы задач
    // и добавлять их в хранилище с помощью метода addTasks, реализованного ранее в классе HttpTaskManager,
    // и заполнять историю.
    public void load() {
        ArrayList<Task> jsonTasks = gson.fromJson(kvTaskClient.load("tasks"), new TypeToken<ArrayList<Task>>() {}.getType());
        addTasks(jsonTasks);

        ArrayList<Epic> jsonEpics = gson.fromJson(kvTaskClient.load("epics"), new TypeToken<ArrayList<Epic>>() {}.getType());
        addEpics(jsonEpics);

        ArrayList<SubTask> jsonSubTasks = gson.fromJson(kvTaskClient.load("subtasks"), new TypeToken<ArrayList<SubTask>>() {}.getType());
        addSubTasks(jsonSubTasks);

        ArrayList<Task> jsonHistory = gson.fromJson(kvTaskClient.load("history"), new TypeToken<ArrayList<Task>>() {}.getType());
        addHistory(jsonHistory);
    }

    // Метод addTasks загружает в наше хранилище задачи.
    public void addTasks(ArrayList<Task> jsonTasks) {
        for (Task task : jsonTasks) {
            tasks.put(task.getTaskId(), task);
        }
    }

    // Метод addTasks загружает в наше хранилище эпики.
    public void addEpics(ArrayList<Epic> jsonEpics) {
        for (Epic epic : jsonEpics) {
            epics.put(epic.getTaskId(), epic);
        }
    }

    // Метод addTasks загружает в наше хранилище подзадачи.
    public void addSubTasks(ArrayList<SubTask> jsonSubTasks) {
        for (SubTask subTask : jsonSubTasks) {
            subTasks.put(subTask.getTaskId(), subTask);
        }
    }

    // Метод addHistory загружает в наше хранилище список задач из истории.
    public void addHistory(ArrayList<Task> jsonHistory) {
        for (Task task : jsonHistory) {
            history().add(task);
        }
    }
}
