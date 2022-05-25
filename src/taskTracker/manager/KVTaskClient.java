package taskTracker.manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    HttpClient kvClient = HttpClient.newHttpClient();
    String kvServerToken;
    String kvServerUrl = "http://localhost:8078/";

    // Объявление КОНСТРУКТОРА КЛАССА
    // Объект класса принимает URL к серверу хранилища и регистрируется (т.е. получает токен API_TOKEN)
    public KVTaskClient() throws IOException { // <--- Исправить тип входного и выходного параметров!

            KVServer kvServer = new KVServer();
            kvServerToken = "DEBUG";

    }

    // Метод put сохраняет состояние менеджера задач через запрос POST/save/<ключ>?API_TOKEN=
    public void put(String key, String json) {
        URI url = URI.create(String.format("%s/save/%s?API_TOKEN=%s", kvServerUrl, key, kvServerToken));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            kvClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        load(key);
    }



    // Метод load возвращает состояние менеджера задач через запрос GET/load/<ключ>?API_TOKEN=
    public String load (String key) {
        URI url = URI.create(String.format("%s/load/%s?API_TOKEN=%s", kvServerUrl, key, kvServerToken));
        System.out.println(url);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = kvClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body().getBytes(StandardCharsets.UTF_8));
            String value = String.valueOf(response.body().getBytes(StandardCharsets.UTF_8));
            return value;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}