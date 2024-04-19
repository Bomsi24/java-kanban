package com.yandex.app.service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class PrioritizedTaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public PrioritizedTaskHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            String path = exchange.getRequestURI().getPath();
            if (Pattern.matches("^/prioritized$", path)) {
                List<Task> prioritized = manager.getPrioritizedTasks();
                String responseJson = gson.toJson(prioritized);
                byte[] responseByte = responseJson.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, responseByte.length);
                exchange.getResponseBody().write(responseByte);
            } else {
                System.out.println("Неверный путь " + path);
                exchange.sendResponseHeaders(406, 0);
            }

        } else {
            System.out.println("Неверный метод: " + method + ". Нужен метод GET");
            exchange.sendResponseHeaders(406, 0);
        }

    }
}
