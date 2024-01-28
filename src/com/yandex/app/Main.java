package com.yandex.app;

import com.yandex.app.model.Statuses;
import com.yandex.app.model.Task;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Epic;
import com.yandex.app.service.manager.FileBackedTasksManager;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;


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
        Epic epic2 = backedTasksManager.create(new Epic("Отпуск", "Подготовиться к отпуску"));
        SubTask subTask3 = backedTasksManager.create(new SubTask("Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ",
                epic2));


        backedTasksManager.getTask(task1.getId());
        backedTasksManager.getEpic(epic1.getId());
        backedTasksManager.getSubTask(subTask1.getId());
        backedTasksManager.getSubTask(subTask1.getId());
        backedTasksManager.getTask(task1.getId());
        subTask1.setStatus(Statuses.DONE);
        backedTasksManager.update(subTask1);
        task1.setStatus(Statuses.IN_PROGRESS);
        backedTasksManager.update(task1);
        System.out.println(backedTasksManager.getHistory());

        FileBackedTasksManager newBackedTasksManager = FileBackedTasksManager.loadFromFile(fileSaveHistory);
        System.out.println(newBackedTasksManager.getHistory());

    }

}


