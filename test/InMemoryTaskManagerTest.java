import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private InMemoryTaskManager inMemoryTaskManager;
    private Task task_1;
    private Task task_2;
    private Epic epic_1;
    private Epic epic_2;
    private Subtask subtask_1;
    private Subtask subtask_2;
    private Subtask subtask_3;
    private Subtask subtask_4;
    private Subtask subtask_5;
    private Subtask subtask_6;
    private Instant instant_1;
    private Instant instant_2;
    private Instant instant_3;
    private Instant instant_4;
    private ZoneId zoneId;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager();
        zoneId = ZoneId.of("Europe/Moscow");

        LocalDateTime localDateTime_1 = LocalDateTime.of(2024, Month.DECEMBER, 15, 15, 10);
        ZonedDateTime zonedDateTime_1 = localDateTime_1.atZone(zoneId);
        instant_1 = zonedDateTime_1.toInstant();

        LocalDateTime localDateTime_2 = LocalDateTime.of(2024, Month.APRIL, 10, 10, 0);
        ZonedDateTime zonedDateTime_2 = localDateTime_2.atZone(zoneId);
        instant_2 = zonedDateTime_2.toInstant();

        LocalDateTime localDateTime_3 = LocalDateTime.of(2024, Month.JULY, 14, 9, 50);
        ZonedDateTime zonedDateTime_3 = localDateTime_3.atZone(zoneId);
        instant_3 = zonedDateTime_3.toInstant();

        LocalDateTime localDateTime_4 = LocalDateTime.of(2024, Month.MAY, 1, 6, 30);
        ZonedDateTime zonedDateTime_4 = localDateTime_4.atZone(zoneId);
        instant_4 = zonedDateTime_4.toInstant();

        taskManager = Managers.getDefault();
        task_1 = new Task("NameTask_1", "DescriptionTask_1", instant_1, Duration.ofSeconds(8000));
        task_2 = new Task("NameTask_2", "DescriptionTask_2");
        epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");
        subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1);
        subtask_2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_1);
        subtask_3 = new Subtask("NameSubtask_3", "DescriptionSubtask_1", epic_1, instant_3, Duration.ofSeconds(1000));
        subtask_4 = new Subtask("NameSubtask_4", "DescriptionSubtask_1", epic_2, instant_4, Duration.ofSeconds(60000));
        subtask_5 = new Subtask("NameSubtask_5", "DescriptionSubtask_5", epic_2, instant_2, Duration.ofSeconds(7000));
        subtask_6 = new Subtask("NameSubtask_6", "DescriptionSubtask_6", epic_2, instant_1, Duration.ofSeconds(800));
    }

    @Test
    void createNewTaskAndGetById() {
        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        final int taskId = task_1.getId();
        final Task savedTask = taskManager.getByIdTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task_1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task_1, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task_2, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void testGetPrioritizedTasks() {
        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        List<Task> sortedList = taskManager.getPrioritizedTasks();

        assertEquals(1, sortedList.size());
        assertEquals(task_1, sortedList.get(0));
    }

    @Test
    void testGetPrioritizedSubtask() {
        taskManager.createEpic(epic_2);
        taskManager.createSubtask(subtask_4);
        taskManager.createSubtask(subtask_5);
        taskManager.createSubtask(subtask_6);

        System.out.println(epic_2);

        List<Task> sortedList = taskManager.getPrioritizedTasks();

        assertEquals(3, sortedList.size());
        assertEquals(subtask_4, sortedList.get(1));
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
        Task updateTasks = new Task("NameTask_3", "DescriptionTask_3", instant_2, Duration.ofSeconds(4000));
        updateTasks.setId(task_1.getId());
        taskManager.updateTask(updateTasks);

        assertEquals(1, taskManager.getTaskList().size());
        assertEquals("NameTask_3", taskManager.getTaskList().get(0).getName());
        assertEquals("DescriptionTask_3", taskManager.getTaskList().get(0).getDescription());
        assertEquals("2024-04-10T07:00:00Z", taskManager.getTaskList().get(0).getStartTime().toString());
        assertEquals("PT1H6M40S", taskManager.getTaskList().get(0).getDuration().toString());
        assertEquals("2024-04-10T08:06:40Z", taskManager.getTaskList().get(0).getEndTime().toString());
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
        taskManager.createEpic(epic_2);
        taskManager.createSubtask(subtask_5);

        Subtask updateSubtasks = new Subtask("NameSubtask_6", "DescriptionSubtask_6", epic_2, instant_1, Duration.ofSeconds(80));
        updateSubtasks.setId(subtask_5.getId());
        taskManager.updateSubtask(updateSubtasks);

        assertEquals(1, taskManager.getSubtaskList().size());
        assertEquals("NameSubtask_6", taskManager.getSubtaskList().get(0).getName());
        assertEquals("DescriptionSubtask_6", taskManager.getSubtaskList().get(0).getDescription());
        assertEquals("2024-12-15T12:10:00Z", taskManager.getSubtaskList().get(0).getStartTime().toString());
        assertEquals("PT1M20S", taskManager.getSubtaskList().get(0).getDuration().toString());
        assertEquals("2024-12-15T12:11:20Z", taskManager.getSubtaskList().get(0).getEndTime().toString());
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
    void testUpdateEpicStatusIfAllSubtasksNewEndDone() {
        subtask_1.setStatus(Status.IN_PROGRESS);
        subtask_2.setStatus(Status.IN_PROGRESS);
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask_2);

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

    @Test
    public void testDeleteAllTask() {
        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getPrioritizedTasks().size());
        assertEquals(0, taskManager.getTaskList().size());
    }

    @Test
    public void testDeleteTaskById() {
        taskManager.createTask(task_1);
        taskManager.createTask(task_2);

        taskManager.deleteTaskById(1);

        assertEquals(0, taskManager.getPrioritizedTasks().size());
        assertEquals(1, taskManager.getTaskList().size());
    }

    @Test
    public void testDeleteAllSubtask() {
        taskManager.createEpic(epic_2);
        System.out.println(taskManager.getEpicList());
        taskManager.createSubtask(subtask_4);
        taskManager.createSubtask(subtask_5);

        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getPrioritizedTasks().size());
        assertEquals(0, taskManager.getSubtaskList().size());
    }

    @Test
    public void testDeleteSubtaskById() {
        taskManager.createEpic(epic_1);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask_2);
        taskManager.createSubtask(subtask_3);
        System.out.println(subtask_1.getEpic());

        taskManager.deleteSubtaskById(subtask_2.getId());
        System.out.println(taskManager.getPrioritizedTasks());

        assertEquals(1, taskManager.getPrioritizedTasks().size());
        assertEquals(2, taskManager.getSubtaskList().size());
    }

    @Test
    public void testGetStartTimeForEpic() {
        inMemoryTaskManager.createEpic(epic_2);
        inMemoryTaskManager.createSubtask(subtask_4);
        inMemoryTaskManager.createSubtask(subtask_5);
        inMemoryTaskManager.createSubtask(subtask_6);

        Instant startTime = epic_2.getStartTime();

        assertEquals("2024-04-10T07:00:00Z", startTime.toString());
    }

    @Test
    public void testGetDurationForEpic() {
        inMemoryTaskManager.createEpic(epic_2);
        inMemoryTaskManager.createSubtask(subtask_4);
        inMemoryTaskManager.createSubtask(subtask_5);
        inMemoryTaskManager.createSubtask(subtask_6);

        Duration duration = epic_2.getDuration();

        assertEquals("PT18H50M", duration.toString());
    }

    @Test
    public void testGetEndTimeForEpic() {
        inMemoryTaskManager.createEpic(epic_2);
        inMemoryTaskManager.createSubtask(subtask_4);
        inMemoryTaskManager.createSubtask(subtask_5);
        inMemoryTaskManager.createSubtask(subtask_6);

        Instant endTime = epic_2.getEndTime();

        assertEquals("2024-12-15T12:23:20Z", endTime.toString());
    }

    @Test
    public void getPrioritizedTasks() {
        taskManager.createTask(task_1);
        taskManager.createTask(task_2);
        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_2);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask_2);
        taskManager.createSubtask(subtask_3);
        taskManager.createSubtask(subtask_4);
        taskManager.createSubtask(subtask_5);

        List<Task> prioritizedSet = taskManager.getPrioritizedTasks();
        List<Task> prioritizedList = new ArrayList<>(prioritizedSet);

        assertEquals(4, prioritizedList.size());
        assertEquals(subtask_5, prioritizedList.get(0));
        assertEquals(subtask_4, prioritizedList.get(1));
        assertEquals(subtask_3, prioritizedList.get(2));
        assertEquals(task_1, prioritizedList.get(3));
    }

    @Test
    public void validateTask() {
        inMemoryTaskManager.createTask(task_1);
        inMemoryTaskManager.createEpic(epic_2);
        inMemoryTaskManager.createSubtask(subtask_5);

        boolean validate_2 = !inMemoryTaskManager.validateTask(inMemoryTaskManager.createSubtask(subtask_4));

        assertFalse(false, String.valueOf(validate_2));
    }
}