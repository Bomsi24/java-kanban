package com.yandex.app;

import com.yandex.app.model.*;
import com.yandex.app.service.manager.FileBackedTasksManager;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;


public class Main {

    public static void main(String[] args) {
        //тестирование кода

        String fileSaveHistory = "save.csv";

        FileBackedTasksManager backedTasksManager = new FileBackedTasksManager(fileSaveHistory);

        Task task1 = backedTasksManager.create(new Task("Купить молоко",
                "Нужно дойти до ближайшего магазина с продуктами и купить молоко", 0, null));

        Task task2 = backedTasksManager.create(new Task("Купить телевизор",
               "Нужно через сайт Ozon заказать телевизор", 50, "10:00 06.01.24"));

        Epic epic1 = backedTasksManager.create(new Epic("Строительство дома",
                "нужно подготовиться к строительству дома ", 0, null));

        SubTask subTask1 = backedTasksManager.create(new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы", epic1, 10, "07:55 06.01.24"
        ));

        SubTask subTask2 = backedTasksManager.create(new SubTask("Найм строителей",
                "Нужно на сайте Ovito найти строителей", epic1, 20, "08:00 06.01.24"
        ));


        System.out.println(backedTasksManager.getPrioritizedTasks());

        FileBackedTasksManager newBackedTasksManager = FileBackedTasksManager.loadFromFile(fileSaveHistory);


    }

}


