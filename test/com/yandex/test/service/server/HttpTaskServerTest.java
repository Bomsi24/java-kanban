package com.yandex.test.service.manager.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;
import com.yandex.app.service.server.HttpTaskServer;
import com.yandex.app.service.server.TasksHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServerTest {
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    Task task;
    private final Gson gson =  Managers.getGson();


    @BeforeEach
    void init() throws IOException {
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);
        task = taskManager.create(new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор", 50, "07:00 06.01.24"));

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200 ,response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task>actual = gson.fromJson(response.body(),taskType);

        Assertions.assertNotNull(actual,"Список пуст");
        Assertions.assertEquals(1,actual.size(),"Неверное количество");
        Assertions.assertEquals(task,actual.get(0), "Задачи не совпадают");
    }


}
