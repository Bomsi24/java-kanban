package com.yandex.app.service.manager;

import com.yandex.app.model.*;
import com.yandex.app.service.history.HistoryManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String fileSave;

    public FileBackedTasksManager(String fileSave) {
        this.fileSave = fileSave;
    }


    public static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String taskString = parsingFile(file);
        if (taskString == null) {
            return fileBackedTasksManager;
        }
        String[] lineTask = taskString.split("\n");
        List<Integer> idTask = historyFromString(file);

        for (int i = 1; i < lineTask.length; i++) {
            if (lineTask[i].isEmpty()) {
                break;
            }
            Task task = fileBackedTasksManager.fromString(lineTask[i]);
            if (task != null) {
                for (Integer id : idTask) {
                    if (task.getId() == id) {
                        fileBackedTasksManager.historyManager.add(task);
                    }
                }
            }
        }
        fileBackedTasksManager.save();
        return fileBackedTasksManager;
    }

    protected static String historyToString(HistoryManager manager) {
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

    private static List<Integer> historyFromString(String value) {
        if (value == null) {
            return new ArrayList<>();
        }
        String[] arrayHistory = parsingFile(value).split("\n");
        if (arrayHistory.length < 8) {
            return new ArrayList<>();
        }
        String stringOfHistory = arrayHistory[arrayHistory.length - 1];
        String[] lineHistory = stringOfHistory.split(",");
        List<Integer> historyList = new ArrayList<>();

        for (String line : lineHistory) {
            historyList.add(Integer.parseInt(line));
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

    protected Task fromString(String value) {
        String[] word = value.split(",");
        boolean isTime = Long.parseLong(word[5]) > 0;
        LocalDateTime startTime = LocalDateTime.parse(word[6]);
        long duration = Long.parseLong(word[5]);

        switch (TypeTasks.valueOf(word[1])) {
            case TASK:
                Task task = new Task(word[2], word[4], Long.parseLong(word[5]), startTime);
                task.setId(Integer.parseInt(word[0]));
                create(task);
                task.setStatus(Statuses.valueOf(word[3]));
                if (isTime) {
                    task.createTime(duration, startTime);
                }
                update(task);
                return task;

            case EPIC:
                Epic epic = new Epic(word[2], word[4], Long.parseLong(word[5]), startTime);
                epic.setId(Integer.parseInt(word[0]));
                create(epic);
                epic.setStatus(Statuses.valueOf(word[3]));
                update(epic);
                return epic;

            case SUB_TASK:
                SubTask subTask = new SubTask(word[2], word[4], epics.get(Integer.parseInt(word[7])),
                        Long.parseLong(word[5]), startTime);
                subTask.setId(Integer.parseInt(word[0]));
                create(subTask);
                subTask.setStatus(Statuses.valueOf(word[3]));
                if (isTime) {
                    subTask.createTime(duration, startTime);
                    //updateStatusEpic(epics.get(Integer.parseInt(word[7])));
                }
                update(subTask);
                return subTask;
        }
        return null;
    }

    protected void save() {
        try {
            Writer fileWriter = new FileWriter(fileSave);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String taskFields = "id,type,name,status,description,duration,startTime,epic";
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
    public Task update(Task task) {
        Task taskUpdate = super.update(task);
        save();
        return taskUpdate;
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public SubTask update(SubTask subTask) {
       SubTask subTaskUpdate = super.update(subTask);
        save();
        return subTaskUpdate;
    }
}
