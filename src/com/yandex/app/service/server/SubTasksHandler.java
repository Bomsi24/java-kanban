package com.yandex.app.service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class SubTasksHandler implements HttpHandler  {
    private final TaskManager manager;
    private final Gson gson;

    public SubTasksHandler(TaskManager manager) {
        this.manager = manager;
        gson = new Gson();
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                subTasksGet(exchange);
                break;
            case "POST":
                subTasksPost(exchange);
                break;
            case "DELETE":
                subTasksDelete(exchange);
                break;
        }
    }

    private void subTasksGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/subtasks$", path)) {//Если нет id возвращаем все задачи
            List<SubTask> tasks = manager.getAllSubTasks();
            response = gson.toJson(tasks);
            sendText(exchange, response);

        } else if (Pattern.matches("^/subtasks/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/subtasks/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                SubTask subTask = manager.getSubTask(id);
                if (subTask != null) {
                    response = gson.toJson(subTask);
                    sendText(exchange, response);
                }
                writeResponse(exchange,"Задачи с id = " + pathId + " нет.",404);

            } else {
                writeResponse(exchange,"Некорректный id =  " + pathId,406);
            }
        }
    }

    private void subTasksPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/subtasks$", path)) {//Создаем
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            SubTask subTaskRequest = gson.fromJson(body, SubTask.class);
            SubTask subTaskCreate = manager.create(subTaskRequest);
            if (subTaskCreate != null) {
                System.out.println("Подзадача создана");
                exchange.sendResponseHeaders(201, 0);

            } else {//Код ошибки\
                writeResponse(exchange,"Подзадача пересекается с существующими",406);
            }
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/subtasks/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                SubTask subTaskRequest = gson.fromJson(body, SubTask.class);
                SubTask subTaskCreate = manager.update(subTaskRequest);
                if (subTaskCreate != null) {
                    System.out.println("Подзадача создана");
                    exchange.sendResponseHeaders(201, 0);

                } else {
                    writeResponse(exchange,"Подзадача пересекается с существующими",406);
                }

            } else {
                writeResponse(exchange,"Некорректный id =  " + pathId,406);
            }
        }
    }

    private void subTasksDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/subtasks$", path)) {//Если нет id возвращаем все задачи
            manager.deleteSubTasks();
            exchange.sendResponseHeaders(201, 0);
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/subtasks/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteSubTaskInId(id);
                exchange.sendResponseHeaders(201, 0);

            } else {
                writeResponse(exchange,"Некорректный id =  " + pathId + pathId,406);
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

