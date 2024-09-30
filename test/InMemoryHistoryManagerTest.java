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
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task1 = new Task("NameTask1", "DescriptionTask1");
        task2 = new Task("NameTask2", "DescriptionTask2");
        epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic1);
    }

    @Test
    void testAddTasksInHistory() {
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "Размер списка верный.");
    }

    @Test
    void testAddTasksInHistoryTwice() {
        task1.setId(1);
        task2.setId(task1.getId());

        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertSame(history.get(0), task2);
        assertEquals(1, history.size());
        assertTrue(history.contains(task2));
    }

    @Test
    void testAddMoreThen10Tasks() {
        Task task1 = new Task("NameTask1", "DescriptionTask1");
        Task task2 = new Task("NameTask2", "DescriptionTask2");
        Task task3 = new Task("NameTask3", "DescriptionTask3");
        Task task4 = new Task("NameTask4", "DescriptionTask4");
        Task task5 = new Task("NameTask5", "DescriptionTask5");
        Task task6 = new Task("NameTask6", "DescriptionTask6");
        Epic epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        Epic epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        Epic epic3 = new Epic("NameEpic3", "DescriptionEpic3");
        Epic epic4 = new Epic("NameEpic4", "DescriptionEpic4");
        Epic epic5 = new Epic("NameEpic5", "DescriptionEpic5");
        Subtask subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic1);
        Subtask subtask2 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic3);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        task4.setId(4);
        task5.setId(5);
        task6.setId(6);
        epic1.setId(7);
        epic2.setId(8);
        epic3.setId(9);
        epic4.setId(10);
        epic5.setId(11);
        subtask1.setId(12);
        subtask2.setId(13);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(epic3);
        historyManager.add(epic4);
        historyManager.add(epic5);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        assertEquals(13, historyManager.getHistory().size());
    }

    @Test
    void testLinkAddLastNotNull() {
        inMemoryHistoryManager.linkAddLast(task1);

        assertNotNull(inMemoryHistoryManager.getHead());
        assertNotNull(inMemoryHistoryManager.getTail());
        assertEquals(task1, inMemoryHistoryManager.getHead().data);
        assertEquals(task1, inMemoryHistoryManager.getTail().data);
        assertTrue(inMemoryHistoryManager.getMapOfTasks().containsKey(task1.getId()));
    }

    @Test
    void testLinkAddLastCheckInteraction() {
        inMemoryHistoryManager.linkAddLast(task1);
        inMemoryHistoryManager.linkAddLast(task2);

        assertEquals(task2, inMemoryHistoryManager.getHead().next.data);
        assertNull(inMemoryHistoryManager.getHead().prev);
        assertEquals(task1, inMemoryHistoryManager.getHead().data);
        assertNull(inMemoryHistoryManager.getTail().next);
    }

    @Test
    void testRemoveFirstNode() {
        task1.setId(1);
        task2.setId(2);
        epic1.setId(3);
        subtask1.setId(4);
        inMemoryHistoryManager.linkAddLast(task1);
        inMemoryHistoryManager.linkAddLast(task2);
        inMemoryHistoryManager.linkAddLast(epic1);
        inMemoryHistoryManager.linkAddLast(subtask1);

        inMemoryHistoryManager.remove(task1.getId());

        assertEquals(task2, inMemoryHistoryManager.getHead().data);
        assertEquals(subtask1, inMemoryHistoryManager.getTail().data);
        assertEquals(3, inMemoryHistoryManager.getMapOfTasks().size());
    }

    @Test
    void testRemoveLastNode() {
        task1.setId(1);
        task2.setId(2);
        epic1.setId(3);
        subtask1.setId(4);
        inMemoryHistoryManager.linkAddLast(task1);
        inMemoryHistoryManager.linkAddLast(task2);
        inMemoryHistoryManager.linkAddLast(epic1);
        inMemoryHistoryManager.linkAddLast(subtask1);

        inMemoryHistoryManager.remove(subtask1.getId());

        assertEquals(epic1, inMemoryHistoryManager.getTail().data);
        assertEquals(task1, inMemoryHistoryManager.getHead().data);
        assertEquals(3, inMemoryHistoryManager.getMapOfTasks().size());
    }

    @Test
    void testRemoveMiddleNode() {
        task1.setId(1);
        task2.setId(2);
        epic1.setId(3);
        subtask1.setId(4);
        inMemoryHistoryManager.linkAddLast(task1);
        inMemoryHistoryManager.linkAddLast(task2);
        inMemoryHistoryManager.linkAddLast(epic1);
        inMemoryHistoryManager.linkAddLast(subtask1);

        inMemoryHistoryManager.remove(epic1.getId());

        assertEquals(task1, inMemoryHistoryManager.getHead().data);
        assertEquals(subtask1, inMemoryHistoryManager.getTail().data);
        assertEquals(3, inMemoryHistoryManager.getMapOfTasks().size());
    }

    @Test
    void testRemoveSingleNode() {
        task1.setId(1);
        inMemoryHistoryManager.linkAddLast(task1);

        inMemoryHistoryManager.remove(task1.getId());

        assertNull(inMemoryHistoryManager.getHead());
        assertNull(inMemoryHistoryManager.getTail());
        assertEquals(0, inMemoryHistoryManager.getMapOfTasks().size());
    }

    @Test
    void testGetHistory() {
        task1.setId(1);
        task2.setId(2);
        epic1.setId(3);
        subtask1.setId(4);
        inMemoryHistoryManager.linkAddLast(task1);
        inMemoryHistoryManager.linkAddLast(task2);
        inMemoryHistoryManager.linkAddLast(epic1);
        inMemoryHistoryManager.linkAddLast(subtask1);

        List<Task> historyList = inMemoryHistoryManager.getHistory();

        assertEquals(4, historyList.size());
        assertEquals(List.of(task1, task2, epic1, subtask1), historyList);
    }

    @Test
    void testGetHistoryAfterRemove() {
        task1.setId(1);
        task2.setId(2);
        epic1.setId(3);
        subtask1.setId(4);
        inMemoryHistoryManager.linkAddLast(task1);
        inMemoryHistoryManager.linkAddLast(task2);
        inMemoryHistoryManager.linkAddLast(epic1);
        inMemoryHistoryManager.linkAddLast(subtask1);

        inMemoryHistoryManager.remove(task1.getId());
        List<Task> historyList = inMemoryHistoryManager.getHistory();

        assertEquals(3, historyList.size());
        assertEquals(List.of(task2, epic1, subtask1), historyList);
    }
}