package com.yandex.app.service.server;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class TasksHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                tasksGet(exchange);
                break;
            case "POST":
                tasksPost(exchange);
                break;
            case "DELETE":
                tasksDelete(exchange);
                break;
        }
    }

    private void tasksGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/tasks$", path)) {//Если нет id возвращаем все задачи
            List<Task> tasks = manager.getAllTasks();
            response = gson.toJson(tasks);
            sendText(exchange, response);

        } else if (Pattern.matches("^/tasks/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/tasks/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                Task task = manager.getTask(id);
                if (task != null) {
                    response = gson.toJson(task);
                    sendText(exchange, response);
                }
                writeResponse(exchange, "Задачи с id = " + pathId + " нет.", 404);

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId, 404);
            }
        }
    }

    private void tasksPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/tasks$", path)) {//Создаем
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task taskRequest = gson.fromJson(body, Task.class);
            Task taskCreate = manager.create(taskRequest);
            if (taskCreate != null) {
                System.out.println("Задача создана");
                exchange.sendResponseHeaders(201, 0);
                exchange.close();

            } else {//Код ошибки\
                writeResponse(exchange, "Задача пересекается с существующими", 406);
            }
        } else if (Pattern.matches("^/tasks/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/tasks/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task taskRequest = gson.fromJson(body, Task.class);
                Task taskUpdate = manager.update(taskRequest);
                if (taskUpdate != null) {
                    System.out.println("Задача обновлена");
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();

                } else {
                    writeResponse(exchange, "Задача пересекается с существующими", 406);
                }

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId, 406);
            }
        }
    }

    private void tasksDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/tasks$", path)) {//Если нет id возвращаем все задачи
            manager.deleteTasks();
            System.out.println("Задачи удалены");
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        } else if (Pattern.matches("^/tasks/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/tasks/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteTaskInId(id);
                System.out.println("Задача удалена");
                exchange.sendResponseHeaders(201, 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId, 406);
            }
        }
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(StandardCharsets.UTF_8));
        }
        exchange.close();
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

}
