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
    private Task task_1;
    private Task task_2;
    private Epic epic_1;
    private Epic epic_2;
    private Subtask subtask_1;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task_1 = new Task("NameTask_1", "DescriptionTask_1");
        task_2 = new Task("NameTask_2", "DescriptionTask_2");
        epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");
        subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1);
    }

    @Test
    void createNewTaskAndGetById() {
        taskManager.createTask(task_1);

        final int taskId = task_1.getId();
        final Task savedTask = taskManager.getByIdTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task_1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task_1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void fieldTasksNotChange() {
        taskManager.createTask(task_1);

        Task checkCreateRight = taskManager.getTaskList().get(0);

        assertEquals("NameTask_1", checkCreateRight.getName(), "Название задачи не " +
                "соответствует фактическому");
        assertEquals("DescriptionTask_1", checkCreateRight.getDescription(), "Описание " +
                "не соответствует фактическому");
        assertEquals(1, checkCreateRight.getId(), "Id не соответствует фактическому");
    }

    @Test
    void createNewEpicAndGetById() {
        taskManager.createEpic(epic_1);

        final int epicId = epic_1.getId();
        final Epic savedEpic = taskManager.getByIdEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic_1, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic_1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void fieldEpicNotChange() {
        taskManager.createEpic(epic_1);

        Epic checkCreateRight = taskManager.getEpicList().get(0);

        assertEquals("NameEpic_1", checkCreateRight.getName(), "Название задачи не " +
                "соответствует фактическому");
        assertEquals("DescriptionEpic_1", checkCreateRight.getDescription(), "Описание " +
                "не соответствует фактическому");
        assertEquals(1, checkCreateRight.getId(), "Id не соответствует фактическому");
    }

    @Test
    void createNewSubtaskAndGetById() {
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);

        final int subtaskId = subtask_1.getId();
        final Subtask savedSubtask = taskManager.getByIdSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask_1, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask_1, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void fieldSubtaskNotChange() {
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);

        Subtask checkCreateRight = taskManager.getSubtaskList().get(0);

        assertEquals("NameSubtask_1", checkCreateRight.getName(), "Название задачи не " +
                "соответствует фактическому");
        assertEquals("DescriptionSubtask_1", checkCreateRight.getDescription(), "Описание " +
                "не соответствует фактическому");
        assertEquals(2, checkCreateRight.getId(), "Id не соответствует фактическому");
    }

    @Test
    void testGetTaskList() {
        List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Список задач не должен быть null");
        assertTrue(tasks.isEmpty(), "Список задач должен быть пуст");

        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Список задач не должен быть null.");
        assertEquals(task_1, tasks.get(0), "Задача в списке не совпадает с добавленной задачей.");
        assertEquals(task_2, tasks.get(1), "Задача в списке не совпадает с добавленной задачей.");
    }

    @Test
    void testGetEpicList() {
        List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Список задач не должен быть null");
        assertTrue(epics.isEmpty(), "Список задач должен быть пуст");

        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_2);

        epics = taskManager.getEpicList();

        assertNotNull(epics, "Список задач не должен быть null.");
        assertEquals(epic_1, epics.get(0), "Задача в списке не совпадает с добавленной задачей.");
        assertEquals(epic_2, epics.get(1), "Задача в списке не совпадает с добавленной задачей.");
    }

    @Test
    void testGetSubtaskList() {
        List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Список задач не должен быть null");
        assertTrue(subtasks.isEmpty(), "Список задач должен быть пуст");

        Subtask subtask2 = new Subtask("NameEpic_2", "DescriptionEpic_2", epic_1);
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask2);

        subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Список задач не должен быть null.");
        assertEquals(subtask2, subtasks.get(0), "Задача в списке не совпадает с добавленной задачей.");
    }

    @Test
    void testTaskSetIdAndGenerateIdWorkTogether() {
        task_1.setId(1);
        task_2.setId(1);

        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        assertNotEquals(task_1.getId(), task_2.getId());
    }

    @Test
    void testUpdateTask() {
        taskManager.createTask(task_1);
        task_1.setStatus(Status.IN_PROGRESS);
        task_1.setName("UpdateTask");
        task_1.setDescription("UpdateDescription");

        taskManager.updateTask(task_1);

        assertNotNull(task_1, "Задача должна существовать после обновления");
        assertEquals("UpdateTask", task_1.getName(), "Имя задачи должно быть обновлено");
        assertEquals("UpdateDescription", task_1.getDescription(), "Описание задачи должно быть обновлено");
        assertEquals(Status.IN_PROGRESS, task_1.getStatus(), "Статус задачи должен быть обновлен");
    }

    @Test
    void testUpdateEpic() {
        taskManager.createEpic(epic_1);
        epic_1.setStatus(Status.IN_PROGRESS);
        epic_1.setName("UpdateEpic");
        epic_1.setDescription("UpdateDescription");

        taskManager.updateEpic(epic_1);

        assertNotNull(epic_1, "Задача должна существовать после обновления");
        assertEquals("UpdateEpic", epic_1.getName(), "Имя задачи должно быть обновлено");
        assertEquals("UpdateDescription", epic_1.getDescription(), "Описание задачи должно быть обновлено");
        assertEquals(Status.IN_PROGRESS, epic_1.getStatus(), "Статус задачи должен быть обновлен");
    }

    @Test
    void testUpdateSubtask() {
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);
        subtask_1.setStatus(Status.IN_PROGRESS);
        subtask_1.setName("UpdateSubtask");
        subtask_1.setDescription("UpdateDescription");

        taskManager.updateSubtask(subtask_1);

        assertNotNull(subtask_1, "Задача должна существовать после обновления");
        assertEquals("UpdateSubtask", subtask_1.getName(), "Имя задачи должно быть обновлено");
        assertEquals("UpdateDescription", subtask_1.getDescription(), "Описание задачи должно быть обновлено");
        assertEquals(Status.IN_PROGRESS, subtask_1.getStatus(), "Статус задачи должен быть обновлен");
    }

    @Test
    void updateEpicStatusIfAllSubtasksNEW() {
        Subtask subtask2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_1);
        subtask_1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask2);

        taskManager.updateEpic(epic_1);

        assertEquals(Status.NEW, epic_1.getStatus(), "Статус эпика должен быть NEW, " +
                "если все подзадачи NEW");
    }

    @Test
    void updateEpicStatusIfAllSubtasksDONE() {
        Subtask subtask2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_1);
        subtask_1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask2);

        taskManager.updateEpic(epic_1);

        assertEquals(Status.DONE, epic_1.getStatus(), "Статус эпика должен быть DONE, " +
                "если все подзадачи DONE");
    }

    @Test
    void updateEpicStatusIfAllSubtasksIN_PROGRESS() {
        Subtask subtask2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_1);
        subtask_1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask2);

        taskManager.updateEpic(epic_1);

        assertEquals(Status.IN_PROGRESS, epic_1.getStatus(), "Статус эпика должен быть IN_PROGRESS, " +
                "если подзадачи с разным статусом");
    }

    @Test
    void testGetSubtaskFromEpic() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask subtask2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_1);

        inMemoryTaskManager.createEpic(epic_1);
        inMemoryTaskManager.createEpic(epic_1);
        inMemoryTaskManager.createSubtask(subtask_1);
        inMemoryTaskManager.createSubtask(subtask2);

        List<Subtask> subtaskFromEpic = inMemoryTaskManager.getSubtaskFromEpic(epic_1.getId());

        assertNotNull(subtaskFromEpic, "Список не должен быть пустым");
        assertEquals(subtask_1, subtaskFromEpic.get(0), "Лист не содержит subtask1");
        assertEquals(subtask2, subtaskFromEpic.get(1), "Лист не содержит subtask2");
    }

    @Test
    void testEpicShouldNotContainRelevantSubtasksAfterRemove() {
        taskManager.deleteEpicById(epic_1.getId());

        assertEquals(0, taskManager.getEpicList().size(), "Список epic пуст");
        assertEquals(0, taskManager.getSubtaskList().size(), "Список subtask пуст");
        System.out.println(subtask_1);
    }

    @Test
    void testChangeTheFieldNameUsingSet() {
        task_1.setName("SetNameTask_1");
        epic_1.setName("SetNameEpic_1");
        subtask_1.setName("SetNameSubtask_1");

        assertEquals("SetNameTask_1", task_1.getName());
        assertEquals("SetNameEpic_1", epic_1.getName());
        assertEquals("SetNameSubtask_1", subtask_1.getName());
    }

    @Test
    void testChangeTheFieldDescriptionUsingSet() {
        task_1.setDescription("SetDescriptionTask_1");
        epic_1.setDescription("SetDescriptionEpic_1");
        subtask_1.setDescription("SetDescriptionSubtask_1");

        assertEquals("SetDescriptionTask_1", task_1.getDescription());
        assertEquals("SetDescriptionEpic_1", epic_1.getDescription());
        assertEquals("SetDescriptionSubtask_1", subtask_1.getDescription());
    }

    @Test
    void testChangeTheFieldStatusUsingSet() {
        task_1.setStatus(Status.NEW);
        epic_1.setStatus(Status.IN_PROGRESS);
        subtask_1.setStatus(Status.DONE);

        assertEquals(Status.NEW, task_1.getStatus());
        assertEquals(Status.IN_PROGRESS, epic_1.getStatus());
        assertEquals(Status.DONE, subtask_1.getStatus());
    }

    @Test
    void testChangeTheFieldIdUsingSet() {
        task_1.setId(1);
        epic_1.setId(4);
        subtask_1.setId(9);

        assertEquals(1, task_1.getId());
        assertEquals(4, epic_1.getId());
        assertEquals(9, subtask_1.getId());
    }
}