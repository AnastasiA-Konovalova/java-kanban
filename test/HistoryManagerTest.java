package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {

    @Test
    void saveOldTask() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("NameTask1", "DescriptionTask1");
        Epic epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        historyManager.add(task1);
        historyManager.add(epic1);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "Список не должен быть пуст");
    }
}