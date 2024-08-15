package test;


import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ManagersTest {
    @Test
    void getDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager, "TaskManager не должен быть null");
        assertTrue(taskManager instanceof InMemoryTaskManager, "taskManager должен instanceof InMemoryTaskManager");
    }

    @Test
    void getDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "HistoryManager не должен быть null");
        assertTrue(historyManager instanceof InMemoryHistoryManager, "historyManager должен instanceof InMemoryHistoryManager");
    }

    @Test
    void differentInstanceOfTaskManager() {
        TaskManager taskManager1 = Managers.getDefault();
        TaskManager taskManager2 = Managers.getDefault();

        assertNotSame(taskManager1, taskManager2, "Методы одинаковы");
    }

    @Test
    void differentInstanceOfHistoryManager() {
        HistoryManager historyManager1 = Managers.getDefaultHistory();
        HistoryManager historyManager2 = Managers.getDefaultHistory();

        assertNotSame(historyManager1, historyManager2, "Методы одинаковы");
    }
}