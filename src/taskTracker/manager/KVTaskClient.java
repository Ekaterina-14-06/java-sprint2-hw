package taskTracker.manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    String kvServerUrl;
    String kvServerAddress;
       int kvServerPort;
    String kvServerToken;
    HttpClient kvClient = HttpClient.newHttpClient();

    // Объявление КОНСТРУКТОРА КЛАССА
    // Объект класса принимает URL к серверу хранилища и регистрируется (т.е. получает токен API_TOKEN)
    public KVTaskClient(String addressKVServer, int portKVServer) throws IOException {
        this.kvServerAddress = addressKVServer;
        this.kvServerPort = portKVServer;
             kvServerUrl = addressKVServer + ":" + portKVServer + "/";
             kvServerToken = register(kvServerUrl);  // kvServerToken = "DEBUG";
    }

    // Метод register возвращает токен apiToken, необходимый для авторизации при доступе к серверу KVServer
    public String register(String kvServerUrl) {
        try {
            String url = String.format("%s/register", kvServerUrl);
            URI uri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder()  // создаём запрос
                    .GET()     // указываем команду
                    .uri(uri)  // указание адреса ресурса
                    .build();  // создание HTTP-запроса
            HttpResponse<String> response = kvClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
            }
            return String.valueOf(response.body().getBytes(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            // e.printStackTrace();
            System.out.println("Во время выполнения запроса возникла ошибка. /n" +
                    "Проверьте, пожалуйста, URL-адрес и повторите попытку.");
            //throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
            return "";
        } catch (IllegalArgumentException e) {
            // e.printStackTrace();
            System.out.println("Введённый вами адрес не соответствует формату URL. /n" +
                    "Попробуйте, пожалуйста, снова.");
            //throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
            return "";
        } catch (MyException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    // Метод put сохраняет состояние менеджера задач (историю и все типы задач?) через запрос POST/save/<ключ>?API_TOKEN=
    public void put(String key, String json) throws MyException {
        try {
            String url = String.format("%s/save/%s?API_TOKEN=%s", kvServerUrl, key, kvServerToken);
            URI uri = URI.create(url);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder()  // создаём запрос
                    .POST(body)  // указываем команду
                    .uri(uri)    // указание адреса ресурса
                    .build();    // создание HTTP-запроса
            HttpResponse<String> response = kvClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
            }
        } catch (IOException | InterruptedException e) {
            // e.printStackTrace();
            System.out.println("Во время выполнения запроса возникла ошибка. /n" +
                               "Проверьте, пожалуйста, URL-адрес и повторите попытку.");
            //throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
        } catch (IllegalArgumentException e) {
            // e.printStackTrace();
            System.out.println("Введённый вами адрес не соответствует формату URL. /n" +
                               "Попробуйте, пожалуйста, снова.");
            //throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод load возвращает состояние менеджера задач через запрос GET/load/<ключ>?API_TOKEN=
    public String load (String key) throws MyException {
        try {
            String url = String.format("%s/load/%s?API_TOKEN=%s", kvServerUrl, key, kvServerToken);
            URI uri = URI.create(url);
            System.out.println(url);
            HttpRequest request = HttpRequest.newBuilder()  // создаём запрос
                    .GET()     // указываем команду
                    .uri(uri)  // указание адреса ресурса
                    .build();  // создание HTTP-запроса
            HttpResponse<String> response = kvClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
            }
            System.out.println(response.body().getBytes(StandardCharsets.UTF_8));
            String value = String.valueOf(response.body().getBytes(StandardCharsets.UTF_8));
            return value;
        } catch (IOException | InterruptedException e) {
            // e.printStackTrace();
            System.out.println("Во время выполнения запроса возникла ошибка. /n" +
                    "Проверьте, пожалуйста, URL-адрес и повторите попытку.");
            //throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
            return "";
        } catch (IllegalArgumentException e) {
            // e.printStackTrace();
            System.out.println("Введённый вами адрес не соответствует формату URL. /n" +
                    "Попробуйте, пожалуйста, снова.");
            //throw new MyException("Ошибка ответа от сервера. Статус-код не равен 200 (OK).");
            return "";
        } catch (MyException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
}

public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
}