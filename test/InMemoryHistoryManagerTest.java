import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private InMemoryHistoryManager inMemoryHistoryManager;
    private Task task_1;
    private Task task_2;
    private Epic epic_1;
    private Subtask subtask_1;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task_1 = new Task("NameTask_1", "DescriptionTask_1");
        task_2 = new Task("NameTask_2", "DescriptionTask_2");
        epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1);
    }

    @Test
    void testAddTasksInHistory() {
        historyManager.add(task_1);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "Размер списка верный.");
    }

    @Test
    void testHistoryIsEmpty() {
        //Пустая история задач.
    }

    @Test
    void testAddTasksInHistoryTwice() {
        task_1.setId(1);
        task_2.setId(task_1.getId());

        historyManager.add(task_1);
        historyManager.add(task_2);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertSame(history.get(0), task_2);
        assertEquals(1, history.size());
        assertTrue(history.contains(task_2));
    }

    @Test
    void testAddMoreThen10Tasks() {
        Task task_1 = new Task("NameTask_1", "DescriptionTask_1");
        Task task_2 = new Task("NameTask_2", "DescriptionTask_2");
        Task task_3 = new Task("NameTask_3", "DescriptionTask_3");
        Task task_4 = new Task("NameTask_4", "DescriptionTask_4");
        Task task_5 = new Task("NameTask_5", "DescriptionTask_5");
        Task task_6 = new Task("NameTask_6", "DescriptionTask_6");
        Epic epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        Epic epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");
        Epic epic_3 = new Epic("NameEpic_3", "DescriptionEpic_3");
        Epic epic_4 = new Epic("NameEpic_4", "DescriptionEpic_4");
        Epic epic_5 = new Epic("NameEpic_5", "DescriptionEpic_5");
        Subtask subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1);
        Subtask subtask_2 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_3);
        task_1.setId(1);
        task_2.setId(2);
        task_3.setId(3);
        task_4.setId(4);
        task_5.setId(5);
        task_6.setId(6);
        epic_1.setId(7);
        epic_2.setId(8);
        epic_3.setId(9);
        epic_4.setId(10);
        epic_5.setId(11);
        subtask_1.setId(12);
        subtask_2.setId(13);

        historyManager.add(task_1);
        historyManager.add(task_2);
        historyManager.add(task_3);
        historyManager.add(task_4);
        historyManager.add(task_5);
        historyManager.add(task_6);
        historyManager.add(epic_1);
        historyManager.add(epic_2);
        historyManager.add(epic_3);
        historyManager.add(epic_4);
        historyManager.add(epic_5);
        historyManager.add(subtask_1);
        historyManager.add(subtask_2);

        assertEquals(13, historyManager.getHistory().size());
    }

    @Test
    void testLinkAddLastNotNull() {
        inMemoryHistoryManager.linkAddLast(task_1);

        assertNotNull(inMemoryHistoryManager.getHead());
        assertNotNull(inMemoryHistoryManager.getTail());
        assertEquals(task_1, inMemoryHistoryManager.getHead().data);
        assertEquals(task_1, inMemoryHistoryManager.getTail().data);
    }

    @Test
    void testLinkAddLastCheckInteraction() {
        inMemoryHistoryManager.linkAddLast(task_1);
        inMemoryHistoryManager.linkAddLast(task_2);

        assertEquals(task_2, inMemoryHistoryManager.getHead().next.data);
        assertNull(inMemoryHistoryManager.getHead().prev);
        assertEquals(task_1, inMemoryHistoryManager.getHead().data);
        assertNull(inMemoryHistoryManager.getTail().next);
    }

    @Test
    void testRemoveFirstNode() {
        task_1.setId(1);
        task_2.setId(2);
        epic_1.setId(3);
        subtask_1.setId(4);
        inMemoryHistoryManager.linkAddLast(task_1);
        inMemoryHistoryManager.linkAddLast(task_2);
        inMemoryHistoryManager.linkAddLast(epic_1);
        inMemoryHistoryManager.linkAddLast(subtask_1);

        inMemoryHistoryManager.remove(task_1.getId());

        assertEquals(task_2, inMemoryHistoryManager.getHead().data);
        assertEquals(subtask_1, inMemoryHistoryManager.getTail().data);
    }

    @Test
    void testRemoveLastNode() {
        task_1.setId(1);
        task_2.setId(2);
        epic_1.setId(3);
        subtask_1.setId(4);
        inMemoryHistoryManager.linkAddLast(task_1);
        inMemoryHistoryManager.linkAddLast(task_2);
        inMemoryHistoryManager.linkAddLast(epic_1);
        inMemoryHistoryManager.linkAddLast(subtask_1);

        inMemoryHistoryManager.remove(subtask_1.getId());

        assertEquals(epic_1, inMemoryHistoryManager.getTail().data);
        assertEquals(task_1, inMemoryHistoryManager.getHead().data);
    }

    @Test
    void testRemoveMiddleNode() {
        task_1.setId(1);
        task_2.setId(2);
        epic_1.setId(3);
        subtask_1.setId(4);
        inMemoryHistoryManager.linkAddLast(task_1);
        inMemoryHistoryManager.linkAddLast(task_2);
        inMemoryHistoryManager.linkAddLast(epic_1);
        inMemoryHistoryManager.linkAddLast(subtask_1);

        inMemoryHistoryManager.remove(epic_1.getId());

        assertEquals(task_1, inMemoryHistoryManager.getHead().data);
        assertEquals(subtask_1, inMemoryHistoryManager.getTail().data);
    }

    @Test
    void testRemoveSingleNode() {
        task_1.setId(1);
        inMemoryHistoryManager.linkAddLast(task_1);

        inMemoryHistoryManager.remove(task_1.getId());

        assertNull(inMemoryHistoryManager.getHead());
        assertNull(inMemoryHistoryManager.getTail());
    }

    @Test
    void testGetHistory() {
        task_1.setId(1);
        task_2.setId(2);
        epic_1.setId(3);
        subtask_1.setId(4);
        inMemoryHistoryManager.linkAddLast(task_1);
        inMemoryHistoryManager.linkAddLast(task_2);
        inMemoryHistoryManager.linkAddLast(epic_1);
        inMemoryHistoryManager.linkAddLast(subtask_1);

        List<Task> historyList = inMemoryHistoryManager.getHistory();

        assertEquals(4, historyList.size());
        assertEquals(List.of(task_1, task_2, epic_1, subtask_1), historyList);
    }

    @Test
    void testGetHistoryAfterRemove() {
        task_1.setId(1);
        task_2.setId(2);
        epic_1.setId(3);
        subtask_1.setId(4);
        inMemoryHistoryManager.linkAddLast(task_1);
        inMemoryHistoryManager.linkAddLast(task_2);
        inMemoryHistoryManager.linkAddLast(epic_1);
        inMemoryHistoryManager.linkAddLast(subtask_1);

        inMemoryHistoryManager.remove(task_1.getId());
        List<Task> historyList = inMemoryHistoryManager.getHistory();

        assertEquals(3, historyList.size());
        assertEquals(List.of(task_2, epic_1, subtask_1), historyList);
    }
}