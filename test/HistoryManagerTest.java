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
        Task task_1 = new Task("NameTask_1", "DescriptionTask_1");
        Epic epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        taskManager.createTask(task_1);
        taskManager.createEpic(epic_1);
        historyManager.add(task_1);
        historyManager.add(epic_1);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "Список не должен быть пуст");
    }
}