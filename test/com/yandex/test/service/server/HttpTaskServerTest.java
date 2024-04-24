package com.yandex.test.service.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.SubTaskDto;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;
import com.yandex.app.service.server.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskServerTest {
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    private Task task1;
    private Epic epic1;
    private SubTask subTask1;
    private final URI uriConst = URI.create("http://localhost:8080");
    private final Gson gson = Managers.getGson();


    @BeforeEach
    void init() throws IOException {
        task1 = new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор",
                50, LocalDateTime.of(2022, 6, 2, 22, 00));
        epic1 = new Epic("Строительство дома",
                "нужно подготовиться к строительству дома ",
                10, LocalDateTime.of(2024, 5, 10, 14, 1));
        subTask1 = new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                epic1, 17, LocalDateTime.of(2024, 5, 10, 17, 1));
        epic1.clearSubTask();
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void getAllTasksFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(task1);
        URI uri = URI.create(uriConst + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(task1, actual.get(0), "Задачи не совпадают");
    }

    @Test
    void getTaskByIdFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(task1);
        URI uri = URI.create(uriConst + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(task1, actual, "Задачи не совпадают");
    }

    @Test
    void postTaskCreateOnTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Task taskPost = new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор",
                50, LocalDateTime.of(2022, 6, 2, 22, 0));

        URI uri = URI.create(uriConst + "/tasks");
        String jsonTask = gson.toJson(taskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonTask, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void postUpdateTaskOnServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Task taskPost = new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор",
                50, LocalDateTime.of(2022, 6, 2, 22, 0));

        URI uri = URI.create(uriConst + "/tasks/1");
        String jsonTask = gson.toJson(taskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonTask, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    void deleteAllTaskFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(uriConst + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void deleteTaskByIdFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(uriConst + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void getAllEpicsFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(epic1);
        URI uri = URI.create(uriConst + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(epic1, actual.get(0), "Задачи не совпадают");
    }

    @Test
    void getEpicByIdFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(epic1);
        URI uri = URI.create(uriConst + "/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(epic1, actual, "Задачи не совпадают");
    }

    @Test
    void postEpicCreateOnTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic epicPost = new Epic("Строительство дома",
                "нужно подготовиться к строительству дома ",
                0, null);

        URI uri = URI.create(uriConst + "/epics");
        String jsonEpic = gson.toJson(epicPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void postUpdateEpicOnServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic epicPost = new Epic("Строительство дома",
                "нужно подготовиться к строительству дома ",
                0, null);

        URI uri = URI.create(uriConst + "/epics/1");
        String jsonEpic = gson.toJson(epicPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    void deleteAllEpicsFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(uriConst + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void deleteEpicByIdFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(uriConst + "/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void getAllSubTasksFromTheServer() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(epic1);
        taskManager.create(subTask1);
        URI uri = URI.create(uriConst + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type subTaskType = new TypeToken<ArrayList<SubTaskDto>>() {
        }.getType();
        List<SubTaskDto> subTaskDto = gson.fromJson(response.body(), subTaskType);
        List<SubTask> actual = subTaskDto.stream()
                .map(task -> new SubTask(task.getName(),
                        task.getDescription(), epic1, task.getDuration(), task.getStartTime()))
                .collect(Collectors.toList());

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(subTask1, actual.get(0), "Задачи не совпадают");

    }

    @Test
    void getSubTaskByIdFromTheServer() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(epic1);
        taskManager.create(subTask1);
        URI uri = URI.create(uriConst + "/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type subTaskType = new TypeToken<SubTaskDto>() {
        }.getType();
        SubTaskDto subTaskDto = gson.fromJson(response.body(), subTaskType);
        SubTask actual = new SubTask(subTaskDto.getName(), subTaskDto.getDescription(), epic1, subTaskDto.getDuration(), subTaskDto.getStartTime());

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(subTask1, actual, "Задачи не совпадают");

    }

    @Test
    void postSubTaskCreateOnTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        SubTask subTaskPost = new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                epic1, 17, LocalDateTime.now());

        URI uri = URI.create(uriConst + "/subtasks");
        String jsonEpic = gson.toJson(subTaskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void postUpdateSubTaskOnServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        SubTask subTaskPost = new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                epic1, 17, LocalDateTime.of(2024, 5, 10, 17, 1));

        URI uri = URI.create(uriConst + "/subtasks/1");
        String jsonEpic = gson.toJson(subTaskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void deleteAllSubTasksFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(uriConst + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void deleteSubTasksByIdFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(uriConst + "/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test
    void getPrioritizedTasksFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(task1);
        URI uri = URI.create(uriConst + "/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(task1, actual.get(0), "Задачи не совпадают");
    }

    @Test
    void getHistoryFromTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.create(task1);
        taskManager.getTask(task1.getId());
        URI uri = URI.create(uriConst + "/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(task1, actual.get(0), "Задачи не совпадают");
    }


}
