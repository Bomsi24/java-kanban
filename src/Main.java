public class Main {

    public static void main(String[] args) {
        //тестирование кода
        Manager manager = new Manager();
        Task task1 = new Task();
        Task task2 = new Task();
        Epic epic1 = new Epic();
        SubTask subTask1 = new SubTask();
        SubTask subTask2 = new SubTask();
        Epic epic2 = new Epic();
        SubTask subTask3 = new SubTask();


        manager.createTask(task1, "Купить молоко",
                "Нужно дойти до ближайшего магазина с продуктами и купить молоко");
        manager.createTask(task2, "Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор");
        manager.createEpic(epic1, "Строительство дома",
                "нужно подготовиться к строительству дома ");
        manager.createSubTask(epic1, subTask1, "Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы");
        manager.createSubTask(epic1, subTask2, "Найм строителей",
                "Нужно на сайте Ovito найти строителей");
        manager.createEpic(epic2, "Отпуск",
                "Подготовиться к отпуску");
        manager.createSubTask(epic2, subTask3, "Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ");

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        manager.updateSubTask(subTask1,"DONE");
        System.out.println(epic1.status);
        manager.updateSubTask(subTask2,"DONE");
        System.out.println(epic1.status);
    }
}
