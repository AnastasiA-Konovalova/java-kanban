import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;


class ManagersTest {
    @Test
    void getDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager, "TaskManager не должен быть null");
    }

    @Test
    void getDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "HistoryManager не должен быть null");
    }

    @Test
    void differentInstanceOfTaskManager() {
        TaskManager taskManager1 = Managers.getDefault();
        TaskManager taskManager2 = Managers.getDefault();

        assertNotSame(taskManager1, taskManager2, "Методы одинаковы");
    }

    @Test
    void differentInstanceOfHistoryManager() {
        HistoryManager historyManager_1 = Managers.getDefaultHistory();
        HistoryManager historyManager_2 = Managers.getDefaultHistory();

        assertNotSame(historyManager_1, historyManager_2, "Методы одинаковы");
    }
}