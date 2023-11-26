import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void setSubTasks(int id, SubTask subTasks) {
        this.subTasks.put(id, subTasks);
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
}

