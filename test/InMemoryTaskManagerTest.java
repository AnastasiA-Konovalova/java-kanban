import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task1 = new Task("NameTask1", "DescriptionTask1");
        task2 = new Task("NameTask2", "DescriptionTask2");
        epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic1);
    }

    @Test
    void createNewTaskAndGetById() {
        taskManager.createTask(task1);

        final int taskId = task1.getId();
        final Task savedTask = taskManager.getByIdTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void fieldTasksNotChange() {
        taskManager.createTask(task1);

        Task checkCreateRight = taskManager.getTaskList().get(0);

        assertEquals("NameTask1", checkCreateRight.getName(), "Название задачи не " +
                "соответствует фактическому");
        assertEquals("DescriptionTask1", checkCreateRight.getDescription(), "Описание " +
                "не соответствует фактическому");
        assertEquals(1, checkCreateRight.getId(), "Id не соответствует фактическому");
    }

    @Test
    void createNewEpicAndGetById() {
        taskManager.createEpic(epic1);

        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getByIdEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void fieldEpicNotChange() {
        taskManager.createEpic(epic1);

        Epic checkCreateRight = taskManager.getEpicList().get(0);

        assertEquals("NameEpic1", checkCreateRight.getName(), "Название задачи не " +
                "соответствует фактическому");
        assertEquals("DescriptionEpic1", checkCreateRight.getDescription(), "Описание " +
                "не соответствует фактическому");
        assertEquals(1, checkCreateRight.getId(), "Id не соответствует фактическому");
    }

    @Test
    void createNewSubtaskAndGetById() {
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask = taskManager.getByIdSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void fieldSubtaskNotChange() {
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        Subtask checkCreateRight = taskManager.getSubtaskList().get(0);

        assertEquals("NameSubtask1", checkCreateRight.getName(), "Название задачи не " +
                "соответствует фактическому");
        assertEquals("DescriptionSubtask1", checkCreateRight.getDescription(), "Описание " +
                "не соответствует фактическому");
        assertEquals(2, checkCreateRight.getId(), "Id не соответствует фактическому");
    }

    @Test
    void testGetTaskList() {
        List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Список задач не должен быть null");
        assertTrue(tasks.isEmpty(), "Список задач должен быть пуст");

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Список задач не должен быть null.");
        assertEquals(task1, tasks.get(0), "Задача в списке не совпадает с добавленной задачей.");
        assertEquals(task2, tasks.get(1), "Задача в списке не совпадает с добавленной задачей.");
    }

    @Test
    void testGetEpicList() {
        List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Список задач не должен быть null");
        assertTrue(epics.isEmpty(), "Список задач должен быть пуст");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        epics = taskManager.getEpicList();

        assertNotNull(epics, "Список задач не должен быть null.");
        assertEquals(epic1, epics.get(0), "Задача в списке не совпадает с добавленной задачей.");
        assertEquals(epic2, epics.get(1), "Задача в списке не совпадает с добавленной задачей.");
    }

    @Test
    void testGetSubtaskList() {
        List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Список задач не должен быть null");
        assertTrue(subtasks.isEmpty(), "Список задач должен быть пуст");

        Subtask subtask2 = new Subtask("NameEpic2", "DescriptionEpic2", epic1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask2);

        subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Список задач не должен быть null.");
        assertEquals(subtask2, subtasks.get(0), "Задача в списке не совпадает с добавленной задачей.");
    }

    @Test
    void testTaskSetIdAndGenerateIdWorkTogether() {
        task1.setId(1);
        task2.setId(1);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void testUpdateTask() {
        taskManager.createTask(task1);
        task1.setStatus(Status.IN_PROGRESS);
        task1.setName("UpdateTask");
        task1.setDescription("UpdateDescription");

        taskManager.updateTask(task1);

        assertNotNull(task1, "Задача должна существовать после обновления");
        assertEquals("UpdateTask", task1.getName(), "Имя задачи должно быть обновлено");
        assertEquals("UpdateDescription", task1.getDescription(), "Описание задачи должно быть обновлено");
        assertEquals(Status.IN_PROGRESS, task1.getStatus(), "Статус задачи должен быть обновлен");
    }

    @Test
    void testUpdateEpic() {
        taskManager.createEpic(epic1);
        epic1.setStatus(Status.IN_PROGRESS);
        epic1.setName("UpdateEpic");
        epic1.setDescription("UpdateDescription");

        taskManager.updateEpic(epic1);

        assertNotNull(epic1, "Задача должна существовать после обновления");
        assertEquals("UpdateEpic", epic1.getName(), "Имя задачи должно быть обновлено");
        assertEquals("UpdateDescription", epic1.getDescription(), "Описание задачи должно быть обновлено");
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус задачи должен быть обновлен");
    }

    @Test
    void testUpdateSubtask() {
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setName("UpdateSubtask");
        subtask1.setDescription("UpdateDescription");

        taskManager.updateSubtask(subtask1);

        assertNotNull(subtask1, "Задача должна существовать после обновления");
        assertEquals("UpdateSubtask", subtask1.getName(), "Имя задачи должно быть обновлено");
        assertEquals("UpdateDescription", subtask1.getDescription(), "Описание задачи должно быть обновлено");
        assertEquals(Status.IN_PROGRESS, subtask1.getStatus(), "Статус задачи должен быть обновлен");
    }

    @Test
    void updateEpicStatusIfAllSubtasksNEW() {
        Subtask subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", epic1);
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.updateEpic(epic1);

        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика должен быть NEW, " +
                "если все подзадачи NEW");
    }

    @Test
    void updateEpicStatusIfAllSubtasksDONE() {
        Subtask subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", epic1);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.updateEpic(epic1);

        assertEquals(Status.DONE, epic1.getStatus(), "Статус эпика должен быть DONE, " +
                "если все подзадачи DONE");
    }

    @Test
    void updateEpicStatusIfAllSubtasksIN_PROGRESS() {
        Subtask subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", epic1);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.updateEpic(epic1);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика должен быть IN_PROGRESS, " +
                "если подзадачи с разным статусом");
    }

    @Test
    void testGetSubtaskFromEpic() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", epic1);

        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);

        List<Subtask> subtaskFromEpic = inMemoryTaskManager.getSubtaskFromEpic(epic1.getId());

        assertNotNull(subtaskFromEpic, "Список не должен быть пустым");
        assertEquals(subtask1, subtaskFromEpic.get(0), "Лист не содержит subtask1");
        assertEquals(subtask2, subtaskFromEpic.get(1), "Лист не содержит subtask2");
    }

    @Test
    void testEpicShouldNotContainRelevantSubtasksAfterRemove() {
        taskManager.deleteEpicById(epic1.getId());

        assertEquals(0, taskManager.getEpicList().size(), "Список epic пуст");
        assertEquals(0, taskManager.getSubtaskList().size(), "Список subtask пуст");
        System.out.println(subtask1);
    }

    @Test
    void testChangeTheFieldNameUsingSet() {
        task1.setName("SetNameTask1");
        epic1.setName("SetNameEpic1");
        subtask1.setName("SetNameSubtask1");

        assertEquals("SetNameTask1", task1.getName());
        assertEquals("SetNameEpic1", epic1.getName());
        assertEquals("SetNameSubtask1", subtask1.getName());
    }

    @Test
    void testChangeTheFieldDescriptionUsingSet() {
        task1.setDescription("SetDescriptionTask1");
        epic1.setDescription("SetDescriptionEpic1");
        subtask1.setDescription("SetDescriptionSubtask1");

        assertEquals("SetDescriptionTask1", task1.getDescription());
        assertEquals("SetDescriptionEpic1", epic1.getDescription());
        assertEquals("SetDescriptionSubtask1", subtask1.getDescription());
    }

    @Test
    void testChangeTheFieldStatusUsingSet() {
        task1.setStatus(Status.NEW);
        epic1.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.DONE);

        assertEquals(Status.NEW, task1.getStatus());
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
        assertEquals(Status.DONE, subtask1.getStatus());
    }

    @Test
    void testChangeTheFieldIdUsingSet() {
        task1.setId(1);
        epic1.setId(4);
        subtask1.setId(9);

        assertEquals(1, task1.getId());
        assertEquals(4, epic1.getId());
        assertEquals(9, subtask1.getId());
    }
}