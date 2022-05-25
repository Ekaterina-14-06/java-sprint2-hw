package taskTracker.manager;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import taskTracker.tasks.Epic;
import taskTracker.tasks.SubTask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

// Класс HttpTaskServer слушает порт 8080 и принимает запросы.
// Данный класс реализует класс FileBackedTasksManager.
public class HttpTaskServer extends FileBackedTasksManager {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static HttpServer httpServer;
    private final TaskManager taskManager;

    //геттеры
    public static HttpServer getHttpServer() {
        return httpServer;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }


    // Объявление конструктора класса HttpTaskServer
    public HttpTaskServer() throws IOException {
        // Создание сервера
        this.httpServer = HttpServer.create();
        // Привязка сервера к порту
        this.httpServer.bind(new InetSocketAddress(PORT), 0);
        // Связывание пути и обработчика (handler) запроса (т.е. определение эндпоинта)
        this.httpServer.createContext("/tasks", new showAllTasksHandler());
        this.httpServer.createContext("/tasks/task", new newTaskHandler());
        this.httpServer.createContext("/tasks/epic", new newEpicHandler());
        this.httpServer.createContext("/tasks/subtask", new newSubTaskHandler());
        this.httpServer.createContext("/tasks/history", new historyHandler());
        this.taskManager = Managers.getDefault();
        // Запуск сервера
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    @Override
    public void newTask(Task task) {
        super.newTask(task);
    }

    @Override
    public void newEpic(Epic epic) {
        super.newEpic(epic);
    }

    @Override
    public void newSubTask(SubTask subTask) {
        super.newSubTask(subTask);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    public void updateEpic (Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
    }

    @Override
    public void deleteTaskById(Long taskId) {
        super.deleteTaskById(taskId);
    }

    @Override
    public void deleteEpicById(Long taskId) {
        super.deleteEpicById(taskId);
    }

    @Override
    public void deleteSubTaskById(Long taskId) {
        super.deleteSubTaskById(taskId);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public void showTask(Long taskId) {
        super.showTask(taskId);
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
    public Set<Task> getPrioritizedTasks() {
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

    @Override
    public void save() {
        super.save();
    }

    @Override
    public String toString(Task task) {
        return super.toString(task);
    }

    @Override
    public String taskToString(Task task) {
        return super.taskToString(task);
    }

    @Override
    public String epicToString(Task task) {
        return super.epicToString(task);
    }

    @Override
    public String subTaskToString(Task task) {
        return super.subTaskToString(task);
    }

    @Override
    public Task taskFromString(String value) {
        return super.taskFromString(value);
    }

    @Override
    public Epic epicFromString(String value) {
        return super.epicFromString(value);
    }

    @Override
    public SubTask subTaskFromString(String value) {
        return super.subTaskFromString(value);
    }

    @Override
    public void loadFromFile(){
        super.loadFromFile();
    }

    @Override
    public boolean fileExists(String fileName) {
        return super.fileExists(fileName);
    }
}

// Объявление обработчика запроса
class showAllTasksHandler implements HttpHandler {
    // Переопределение метода handle, который определяет логику работы эндпоинта
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /tasks запроса от клиента.");
        switch (httpExchange.getRequestMethod()) {
            case "GET": // showAllTasks
                Gson gson = new Gson();
                HttpTaskServer httpTaskServer = new HttpTaskServer();
                String response;
                response = gson.toJson(httpTaskServer.getTaskManager().getPrioritizedTasks());
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(200, 0);
                    os.write(response.getBytes());
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "DELETE": // deleteAllTasks
                HttpTaskServer httpTaskServer2 = new HttpTaskServer();
                httpTaskServer2.getTaskManager().deleteAllTasks();
                httpExchange.sendResponseHeaders(200, 0);
        }
    }
}

// Объявление обработчика запроса
class newTaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
         Gson gson = new Gson();
        HttpTaskServer httpTaskServer = new HttpTaskServer();


        System.out.println("Началась обработка /tasks/task запроса от клиента.");
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                String response;
                Headers headers = httpExchange.getRequestHeaders();
                Long id = Long.parseLong(headers.get("TaskId").get(0));
                response = gson.toJson(httpTaskServer.getTaskManager().getTaskById(id));
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(200, 0);
                    os.write(response.getBytes());
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "POST": // "PUT"
                InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String body = bufferedReader.readLine();
                Task taskPost = gson.fromJson(body, Task.class);
                httpTaskServer.getTaskManager().newTask(taskPost);
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "PATCH":
                InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                String body2 = bufferedReader2.readLine();
                Task taskPost2 = gson.fromJson(body2, Task.class);
                httpTaskServer.getTaskManager().updateTask(taskPost2);
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                Headers headers2 = httpExchange.getRequestHeaders();
                Long id2 = Long.parseLong(headers2.get("TaskId").get(0));
                httpTaskServer.getTaskManager().deleteTaskById(id2);
                httpExchange.sendResponseHeaders(200, 0);
        }
    }
}

// Объявление обработчика запроса
class newEpicHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        Gson gson = new Gson();
        System.out.println("Началась обработка /tasks/epic запроса от клиента.");
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                String response;
                Headers headers = httpExchange.getRequestHeaders();
                Long id = Long.parseLong(headers.get("TaskId").get(0));
                response = gson.toJson(httpTaskServer.getTaskManager().getEpicById(id));
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(200, 0);
                    os.write(response.getBytes());
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "POST": // "PUT"
                InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String body = bufferedReader.readLine();
                Epic epicPost = gson.fromJson(body, Epic.class);
                httpTaskServer.getTaskManager().newEpic(epicPost);
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                Headers headers2 = httpExchange.getRequestHeaders();
                Long id2 = Long.parseLong(headers2.get("TaskId").get(0));
                httpTaskServer.getTaskManager().deleteEpicById(id2);
                httpExchange.sendResponseHeaders(200, 0);
        }
    }
}

// Объявление обработчика запроса
class newSubTaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        Gson gson = new Gson();
        System.out.println("Началась обработка /tasks/subtask запроса от клиента.");
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                String response;
                Headers headers = httpExchange.getRequestHeaders();
                Long id = Long.parseLong(headers.get("TaskId").get(0));
                response = gson.toJson(httpTaskServer.getTaskManager().getSubTaskById(id));
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(200, 0);
                    os.write(response.getBytes());
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "POST": // "PUT"
                InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String body = bufferedReader.readLine();
                SubTask subTaskPost = gson.fromJson(body, SubTask.class);
                httpTaskServer.getTaskManager().newSubTask(subTaskPost);
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "PATCH":
                InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                String body2 = bufferedReader2.readLine();
                SubTask subTaskPost2 = gson.fromJson(body2, SubTask.class);
                httpTaskServer.getTaskManager().updateSubtask(subTaskPost2);
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                Headers headers2 = httpExchange.getRequestHeaders();
                Long id2 = Long.parseLong(headers2.get("TaskId").get(0));
                httpTaskServer.getTaskManager().deleteSubTaskById(id2);
                httpExchange.sendResponseHeaders(200, 0);
        }
    }
}

// Объявление обработчика запроса
class historyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        Gson gson = new Gson();
        System.out.println("Началась обработка /tasks/history запроса от клиента.");
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                String response;
                Headers headers = httpExchange.getRequestHeaders();
                Long id = Long.parseLong(headers.get("TaskId").get(0));
                response = gson.toJson(httpTaskServer.getTaskManager().history());
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(200, 0);
                    os.write(response.getBytes());
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            }
    }
}