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
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    private Task task_1;
    private Task task_2;
    private Epic epic_1;
    private Epic epic_2;
    private Subtask subtask_1;
    private Subtask subtask_2;

    @BeforeEach
    void setUp() {
        task_1 = new Task("NameTask_1", "DescriptionTask_1");
        task_2 = new Task("NameTask_2", "DescriptionTask_2");
        epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");
        subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1);
        subtask_2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_1);
    }

    @Test
    public void testLoadTaskFromNotEmptyFile() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        Writer fileWriter = new FileWriter(tmpFile);
        fileWriter.write("""
                id,type,name,status,description,epic
                100,Task,Task_1,NEW,DescriptionTask_1
                6,Task,Task_2,IN_PROGRESS,DescriptionTask_2
                4,Task,Task_3,IN_PROGRESS,DescriptionTask_3""");
        fileWriter.flush();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);
        actualFileManager.createTask(new Task("Task_4", "DescriptionTask_4"));
        List<Task> taskList = actualFileManager.getTaskList();
        taskList.sort(Comparator.comparingInt(Task::getId));

        assertEquals(4, taskList.size());
        assertEquals(4, taskList.get(0).getId());
        assertEquals(Task.class, taskList.get(0).getClass());
        assertEquals("Task_3", taskList.get(0).getName());
        assertEquals(Status.IN_PROGRESS, taskList.get(0).getStatus());
        assertEquals("DescriptionTask_3", taskList.get(0).getDescription());

        assertEquals(6, taskList.get(1).getId());
        assertEquals(Task.class, taskList.get(1).getClass());
        assertEquals("Task_2", taskList.get(1).getName());
        assertEquals(Status.IN_PROGRESS, taskList.get(1).getStatus());
        assertEquals("DescriptionTask_2", taskList.get(1).getDescription());

        assertEquals(100, taskList.get(2).getId());
        assertEquals(Task.class, taskList.get(2).getClass());
        assertEquals("Task_1", taskList.get(2).getName());
        assertEquals(Status.NEW, taskList.get(2).getStatus());
        assertEquals("DescriptionTask_1", taskList.get(2).getDescription());

        assertEquals(101, taskList.get(3).getId());
        assertEquals(Task.class, taskList.get(3).getClass());
        assertEquals("Task_4", taskList.get(3).getName());
        assertEquals(Status.NEW, taskList.get(3).getStatus());
        assertEquals("DescriptionTask_4", taskList.get(3).getDescription());
    }

    @Test
    public void testLoadEpicFromNotEmptyFile() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        Writer fileWriter = new FileWriter(tmpFile);
        fileWriter.write("""
                id,type,name,status,description,epic
                5,Epic,Epic_1,IN_PROGRESS,DescriptionEpic_1
                4,Epic,Epic_2,IN_PROGRESS,DescriptionEpic_2
                34,Epic,Epic_3,NEW,DescriptionEpic_3""");
        fileWriter.flush();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);
        actualFileManager.createEpic(new Epic("Epic_4", "DescriptionEpic_4"));
        List<Epic> epicList = actualFileManager.getEpicList();
        epicList.sort(Comparator.comparingInt(Epic::getId));

        assertEquals(4, epicList.size());
        assertEquals(4, epicList.get(0).getId());
        assertEquals(Epic.class, epicList.get(0).getClass());
        assertEquals("Epic_2", epicList.get(0).getName());
        assertEquals(Status.IN_PROGRESS, epicList.get(0).getStatus());
        assertEquals("DescriptionEpic_2", epicList.get(0).getDescription());

        assertEquals(5, epicList.get(1).getId());
        assertEquals(Epic.class, epicList.get(1).getClass());
        assertEquals("Epic_1", epicList.get(1).getName());
        assertEquals(Status.IN_PROGRESS, epicList.get(1).getStatus());
        assertEquals("DescriptionEpic_1", epicList.get(1).getDescription());

        assertEquals(34, epicList.get(2).getId());
        assertEquals(Epic.class, epicList.get(2).getClass());
        assertEquals("Epic_3", epicList.get(2).getName());
        assertEquals(Status.NEW, epicList.get(2).getStatus());
        assertEquals("DescriptionEpic_3", epicList.get(2).getDescription());

        assertEquals(35, epicList.get(3).getId());
        assertEquals(Epic.class, epicList.get(3).getClass());
        assertEquals("Epic_4", epicList.get(3).getName());
        assertEquals(Status.NEW, epicList.get(3).getStatus());
        assertEquals("DescriptionEpic_4", epicList.get(3).getDescription());
    }

    @Test
    public void testLoadSubtaskFromNotEmptyFile() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        Writer fileWriter = new FileWriter(tmpFile);
        fileWriter.write("""
                id,type,name,status,description,epic
                1,Epic,Epic_1,NEW,DescriptionEpic_1
                5,Subtask,Subtask_1,IN_PROGRESS,DescriptionSubtask_1,1
                6,Subtask,Subtask_2,IN_PROGRESS,DescriptionSubtask_2,1
                14,Subtask,Subtask_3,NEW,DescriptionSubtask_3,1""");
        fileWriter.flush();

        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);
        List<Epic> epicList = actualFileManager.getEpicList();
        actualFileManager.createSubtask(new Subtask("Subtask_4", "DescriptionSubtask_4", epicList.get(0)));
        List<Subtask> subtaskList = actualFileManager.getSubtaskList();
        subtaskList.sort(Comparator.comparingInt(Subtask::getId));

        assertEquals(4, subtaskList.size());
        assertEquals(1, epicList.size());
        assertEquals(5, subtaskList.get(0).getId());
        assertEquals(Subtask.class, subtaskList.get(0).getClass());
        assertEquals("Subtask_1", subtaskList.get(0).getName());
        assertEquals(Status.IN_PROGRESS, subtaskList.get(0).getStatus());
        assertEquals("DescriptionSubtask_1", subtaskList.get(0).getDescription());
        assertEquals(1, subtaskList.get(0).getEpic().getId());

        assertEquals(6, subtaskList.get(1).getId());
        assertEquals(Subtask.class, subtaskList.get(1).getClass());
        assertEquals("Subtask_2", subtaskList.get(1).getName());
        assertEquals(Status.IN_PROGRESS, subtaskList.get(1).getStatus());
        assertEquals("DescriptionSubtask_2", subtaskList.get(1).getDescription());
        assertEquals(1, subtaskList.get(1).getEpic().getId());

        assertEquals(14, subtaskList.get(2).getId());
        assertEquals(Subtask.class, subtaskList.get(2).getClass());
        assertEquals("Subtask_3", subtaskList.get(2).getName());
        assertEquals(Status.NEW, subtaskList.get(2).getStatus());
        assertEquals("DescriptionSubtask_3", subtaskList.get(2).getDescription());
        assertEquals(1, subtaskList.get(2).getEpic().getId());

        assertEquals(15, subtaskList.get(3).getId());
        assertEquals(Subtask.class, subtaskList.get(3).getClass());
        assertEquals("Subtask_4", subtaskList.get(3).getName());
        assertEquals(Status.NEW, subtaskList.get(3).getStatus());
        assertEquals("DescriptionSubtask_4", subtaskList.get(3).getDescription());
        assertEquals(1, subtaskList.get(3).getEpic().getId());
    }

    @Test
    public void testSaveAndLoadTasks() throws IOException {
        File tmpFile = File.createTempFile("data", null);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tmpFile.toString());
        fileBackedTaskManager.createTask(task_1);
        fileBackedTaskManager.createTask(task_2);

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
        fileBackedTaskManager.createEpic(epic_1);
        fileBackedTaskManager.createEpic(epic_2);

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
        fileBackedTaskManager.createEpic(epic_1);
        fileBackedTaskManager.createSubtask(subtask_1);

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
        fileBackedTaskManager.createEpic(epic_1);
        fileBackedTaskManager.createSubtask(subtask_1);
        fileBackedTaskManager.createSubtask(subtask_2);

        fileBackedTaskManager.save();
        FileBackedTaskManager actualFileManager = FileBackedTaskManager.loadFromFile(tmpFile);

        Epic loadedEpic = actualFileManager.getByIdEpic(epic_1.getId());
        List<Integer> subtasksInEpic = loadedEpic.getSubtasks();

        assertEquals(2, subtasksInEpic.size());
        assertEquals(subtask_1.getId(), subtasksInEpic.get(0));
        assertEquals(subtask_2.getId(), subtasksInEpic.get(1));
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