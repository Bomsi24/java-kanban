package com.yandex.app;

import com.yandex.app.model.Task;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Epic;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        //тестирование кода
        TaskManager manager = Managers.getDefault();

        int taskId1 = manager.create(new Task("Купить молоко",
                "Нужно дойти до ближайшего магазина с продуктами и купить молоко"));
        int taskId2 = manager.create(new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор"));
        int epicId1 = manager.create(new Epic("Строительство дома",
                "нужно подготовиться к строительству дома "));
        int subTaskId1 = manager.create(new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                manager.getEpic(epicId1)));
        int subTaskId2 = manager.create(new SubTask("Найм строителей",
                "Нужно на сайте Ovito найти строителей",
                manager.getEpic(epicId1)));
        int epicId2 = manager.create(new Epic("Отпуск", "Подготовиться к отпуску"));
        int subTaskId3 = manager.create(new SubTask("Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ",
                manager.getEpic(epicId2)));

        System.out.println("Задачи: " + manager.getAllTasks().toString());
        System.out.println("Эпики: " + manager.getAllEpics().toString());
        System.out.println("Подзадачи: " + manager.getAllSubTasks().toString());

        manager.update(new Task(manager.getTask(taskId1).getName(),
                manager.getTask(taskId1).getDescription(), Task.Statuses.DONE,
                taskId1));
        manager.update(new Task(manager.getTask(taskId2).getName(),
                manager.getTask(taskId2).getDescription(), Task.Statuses.IN_PROGRESS,
                taskId2));
        manager.update(new Epic(manager.getEpic(epicId1).getName(),
                manager.getEpic(epicId1).getDescription(),
                epicId1));
        manager.update(new SubTask(manager.getSubTask(subTaskId1).getName(),
                manager.getSubTask(subTaskId1).getDescription(), Task.Statuses.DONE,
                subTaskId1, manager.getEpic(epicId1)));
        manager.update(new SubTask(manager.getSubTask(subTaskId2).getName(),
                manager.getSubTask(subTaskId2).getDescription(), Task.Statuses.IN_PROGRESS,
                subTaskId2, manager.getEpic(epicId1)));
        manager.update(new Epic(manager.getEpic(epicId2).getName(),
                manager.getEpic(epicId2).getDescription(),
                epicId2));
        manager.update(new SubTask(manager.getSubTask(subTaskId3).getName(),
                manager.getSubTask(subTaskId3).getDescription(), Task.Statuses.DONE,
                subTaskId3, manager.getEpic(epicId2)));

        System.out.println("Task1: " + manager.getTask(taskId1).toString());
        System.out.println("Последние задачи: " + manager.getHistory());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Последние задачи: " + manager.getHistory());
        System.out.println("Epic1: " + manager.getEpic(epicId1).toString());
        System.out.println("Последние задачи: " + manager.getHistory());
        System.out.println("SubTusk1: " + manager.getSubTask(subTaskId1).toString());
        System.out.println("Последние задачи: " + manager.getHistory());
        System.out.println("SubTusk2: " + manager.getSubTask(subTaskId2).toString());
        System.out.println("Последние задачи: " + manager.getHistory());
        System.out.println("Epic2: " + manager.getEpic(epicId2).toString());
        System.out.println("Последние задачи: " + manager.getHistory());
        System.out.println("SubTusk3: " + manager.getSubTask(subTaskId3).toString());

        manager.deleteTaskInId(taskId1);
        manager.deleteEpicInId(epicId2);
        manager.deleteSubTaskInId(subTaskId1);

        System.out.println("TaskAll: " + manager.getAllTasks().toString());
        System.out.println("EpicAll: " + manager.getAllEpics().toString());
        System.out.println("SubTaskAll: " + manager.getAllSubTasks().toString());

        //Тест на отображение не более 10 задач
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Task2: " + manager.getTask(taskId2).toString());
        System.out.println("Последние задачи: " + manager.getHistory());
    }
}
