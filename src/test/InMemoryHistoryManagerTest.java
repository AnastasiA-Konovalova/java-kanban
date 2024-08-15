package test;

import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("NameTask1", "DescriptionTask1");
        final List<Task> history = historyManager.getHistory();

        historyManager.add(task1);

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

}