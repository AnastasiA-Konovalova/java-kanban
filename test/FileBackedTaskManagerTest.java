import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import save.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    File tmpFile;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;

    @BeforeEach
    void setUp() {
        task1 = new Task("NameTask1", "DescriptionTask1");
        task2 = new Task("NameTask2", "DescriptionTask2");
        epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic1);
    }

    @Test
    public void testSaveAndLoadFile() throws IOException {
        tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager inMemoryTaskManager = new FileBackedTaskManager(tmpFile.toString());
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubtask(subtask1);
        List<Task> taskList1 = inMemoryTaskManager.getTaskList();
        List<Epic> epicList1 = inMemoryTaskManager.getEpicList();
        List<Subtask> subtaskList1 = inMemoryTaskManager.getSubtaskList();

        inMemoryTaskManager.save();

        FileBackedTaskManager taskManager1 = inMemoryTaskManager.loadFromFile(tmpFile);
        List<Task> taskList2 = taskManager1.getTaskList();
        List<Epic> epicList2 = taskManager1.getEpicList();
        List<Subtask> subtaskList2 = taskManager1.getSubtaskList();

        assertEquals(taskList1.size(), taskList2.size());
        assertEquals(epicList1.size(), epicList2.size());
        assertEquals(subtaskList2.size(), subtaskList2.size());

        for (int i = 0; i < taskList1.size(); i++) {
            assertEquals(taskList1.get(i), taskList2.get(i));
        }

        for (int i = 0; i < epicList1.size(); i++) {
            assertEquals(epicList1.get(i), epicList2.get(i));
        }

        for (int i = 0; i < subtaskList1.size(); i++) {
            assertEquals(subtaskList1.get(i), subtaskList2.get(i));
        }
    }

    @Test
    public void testSaveAndLoadEmptyFile() throws IOException {
        tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager emptyFile = new FileBackedTaskManager(tmpFile.toString());

        emptyFile.save();

        assertTrue(emptyFile.getTaskList().isEmpty());
        assertTrue(emptyFile.getEpicList().isEmpty());
        assertTrue(emptyFile.getSubtaskList().isEmpty());

        emptyFile = FileBackedTaskManager.loadFromFile(tmpFile);

        assertTrue(emptyFile.getTaskList().isEmpty());
        assertTrue(emptyFile.getEpicList().isEmpty());
        assertTrue(emptyFile.getSubtaskList().isEmpty());
    }
}