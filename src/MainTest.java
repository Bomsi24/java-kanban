import com.yandex.app.model.Epic;
import com.yandex.app.model.StartTimeComparator;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;

import java.util.Set;
import java.util.TreeSet;

public class MainTest {
    public static void main(String[] args) {
        Set<Task> prioritizedTasks = new TreeSet<>(new StartTimeComparator());

        Task task1 = new Task("Купить молоко",
                "Нужно дойти до ближайшего магазина с продуктами и купить молоко");
        Epic epic1 = new Epic("Строительство дома",
                "нужно подготовиться к строительству дома ");
        Task task2 = new Task("Купить телевизор",
                "Нужно через сайт Ozon заказать телевизор");
        SubTask subTask1 = new SubTask("Заказ материалов",
                "Нужно на сайте Леруа мерлен заказать материалы",
                epic1);

        subTask1.createTime(50, "10:00 06.01.24");
        task2.createTime(20,"07:55 06.01.24");
        task2.createTime(80,"08:00 06.01.24");

        prioritizedTasks.add(subTask1);
        prioritizedTasks.add(task1);
        prioritizedTasks.add(task2);

        System.out.println(prioritizedTasks);
    }
}
