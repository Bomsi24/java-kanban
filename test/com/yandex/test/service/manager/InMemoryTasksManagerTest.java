package com.yandex.test.service.manager;

import com.yandex.app.service.manager.InMemoryTaskManager;

import org.junit.jupiter.api.BeforeEach;


public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createInMemoryTasksManagerTest() {
        this.manager = new InMemoryTaskManager();
    }
}
