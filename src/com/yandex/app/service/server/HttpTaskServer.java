package com.yandex.app.service.server;

import com.sun.net.httpserver.HttpServer;
import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int PORT = 8080;
    private TaskManager taskManager;
    private final HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubTasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryTaskHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTaskHandler(taskManager));
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();

        //httpTaskServer.stop();

    }

    public void start() {
        System.out.println("Старт сервера на порту " + PORT);
        server.start();

    }

    public void stop() {
        System.out.println("Остановка сервера на порту " + PORT);
        server.stop(0);
    }


}
