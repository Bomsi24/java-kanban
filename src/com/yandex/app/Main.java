package com.yandex.app;

import com.yandex.app.model.Task;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Epic;
import com.yandex.app.service.Manager;

public class Main {

    public static void main(String[] args) {
        //тестирование кода
        Manager manager = new Manager();

        int taskId1 = manager.create(new Task("Купить молоко",
                "Нужно дойти до ближайшего магазина с продуктами и купить молоко"));
        int taskId2 = manager.create(new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор"));
        int epicId1 = manager.create(new Epic("Строительство дома",
                "нужно подготовиться к строительству дома "));
        int subTaskId1 = manager.create(new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                manager.getEpicById(epicId1)));
        int subTaskId2 = manager.create(new SubTask("Найм строителей",
                "Нужно на сайте Ovito найти строителей",
                manager.getEpicById(epicId1)));
        int epicId2 = manager.create(new Epic("Отпуск", "Подготовиться к отпуску"));
        int subTaskId3 = manager.create(new SubTask("Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ",
                manager.getEpicById(epicId2)));

        System.out.println("Задачи: " + manager.getAllTasks().toString());
        System.out.println("Эпики: " + manager.getAllEpics().toString());
        System.out.println("Подзадачи: " + manager.getAllSubTasks().toString());

        manager.update(new Task(manager.getTaskById(taskId1).getName(),
                manager.getTaskById(taskId1).getDescription(), manager.getTaskById(taskId1).statuses[2],
                taskId1));
        manager.update(new Task(manager.getTaskById(taskId2).getName(),
                manager.getTaskById(taskId2).getDescription(), manager.getTaskById(taskId2).statuses[1],
                taskId2));
        manager.update(new Epic(manager.getEpicById(epicId1).getName(),
                manager.getEpicById(epicId1).getDescription(),
                epicId1));
        manager.update(new SubTask(manager.getSubTaskById(subTaskId1).getName(),
                manager.getSubTaskById(subTaskId1).getDescription(), manager.getSubTaskById(subTaskId1).statuses[2],
                subTaskId1, manager.getEpicById(epicId1)));
        manager.update(new SubTask(manager.getSubTaskById(subTaskId2).getName(),
                manager.getSubTaskById(subTaskId2).getDescription(), manager.getSubTaskById(subTaskId2).statuses[1],
                subTaskId2, manager.getEpicById(epicId1)));
        manager.update(new Epic(manager.getEpicById(epicId2).getName(),
                manager.getEpicById(epicId2).getDescription(),
                epicId2));
        manager.update(new SubTask(manager.getSubTaskById(subTaskId3).getName(),
                manager.getSubTaskById(subTaskId3).getDescription(), manager.getSubTaskById(subTaskId3).statuses[2],
                subTaskId3, manager.getEpicById(epicId2)));

        System.out.println("Task1: " + manager.getTaskById(taskId1).toString());
        System.out.println("Task2: " + manager.getTaskById(taskId2).toString());
        System.out.println("Epic1: " + manager.getEpicById(epicId1).toString());
        System.out.println("SubTusk1: " + manager.getSubTaskById(subTaskId1).toString());
        System.out.println("SubTusk2: " + manager.getSubTaskById(subTaskId2).toString());
        System.out.println("Epic2: " + manager.getEpicById(epicId2).toString());
        System.out.println("SubTusk3: " + manager.getSubTaskById(subTaskId3).toString());

        manager.deleteTaskInId(taskId1);
        manager.deleteEpicInId(epicId2);
        manager.deleteSubTaskInId(subTaskId1);

        System.out.println("TaskAll: " + manager.getAllTasks().toString());
        System.out.println("EpicAll: " + manager.getAllEpics().toString());
        System.out.println("SubTaskAll: " + manager.getAllSubTasks().toString());
    }
}
