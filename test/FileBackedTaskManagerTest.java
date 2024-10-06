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
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        task1 = new Task("NameTask1", "DescriptionTask1");
        task2 = new Task("NameTask2", "DescriptionTask2");
        epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic1);
        subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", epic1);
    }

    @Test
    public void testSaveAndLoadTasks() throws IOException {
        tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager inMemoryTaskManager = new FileBackedTaskManager(tmpFile.toString());

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        inMemoryTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        List<Task> taskList1 = inMemoryTaskManager.getTaskList();
        List<Task> taskList2 = actualFileManager.getTaskList();

        assertEquals(taskList1.size(), taskList2.size());
        // NikolaiDem: Что здесь проверяется?
        //что размер первоначального листа равен размеру скаченного, т.е. все эл-ты были загружены в файл

        for (int i = 0; i < taskList1.size(); i++) {
            assertEquals(taskList1.get(i), taskList2.get(i));
        }
    }

    @Test
    public void testSaveAndLoadEpics() throws IOException {
        tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager inMemoryTaskManager = new FileBackedTaskManager(tmpFile.toString());

        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);

        inMemoryTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        List<Epic> epicList1 = inMemoryTaskManager.getEpicList();
        List<Epic> epicList2 = actualFileManager.getEpicList();

        assertEquals(epicList1.size(), epicList2.size());

        for (int i = 0; i < epicList1.size(); i++) {
            assertEquals(epicList1.get(i), epicList2.get(i));
        }
    }

    @Test
    public void testSaveAndLoadSubtasks() throws IOException {
        tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager inMemoryTaskManager = new FileBackedTaskManager(tmpFile.toString());

        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubtask(subtask1);

        inMemoryTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        List<Subtask> subtaskList1 = inMemoryTaskManager.getSubtaskList();
        List<Subtask> subtaskList2 = actualFileManager.getSubtaskList();

        assertEquals(subtaskList1.size(), subtaskList2.size());

        for (int i = 0; i < subtaskList1.size(); i++) {
            assertEquals(subtaskList1.get(i), subtaskList2.get(i));
        }
    }

    @Test
    public void testSubtasksInEpics() throws IOException {
        tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager inMemoryTaskManager = new FileBackedTaskManager(tmpFile.toString());

        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);

        inMemoryTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        Epic loadedEpic = actualFileManager.getByIdEpic(epic1.getId());
        List<Integer> subtasksInEpic = loadedEpic.getSubtasks();

        assertEquals(2, subtasksInEpic.size());
        assertEquals(subtask1.getId(), subtasksInEpic.get(0));
        assertEquals(subtask2.getId(), subtasksInEpic.get(1));
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