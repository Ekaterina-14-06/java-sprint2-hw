package taskTracker.manager;

import taskTracker.tasks.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

// Класс HttpTaskServer слушает порт 8080 и принимает запросы.
public class HttpTaskServer {
    private static final int PORT_KVCLIENT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static HttpServer httpServer;
    private final TaskManager taskManager;
    private final Gson gson;

    // геттеры
    public static HttpServer getHttpServer() {
        return httpServer;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    // Объявление конструктора класса HttpTaskServer
    public HttpTaskServer() throws IOException {
        gson = new Gson();
        // Создание сервера
        httpServer = HttpServer.create();
        // Привязка сервера к порту
        httpServer.bind(new InetSocketAddress(PORT_KVCLIENT), 0);
        // Связывание пути и обработчика (handler) запроса (т.е. определение эндпоинта)
        httpServer.createContext("/tasks", new showAllTasksHandler());
        httpServer.createContext("/tasks/task", new newTaskHandler());
        httpServer.createContext("/tasks/epic", new newEpicHandler());
        httpServer.createContext("/tasks/subtask", new newSubTaskHandler());
        httpServer.createContext("/tasks/history", new historyHandler());
        taskManager = Managers.getDefault();
        // Запуск сервера
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT_KVCLIENT + " порту!");
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    // Объявление обработчика запроса
    class showAllTasksHandler implements HttpHandler {
        // Переопределение метода handle, который определяет логику работы эндпоинта
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента.");
            switch (httpExchange.getRequestMethod()) {
                case "GET": // showAllTasks
                    String response;
                    response = gson.toJson(getTaskManager().getPrioritizedTasks());
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(200, 0);
                        os.write(response.getBytes());
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                case "DELETE": // deleteAllTasks
                    try {
                        getTaskManager().deleteAllTasks();
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
            System.out.println("Началась обработка /tasks/task запроса от клиента.");
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    String response;
                    Headers headers = httpExchange.getRequestHeaders();
                    Long id = Long.parseLong(headers.get("TaskId").get(0));
                    response = gson.toJson(getTaskManager().getTaskById(id));
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(200, 0);
                        os.write(response.getBytes());
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                case "POST": // "PUT"
                    // InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                    // BufferedReader bufferedReader = new BufferedReader(streamReader);
                    // String body = bufferedReader.readLine();
                    String body = readText(httpExchange);

                    if (body.isEmpty()) {
                        System.out.println("В теле (body) запроса отсутствует задача Task.");
                        httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                        return;
                    }

                    Task taskPost = gson.fromJson(body, Task.class);
                    try {
                        getTaskManager().newTask(taskPost);
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "PATCH":
                    // InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                    // BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                    // String body2 = bufferedReader2.readLine();
                    String body2 = readText(httpExchange);

                    if (body2.isEmpty()) {
                        System.out.println("В теле (body) запроса отсутствует задача Task.");
                        httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                        return;
                    }

                    Task taskPost2 = gson.fromJson(body2, Task.class);
                    try {
                        getTaskManager().updateTask(taskPost2);
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
                            getTaskManager().deleteTaskById(id2);
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        try {
                            getTaskManager().deleteAllTasks();
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
                    response = gson.toJson(getTaskManager().getEpicById(id));
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

                    // InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                    // BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                    // String body2 = bufferedReader2.readLine();
                    String body = readText(httpExchange);

                    if (body.isEmpty()) {
                        System.out.println("В теле (body) запроса отсутствует задача Epic.");
                        httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                        return;
                    }

                    Epic epicPost2 = gson.fromJson(body, Epic.class);
                    try {
                        getTaskManager().updateEpic(epicPost2);
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST": // "PUT"
                    // InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                    // BufferedReader bufferedReader = new BufferedReader(streamReader);
                    // String body = bufferedReader.readLine();
                    String body2 = readText(httpExchange);

                    if (body2.isEmpty()) {
                        System.out.println("В теле (body) запроса отсутствует задача Epic.");
                        httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                        return;
                    }

                    Epic epicPost = gson.fromJson(body2, Epic.class);
                    try {
                        getTaskManager().newEpic(epicPost);
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
                            getTaskManager().deleteEpicById(id2);
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        try {
                            getTaskManager().deleteAllTasks();
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
            System.out.println("Началась обработка /tasks/subtask запроса от клиента.");
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    String response;
                    Headers headers = httpExchange.getRequestHeaders();

                    if (headers.get("TaskId") == null) {
                        httpExchange.sendResponseHeaders(404, 0);  // NOT_FOUND
                    }

                    Long id = Long.parseLong(headers.get("TaskId").get(0));
                    response = gson.toJson(getTaskManager().getSubTaskById(id));
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                case "POST": // "PUT"
                    // InputStreamReader streamReader = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                    // BufferedReader bufferedReader = new BufferedReader(streamReader);
                    // String body = bufferedReader.readLine();
                    String body = readText(httpExchange);

                    if (body.isEmpty()) {
                        System.out.println("В теле (body) запроса отсутствует подзадача subTask.");
                        httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                        return;
                    }

                    SubTask subTaskPost = gson.fromJson(body, SubTask.class);
                    try {
                        getTaskManager().newSubTask(subTaskPost);
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "PATCH":
                    // InputStreamReader streamReader2 = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET);
                    // BufferedReader bufferedReader2 = new BufferedReader(streamReader2);
                    // String body2 = bufferedReader2.readLine();
                    String body2 = readText(httpExchange);

                    if (body2.isEmpty()) {
                        System.out.println("В теле (body) запроса отсутствует подзадача subTask.");
                        httpExchange.sendResponseHeaders(400, 0);  // BAD_REQUEST
                        return;
                    }

                    SubTask subTaskPost2 = gson.fromJson(body2, SubTask.class);
                    try {
                        getTaskManager().updateSubtask(subTaskPost2);
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
                            getTaskManager().deleteSubTaskById(id2);
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        try {
                            getTaskManager().deleteAllTasks();
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
            System.out.println("Началась обработка /tasks/history запроса от клиента.");
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    String response;
                    Headers headers = httpExchange.getRequestHeaders();
                    Long id = Long.parseLong(headers.get("TaskId").get(0));
                    response = gson.toJson(getTaskManager().history());
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(200, 0);
                        os.write(response.getBytes());
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
            }
        }
    }
}