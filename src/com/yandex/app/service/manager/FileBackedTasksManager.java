package com.yandex.app.service.manager;

import com.yandex.app.model.*;
import com.yandex.app.service.history.HistoryManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final String fileSave;

    public FileBackedTasksManager(String fileSave) {
        this.fileSave = fileSave;
    }


    public static void main(String[] args) {

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
        SubTask subTask3 = backedTasksManager.create(new SubTask("Выбор места для отпуска",
                "Нужно в турфирме Манго выбрать в какую страну лететь отдыхать ",
                epic1));
        Epic epic2 = backedTasksManager.create(new Epic("Отпуск", "Подготовиться к отпуску"));


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

    static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String taskString = parsingFile(file);
        String[] lineTask = taskString.split("\n");
        List<Integer> idTask = historyFromString(file);

        for (int i = 1; i < lineTask.length; i++) {
            if (lineTask[i].isEmpty()) {
                break;
            }
            Task task = fileBackedTasksManager.fromString(lineTask[i]);
            for (Integer id : idTask) {
                if (id == task.getId()) {
                    fileBackedTasksManager.historyManager.add(task);
                }
            }
        }
        fileBackedTasksManager.save();
        return fileBackedTasksManager;
    }

    static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        StringBuilder taskString = new StringBuilder();
        taskString.append("\n");
        for (Task task : tasks) {
            taskString.append(task.getId());
            taskString.append(",");
        }
        taskString.deleteCharAt(taskString.length() - 1);
        return taskString.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] arrayHistory = parsingFile(value).split("\n");
        String stringOfHistory = arrayHistory[arrayHistory.length - 1];
        String[] lineHistory = stringOfHistory.split(",");
        List<Integer> historyList = new ArrayList<>();

        for (int i = 0; i < lineHistory.length; i++) {
            historyList.add(Integer.parseInt(lineHistory[i]));
        }
        return historyList;
    }

    private static String parsingFile(String path) {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private Task fromString(String value) {
        String[] word = value.split(",");

        switch (TypeTasks.valueOf(word[1])) {
            case TASK:
                Task task = new Task(word[2], word[4]);
                task.setId(Integer.parseInt(word[0]));
                create(task);
                task.setStatus(Statuses.valueOf(word[3]));
                update(task);
                return task;

            case EPIC:
                Epic epic = new Epic(word[2], word[4]);
                epic.setId(Integer.parseInt(word[0]));
                create(epic);
                epic.setStatus(Statuses.valueOf(word[3]));
                update(epic);
                return epic;

            case SUB_TASK:
                SubTask subTask = new SubTask(word[2], word[4],
                        epics.get(Integer.parseInt(word[5])));
                subTask.setId(Integer.parseInt(word[0]));
                create(subTask);
                subTask.setStatus(Statuses.valueOf(word[3]));
                update(subTask);
                return subTask;
        }
        return null;
    }

    void save() {
        try {
            Writer fileWriter = new FileWriter(fileSave);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String taskFields = "id,type,name,status,description,epic";
            bufferedWriter.write(taskFields);

            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    bufferedWriter.write(task.toString());
                }
            }
            if (!epics.isEmpty()) {
                for (Epic epic : epics.values()) {
                    bufferedWriter.write(epic.toString());
                }
            }
            if (!subTasks.isEmpty()) {
                for (SubTask subTask : subTasks.values()) {
                    bufferedWriter.write(subTask.toString());
                }
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(historyToString(historyManager));
            bufferedWriter.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при создании задачи");
        }
    }


    @Override
    public Task create(Task task) {
        super.create(task);
        save();
        return task;
    }

    @Override
    public Epic create(Epic epic) {
        super.create(epic);
        save();
        return epic;
    }

    @Override
    public SubTask create(SubTask subTask) {
        super.create(subTask);
        save();
        return subTask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public Task getTask(Integer id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(Integer id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public void deleteTaskInId(Integer id) {
        super.deleteTaskInId(id);
        save();
    }

    @Override
    public void deleteEpicInId(Integer id) {
        super.deleteEpicInId(id);
        save();
    }

    @Override
    public void deleteSubTaskInId(Integer id) {
        super.deleteSubTaskInId(id);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(SubTask subTask) {
        super.update(subTask);
        save();
    }
}
