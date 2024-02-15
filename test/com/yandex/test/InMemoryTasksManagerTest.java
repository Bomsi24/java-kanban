package com.yandex.test;

import com.yandex.app.model.Task;
import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.history.InMemoryHistoryManager;
import com.yandex.app.service.manager.InMemoryTaskManager;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    public void createInMemoryTasksManagerTest() {
        this.manager = new InMemoryTaskManager();
    }


}
