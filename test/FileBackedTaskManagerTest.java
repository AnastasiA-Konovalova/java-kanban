import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import save.FileBackedTaskManager;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
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
    public void testLoadTaskFromNotEmptyFile() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        Writer fileWriter = new FileWriter(tmpFile);
        fileWriter.write("""
                id,type,name,status,description,epic
                1,Task,Task_1,NEW,Go away
                2,Task,Task_2,IN_PROGRESS,Cook
                3,Task,Task_3,IN_PROGRESS,Read""");
        fileWriter.flush();
        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);
        List<Task> taskList1 = actualFileManager.getTaskList();

        assertEquals(3, taskList1.size());
        assertEquals(1, taskList1.get(0).getId());
        assertEquals(Task.class, taskList1.get(0).getClass());
        assertEquals("Task_3", taskList1.get(2).getName());
        assertEquals(Status.IN_PROGRESS, taskList1.get(1).getStatus());
        assertEquals(Status.IN_PROGRESS, taskList1.get(2).getStatus());
        assertEquals("Cook", taskList1.get(1).getDescription());
        assertEquals("Read", taskList1.get(2).getDescription());
    }

    @Test
    public void testLoadEpicFromNotEmptyFile() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        Writer fileWriter = new FileWriter(tmpFile);
        fileWriter.write("""
                id,type,name,status,description,epic
                1,Epic,Epic_1,IN_PROGRESS,Buy everything
                2,Epic,Epic_2,IN_PROGRESS,Cut carrot
                3,Epic,Epic_3,NEW,Wash onion""");
        fileWriter.flush();
        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);
        List<Epic> epicList1 = actualFileManager.getEpicList();

        assertEquals(3, epicList1.size());
        assertEquals(1, epicList1.get(0).getId());
        assertEquals(Epic.class, epicList1.get(0).getClass());
        assertEquals("Epic_2", epicList1.get(1).getName());
        assertEquals(Status.IN_PROGRESS, epicList1.get(1).getStatus());
        assertEquals(Status.NEW, epicList1.get(2).getStatus());
        assertEquals("Cut carrot", epicList1.get(1).getDescription());
        assertEquals("Wash onion", epicList1.get(2).getDescription());
    }

    @Test
    public void testLoadSubtaskFromNotEmptyFile() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        Writer fileWriter = new FileWriter(tmpFile);
        fileWriter.write("""
                id,type,name,status,description,epic
                1,Epic,Epic_1,NEW,Buy everything
                2,Subtask,Subtask_1,IN_PROGRESS,Plane,1
                3,Subtask,Subtask_2,IN_PROGRESS,Reserve,1
                4,Subtask,Subtask_2,NEW,Sleep,1""");
        fileWriter.flush();
        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);
        List<Epic> epicList1 = actualFileManager.getEpicList();
        List<Subtask> subtaskList = actualFileManager.getSubtaskList();

        assertEquals(3, subtaskList.size());
        assertEquals(2, subtaskList.get(0).getId());
        assertEquals(Subtask.class, subtaskList.get(1).getClass());
        assertEquals("Subtask_2", subtaskList.get(1).getName());
        assertEquals(Status.IN_PROGRESS, subtaskList.get(1).getStatus());
        assertEquals(Status.NEW, subtaskList.get(2).getStatus());
        assertEquals("Reserve", subtaskList.get(1).getDescription());
        assertEquals("Sleep", subtaskList.get(2).getDescription());
        assertEquals(3, epicList1.get(0).getSubtasks().size());
    }

    @Test
    public void testSaveAndLoadTasks() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tmpFile.toString());
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);

        fileBackedTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        List<Task> taskList1 = fileBackedTaskManager.getTaskList();
        List<Task> taskList2 = actualFileManager.getTaskList();

        assertEquals(taskList1.size(), taskList2.size());
        for (int i = 0; i < taskList1.size(); i++) {
            assertEquals(taskList1.get(i), taskList2.get(i));
        }
    }

    @Test
    public void testSaveAndLoadEpics() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tmpFile.toString());

        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createEpic(epic2);

        fileBackedTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        List<Epic> epicList1 = fileBackedTaskManager.getEpicList();
        List<Epic> epicList2 = actualFileManager.getEpicList();

        assertEquals(epicList1.size(), epicList2.size());
        for (int i = 0; i < epicList1.size(); i++) {
            assertEquals(epicList1.get(i), epicList2.get(i));
        }
    }

    @Test
    public void testSaveAndLoadSubtasks() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tmpFile.toString());

        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubtask(subtask1);

        fileBackedTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        List<Subtask> subtaskList1 = fileBackedTaskManager.getSubtaskList();
        List<Subtask> subtaskList2 = actualFileManager.getSubtaskList();

        assertEquals(subtaskList1.size(), subtaskList2.size());
        assertEquals(subtaskList1.get(0).getEpic(), subtaskList2.get(0).getEpic());

        for (int i = 0; i < subtaskList1.size(); i++) {
            assertEquals(subtaskList1.get(i), subtaskList2.get(i));
        }
    }

    @Test
    public void testSubtasksInEpics() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tmpFile.toString());

        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubtask(subtask1);
        fileBackedTaskManager.createSubtask(subtask2);

        fileBackedTaskManager.save();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        Epic loadedEpic = actualFileManager.getByIdEpic(epic1.getId());
        List<Integer> subtasksInEpic = loadedEpic.getSubtasks();

        assertEquals(2, subtasksInEpic.size());
        assertEquals(subtask1.getId(), subtasksInEpic.get(0));
        assertEquals(subtask2.getId(), subtasksInEpic.get(1));
    }

    @Test
    public void testSaveAndLoadEmptyFile() throws IOException {
        File tmpFile = File.createTempFile("data", null);
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