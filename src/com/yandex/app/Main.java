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
              "Нужно дойти до ближайшего магазина с продуктами и купить молоко"));

        Task task2 = backedTasksManager.create(new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор"));
        Epic epic1 = backedTasksManager.create(new Epic("Строительство дома",

                "нужно подготовиться к строительству дома "));

        SubTask subTask1 = backedTasksManager.create(new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                epic1));

        SubTask subTask2 = backedTasksManager.create(new SubTask("Найм строителей",
                "Нужно на сайте Ovito найти строителей",
                epic1));
                /*
        Epic epic2 = backedTasksManager.create(new Epic("Отпуск", "Подготовиться к отпуску"));
        SubTask subTask3 = backedTasksManager.create(new SubTask("Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ",
                epic2));


         */



        //2024,10,1,12,12
        //"14:00 02.01.24"
       // backedTasksManager.getTask(task1.getId());
        //task1.createTime(100, "14:00 02.01.24");
       // backedTasksManager.update(task1);
       // backedTasksManager.getEpic(epic1.getId());
        //backedTasksManager.getSubTask(subTask1.getId());
       //backedTasksManager.getSubTask(subTask1.getId());
        backedTasksManager.getTask(task1.getId());
        //subTask1.setStatus(Statuses.DONE);
        subTask1.createTime(50, "10:00 06.01.24");
        //backedTasksManager.update(subTask1);
        task2.createTime(20,"07:55 06.01.24");

        task2.createTime(80,"08:00 06.01.24");

       //backedTasksManager.update(subTask2);
      // backedTasksManager.update(task1);
        /*
       backedTasksManager.update(task2);
       backedTasksManager.update(subTask1);
       backedTasksManager.update(subTask1);
       backedTasksManager.update(subTask2);
       backedTasksManager.update(subTask2);

         */
        //System.out.println(epic1.getEndTime());
      // backedTasksManager.getAllTasks();
        //task1.setStatus(Statuses.IN_PROGRESS);
        //backedTasksManager.update(task1);

        //System.out.println(prioritizedTasks);
        //backedTasksManager.update(task1);
        //System.out.println(backedTasksManager.getHistory());
       // System.out.println(backedTasksManager.getPrioritizedTasks());

        FileBackedTasksManager newBackedTasksManager = FileBackedTasksManager.loadFromFile(fileSaveHistory);
        //System.out.println(newBackedTasksManager.getHistory());

    }

}


