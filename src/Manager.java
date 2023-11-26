import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int idTask = 0;//идентификатор

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void createTask(Task task, String name, String description) {
        task.name = name;
        task.description = description;
        task.status = task.STATUSES[0];
        task.ID = addId();
        this.tasks.put(task.ID, task);
    }

    public void createEpic(Epic epic, String name, String description) {
        epic.name = name;
        epic.description = description;
        epic.status = epic.STATUSES[0];
        epic.ID = addId();
        this.epics.put(epic.ID, epic);
    }

    public void createSubTask(Epic epic, SubTask subTask, String name, String description) {
        subTask.name = name;
        subTask.description = description;
        subTask.status = subTask.STATUSES[0];
        subTask.ID = addId();
        this.subTasks.put(subTask.ID, subTask);
        epic.setSubTasks(subTask.ID, subTask);
        subTask.setToPlugEpic(epic);

    }

    public ArrayList<String> getAllTasks() {
        ArrayList<String> printTask = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Integer i : tasks.keySet()) {
                Task task = tasks.get(i);
                String nameTask = task.name;
                printTask.add(nameTask);
            }
        } else {
            printTask.add("Задач нет");
            return printTask;
        }
        return printTask;
    }

    public ArrayList<String> getAllEpics() {
        ArrayList<String> printEpic = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (Integer i : epics.keySet()) {
                Epic epic = epics.get(i);
                String nameTask = epic.name;
                printEpic.add(nameTask);
            }
        } else {
            printEpic.add("Задач нет");
            return printEpic;
        }
        return printEpic;
    }

    public ArrayList<String> getAllSubTasks() {
        ArrayList<String> printSubTask = new ArrayList<>();
        if (!subTasks.isEmpty()) {
            for (Integer i : subTasks.keySet()) {
                SubTask subTask = subTasks.get(i);
                String nameTask = subTask.name;
                printSubTask.add(nameTask);
            }
        } else {
            printSubTask.add("Задач нет");
            return printSubTask;
        }
        return printSubTask;
    }

    public HashMap<Integer, Task> deleteTasks() {
        tasks.clear();
        return tasks;
    }

    public HashMap<Integer, Epic> deleteEpics() {
        epics.clear();
        subTasks.clear();// удаляем подзадачи потому что их без эпика несуществует
        return epics;
    }

    public HashMap<Integer, SubTask> deleteSubTasks() {
        subTasks.clear();
        return subTasks;
    }

    public Object getTaskInId(Integer id) {
        if (!tasks.isEmpty()) {
            for (Integer i : tasks.keySet()) {
                if (i.equals(id)) {
                    return (Task) tasks.get(i);
                }
            }
        }
        if (!epics.isEmpty()) {
            for (Integer i : epics.keySet()) {
                if (i.equals(id)) {
                    return (Epic) epics.get(i);
                }
            }
        }
        if (!subTasks.isEmpty()) {
            for (Integer i : subTasks.keySet()) {
                if (i.equals(id)) {
                    return (SubTask) subTasks.get(i);
                }
            }
        }
        return null;
    }

    public Object deleteTaskInId(Integer id) {
        if (!tasks.isEmpty()) {
            for (Integer i : tasks.keySet()) {
                if (i.equals(id)) {
                    return tasks.remove(i);
                }
            }
        }
        if (!epics.isEmpty()) {
            for (Integer i : epics.keySet()) {
                if (i.equals(id)) {
                    return epics.remove(i);
                }
            }
        }
        if (!subTasks.isEmpty()) {
            for (Integer i : subTasks.keySet()) {
                if (i.equals(id)) {
                    return subTasks.remove(i);
                }
            }
        }
        return null;
    }


    public void updateTask(Task task, String status) {
        task.status = status;
        this.tasks.put(task.ID, task);
    }

    public void updateEpic(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.status = epic.STATUSES[0];

        } else {
            int statusNew = 0;
            int statusInProgress = 0;
            int statusDone = 0;
            for (SubTask subTask : epic.getSubTasks().values()) {
                switch (subTask.status) {
                    case "NEW":
                        statusNew++;
                        break;
                    case "IN_PROGRESS":
                        statusInProgress++;
                        break;
                    case "DONE":
                        statusDone++;
                        break;
                }
            }
            if (statusDone == 0 && statusInProgress == 0) {
                epic.status = epic.STATUSES[0];
            } else if (statusNew == 0 && statusInProgress == 0) {
                epic.status = epic.STATUSES[2];
            } else {
                epic.status = epic.STATUSES[1];
            }
        }
        this.epics.put(epic.ID, epic);
    }

    public void updateSubTask(SubTask subTask, String status) {
        subTask.status = status;
        this.subTasks.put(subTask.ID, subTask);
        updateEpic(subTask.getEpic());

    }

    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.addAll(epic.getSubTasks().values());
        return subTasks;
    }

    public int addId() {
        this.idTask++;
        return idTask;
    }
}

