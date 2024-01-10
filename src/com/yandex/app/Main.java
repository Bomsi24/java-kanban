package com.yandex.app;

import com.yandex.app.model.Statuses;
import com.yandex.app.model.Task;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Epic;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        //тестирование кода
        TaskManager manager = Managers.getDefault();

        Task task1 = manager.create(new Task("Купить молоко",
                "Нужно дойти до ближайшего магазина с продуктами и купить молоко"));
        Task task2 = manager.create(new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор"));
        Epic epic1 = manager.create(new Epic("Строительство дома",
                "нужно подготовиться к строительству дома "));
        SubTask subTask1 = manager.create(new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                epic1));
        SubTask subTask2 = manager.create(new SubTask("Найм строителей",
                "Нужно на сайте Ovito найти строителей",
                epic1));
        SubTask subTask3 = manager.create(new SubTask("Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ",
                epic1));
        Epic epic2 = manager.create(new Epic("Отпуск", "Подготовиться к отпуску"));


        manager.getEpic(epic1.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getTask(task2.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getEpic(epic1.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getSubTask(subTask2.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getSubTask(subTask1.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getSubTask(subTask3.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getSubTask(subTask3.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getTask(task2.getId());

        manager.deleteSubTaskInId(subTask2.getId());
        System.out.println("История просмотров: " + manager.getHistory());

        System.out.println("История просмотров: " + manager.getHistory());
        manager.getEpic(epic2.getId());

        manager.deleteEpicInId(epic1.getId());
        System.out.println("История просмотров, после удаления эпика: " + manager.getHistory());

        System.out.println("История просмотров: " + manager.getHistory());
        manager.getEpic(epic2.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getTask(task1.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getTask(task1.getId());

        manager.deleteTaskInId(task2.getId());
        System.out.println("История просмотров: " + manager.getHistory());

        System.out.println("История просмотров: " + manager.getHistory());
        manager.getTask(task2.getId());
        System.out.println("История просмотров: " + manager.getHistory());

        manager.deleteEpics();
        System.out.println("История просмотров после удаления: " + manager.getHistory());

        manager.getTask(task1.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getTask(task2.getId());
        System.out.println("История просмотров: " + manager.getHistory());
        manager.deleteTasks();
        System.out.println("История просмотров пустая: " + manager.getHistory());

    }

}
