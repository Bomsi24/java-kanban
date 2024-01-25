package com.yandex.app.service.manager;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.model.TypeTasks;
import com.yandex.app.service.history.HistoryManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final File file;
    public FileBackedTasksManager(File file) {
        this.file = file;

    }

     public static void main(String[] args){

        File file1 = new File("save.csv");

       FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file1);

       fileBackedTasksManager.create(new Task("Купить молоко",
               "Нужно дойти до ближайшего магазина с продуктами и купить молоко"));


    }



    static FileBackedTasksManager loadFromFile(final File file){

        return new FileBackedTasksManager(file);
    }

    static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        StringBuilder taskString = new StringBuilder();
        for (Task task : tasks) {
            taskString.append(task.toString() + "\n");
        }
        taskString.append( " " + "\n");
        for(Task taskId : tasks) {
         taskString.append(taskId.getId());
        }

        return taskString.toString();

    }

    static List<Integer> historyFromString(String value) throws IOException {
        String content = Files.readString(Paths.get(value));
        String[] str = content.split("\n");
        String stringOfHistory = str[str.length - 1];
        List<Integer> historyList = new ArrayList<>();
        for (int i = 0; i < stringOfHistory.length(); i++) {
            historyList.add(stringOfHistory.indexOf(i));
        }
        return historyList;

    }

    public Task fromString(String value) {
        String[] word = value.split(",");

        switch (TypeTasks.valueOf(word[1])) {
            case TASK:
                Task task = new Task(word[2], word[4]);
                task.setId(Integer.parseInt(word[0]));
                return create(task);

            case EPIC:
                Epic epic = new Epic(word[2], word[4]);
                epic.setId(Integer.parseInt(word[0]));
                return create(epic);

            case SUB_TASK:
                SubTask subTask = new SubTask(word[2], word[4],
                        getEpic(Integer.parseInt(word[5])));
                subTask.setId(Integer.parseInt(word[0]));
                return create(subTask);
        }
        return null;

    }

     void save() throws IOException {
        Writer fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        String taskFields = "id,type,name,status,description,epic" + '\n';
        bufferedWriter.write(taskFields);
        bufferedWriter.write(historyToString(historyManager));
        fileWriter.close();
        bufferedWriter.close();

    }


    @Override
    public Task create(Task task) {
        super.create(task);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при создании задачи");
        }

        return task;
    }

    @Override
    public Epic create(Epic epic) {
        super.create(epic);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при создании эпика");
        }
        return epic;
    }

    @Override
    public SubTask create(SubTask subTask) {
        super.create(subTask);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при создании подзадачи");
        }
        return subTask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при удалении всех задач");
        }
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при удалении всех эпиков");
        }
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при удалении всех подзадач");
        }
    }

    @Override
    public Task getTask(Integer id) {
        Task task = super.getTask(id);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при получении задачи");
        }
        return null;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при получении эпика");
        }
        return epic;
    }

    @Override
    public SubTask getSubTask(Integer id) {
        SubTask subTask = super.getSubTask(id);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при получении подзадачи");
        }
        return subTask;
    }

    @Override
    public void deleteTaskInId(Integer id) {
        super.deleteTaskInId(id);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при удалении задачи по id");
        }
    }

    @Override
    public void deleteEpicInId(Integer id) {
        super.deleteEpicInId(id);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при удалении эпика по id");
        }
    }

    @Override
    public void deleteSubTaskInId(Integer id) {
        super.deleteSubTaskInId(id);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при удалении подзадачи по id");
        }
    }

    @Override
    public void update(Task task) {
        super.update(task);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при обновлении задачи");
        }
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при обновлении эпика");
        }
    }

    @Override
    public void update(SubTask subTask) {
        super.update(subTask);
        try {
            save();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при обновлении подзадачи");
        }
    }
}
