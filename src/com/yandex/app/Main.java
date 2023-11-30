package com.yandex.app;

import com.yandex.app.model.Task;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Epic;
import com.yandex.app.service.Manager;

public class Main {

    public static void main(String[] args) {
        //тестирование кода
        Manager manager = new Manager();

        int task1 = manager.create(new Task("Купить молоко",
                "Нужно дойти до ближайшего магазина с продуктами и купить молоко"));
        int task2 = manager.create(new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор"));
        int epic1 = manager.create(new Epic("Строительство дома",
                "нужно подготовиться к строительству дома "));
        int subTask1 = manager.create(new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                manager.getEpicById(epic1)));
        int subTask2 = manager.create(new SubTask("Найм строителей",
                "Нужно на сайте Ovito найти строителей",
                manager.getEpicById(epic1)));
        int epic2 = manager.create(new Epic("Отпуск", "Подготовиться к отпуску"));
        int subTask3 = manager.create(new SubTask("Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ",
                manager.getEpicById(epic2)));

        System.out.println("Задачи: " + manager.getAllTasks().toString());
        System.out.println("Эпики: " + manager.getAllEpics().toString());
        System.out.println("Подзадачи: " + manager.getAllSubTasks().toString());

        manager.update(new Task(manager.getTaskById(task1).getName(),
                manager.getTaskById(task1).getDescription(), manager.getTaskById(task1).statuses[2],
                task1));
        manager.update(new Task(manager.getTaskById(task2).getName(),
                manager.getTaskById(task2).getDescription(), manager.getTaskById(task2).statuses[1],
                task2));
        manager.update(new Epic(manager.getEpicById(epic1).getName(),
                manager.getEpicById(epic1).getDescription(),
                epic1));
        manager.update(new SubTask(manager.getSubTaskById(subTask1).getName(),
                manager.getSubTaskById(subTask1).getDescription(), manager.getSubTaskById(subTask1).statuses[2],
                subTask1, manager.getEpicById(epic1)));
        manager.update(new SubTask(manager.getSubTaskById(subTask2).getName(),
                manager.getSubTaskById(subTask2).getDescription(), manager.getSubTaskById(subTask2).statuses[1],
                subTask2, manager.getEpicById(epic1)));
        manager.update(new Epic(manager.getEpicById(epic2).getName(),
                manager.getEpicById(epic2).getDescription(),
                epic2));
        manager.update(new SubTask(manager.getSubTaskById(subTask3).getName(),
                manager.getSubTaskById(subTask3).getDescription(), manager.getSubTaskById(subTask3).statuses[2],
                subTask3, manager.getEpicById(epic2)));

        System.out.println("Task1: " + manager.getTaskById(task1).toString());
        System.out.println("Task2: " + manager.getTaskById(task2).toString());
        System.out.println("Epic1: " + manager.getEpicById(epic1).toString());
        System.out.println("SubTusk1: " + manager.getSubTaskById(subTask1).toString());
        System.out.println("SubTusk2: " + manager.getSubTaskById(subTask2).toString());
        System.out.println("Epic2: " + manager.getEpicById(epic2).toString());
        System.out.println("SubTusk3: " + manager.getSubTaskById(subTask3).toString());

        manager.deleteTaskInId(task1);
        manager.deleteEpicInId(epic2);
        manager.deleteSubTaskInId(subTask1);

        System.out.println("TaskAll: " + manager.getAllTasks().toString());
        System.out.println("EpicAll: " + manager.getAllEpics().toString());
        System.out.println("SubTaskAll: " + manager.getAllSubTasks().toString());
    }
}
