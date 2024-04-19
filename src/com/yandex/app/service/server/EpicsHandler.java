package com.yandex.app.service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicsHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
        gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                epicsGet(exchange);
                break;
            case "POST":
                epicsPost(exchange);
                break;
            case "DELETE":
                epicsDelete(exchange);
                break;
        }
    }

    private void epicsGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/epics$", path)) {//Если нет id возвращаем все задачи
            List<Epic> epics = manager.getAllEpics();
                response = gson.toJson(epics);
                sendText(exchange, response);


        } else if (Pattern.matches("^/epics/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/epics/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                Epic epic = manager.getEpic(id);
                if (epic != null) {
                    response = gson.toJson(epic);
                    sendText(exchange, response);
                }
                writeResponse(exchange,"Задачи с id = " + pathId + " нет.",404);

            } else {
                writeResponse(exchange,"Некорректный id =  " + pathId,406);
            }
        }
    }

    private void epicsPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/epics$", path)) {//Создаем
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epicRequest = gson.fromJson(body, Epic.class);
            Epic epicCreate = manager.create(epicRequest);
            if (epicCreate != null) {
                System.out.println("Задача создана");
                exchange.sendResponseHeaders(201, 0);

            } else {
                writeResponse(exchange,"Задача пересекается с существующими",406);
            }
        } else if (Pattern.matches("^/epics/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/epics/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic epicRequest = gson.fromJson(body, Epic.class);
                manager.update(epicRequest);
                System.out.println("Задача создана");
                exchange.sendResponseHeaders(201, 0);


            } else {
                writeResponse(exchange,"Некорректный id =  " + pathId,406);
            }
        }
    }

    private void epicsDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/epics$", path)) {//Если нет id возвращаем все задачи
            manager.deleteEpics();
            exchange.sendResponseHeaders(201, 0);
        } else if (Pattern.matches("^/epics/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/epics/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteTaskInId(id);
                exchange.sendResponseHeaders(201, 0);

            } else {
                writeResponse(exchange,"Некорректный id =  " + pathId,406);
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
