package taskTracker.manager;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import taskTracker.tasks.*;

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
public class HttpTaskServer {
    private static final int PORT_KVCLIENT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static HttpServer httpServer;
    private final TaskManager taskManager;

    // геттеры
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
        this.httpServer.bind(new InetSocketAddress(PORT_KVCLIENT), 0);
        // Связывание пути и обработчика (handler) запроса (т.е. определение эндпоинта)
        this.httpServer.createContext("/tasks", new showAllTasksHandler());
        this.httpServer.createContext("/tasks/task", new newTaskHandler());
        this.httpServer.createContext("/tasks/epic", new newEpicHandler());
        this.httpServer.createContext("/tasks/subtask", new newSubTaskHandler());
        this.httpServer.createContext("/tasks/history", new historyHandler());
        this.taskManager = Managers.getDefault();
        // Запуск сервера
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT_KVCLIENT + " порту!");
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
                try {
                    httpTaskServer2.getTaskManager().deleteAllTasks();
                } catch (MyException e) {
                    e.printStackTrace();
                }
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

                if (body.length() == 0) {
                    httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                    break;
                }

                Task taskPost = gson.fromJson(body, Task.class);
                try {
                    httpTaskServer.getTaskManager().newTask(taskPost);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "PATCH":
                InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                String body2 = bufferedReader2.readLine();

                if (body2.length() == 0) {
                    httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                    break;
                }

                Task taskPost2 = gson.fromJson(body2, Task.class);
                try {
                    httpTaskServer.getTaskManager().updateTask(taskPost2);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                Headers headers2 = httpExchange.getRequestHeaders();

                if (headers2.get("TaskId") != null) {
                    Long id2 = Long.parseLong(headers2.get("TaskId").get(0));
                    try {
                        httpTaskServer.getTaskManager().deleteTaskById(id2);
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    try {
                        httpTaskServer.getTaskManager().deleteAllTasks();
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                }
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

                if (headers.get("TaskId") == null) {
                    httpExchange.sendResponseHeaders(404, 0);  // NOT_FOUND
                    return;
                }

                Long id = Long.parseLong(headers.get("TaskId").get(0));
                response = gson.toJson(httpTaskServer.getTaskManager().getEpicById(id));
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(200, 0);
                    os.write(response.getBytes());
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "PATCH":
                // Дописать кейс с обновлением эпика.
                // ИЛИ можно не вводить новый кейс PATCH, а просто вытащить из переданного объекта идентификатор.
                // Если объект передан с идентификатором - обновляем, если без - добавляем новый)
                InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                String body2 = bufferedReader2.readLine();

                if (body2.length() == 0) {
                    httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                    break;
                }

                Epic epicPost2 = gson.fromJson(body2, Epic.class);
                try {
                    httpTaskServer.getTaskManager().updateEpic(epicPost2);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "POST": // "PUT"
                InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String body = bufferedReader.readLine();

                if (body.length() == 0) {
                    httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                    break;
                }

                Epic epicPost = gson.fromJson(body, Epic.class);
                try {
                    httpTaskServer.getTaskManager().newEpic(epicPost);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                Headers headers2 = httpExchange.getRequestHeaders();

                if (headers2.get("TaskId") != null) {
                    Long id2 = Long.parseLong(headers2.get("TaskId").get(0));
                    try {
                        httpTaskServer.getTaskManager().deleteEpicById(id2);
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    try {
                        httpTaskServer.getTaskManager().deleteAllTasks();
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                }
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

                if (headers.get("TaskId") == null) {
                    httpExchange.sendResponseHeaders(404, 0);  // NOT_FOUND
                }

                Long id = Long.parseLong(headers.get("TaskId").get(0));
                response = gson.toJson(httpTaskServer.getTaskManager().getSubTaskById(id));
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "POST": // "PUT"
                InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String body = bufferedReader.readLine();

                if (body.length() == 0) {
                    httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                }

                SubTask subTaskPost = gson.fromJson(body, SubTask.class);
                try {
                    httpTaskServer.getTaskManager().newSubTask(subTaskPost);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "PATCH":
                InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                String body2 = bufferedReader2.readLine();

                if (body2.length() == 0) {
                    httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                }

                SubTask subTaskPost2 = gson.fromJson(body2, SubTask.class);
                try {
                    httpTaskServer.getTaskManager().updateSubtask(subTaskPost2);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                Headers headers2 = httpExchange.getRequestHeaders();

                if (headers2.get("TaskId") != null) {
                    Long id2 = Long.parseLong(headers2.get("TaskId").get(0));
                    try {
                        httpTaskServer.getTaskManager().deleteSubTaskById(id2);
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    try {
                        httpTaskServer.getTaskManager().deleteAllTasks();
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                }
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