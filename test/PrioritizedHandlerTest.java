import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrioritizedHandlerTest {

    HttpClient client;
    private Task task_1;
    private Task task_2;
    private Epic epic_1;
    private Subtask subtask_1;
    private Subtask subtask_2;
    private Instant instant_1;
    private Instant instant_2;
    private Instant instant_3;
    private Instant instant_4;
    private ZoneId zoneId;

    @BeforeEach
    void setUp() throws IOException {
        HttpTaskServer.taskManager.deleteAllTasks();
        HttpTaskServer.taskManager.deleteAllEpics();
        HttpTaskServer.taskManager.deleteAllSubtasks();
        client = HttpClient.newHttpClient();

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

        task_1 = new Task("NameTask_1", "DescriptionTask_1", instant_1, Duration.ofSeconds(8000));
        task_2 = new Task("NameTask_2", "DescriptionTask_2", instant_2, Duration.ofSeconds(8000));
        epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        subtask_1 = new Subtask("NameSubtask_3", "DescriptionSubtask_1", epic_1, instant_3, Duration.ofSeconds(1000));
        subtask_2 = new Subtask("NameSubtask_4", "DescriptionSubtask_1", epic_1, instant_4, Duration.ofSeconds(60000));

        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void testGetPrioritizedTasksSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createTask(task_1);
        HttpTaskServer.taskManager.createTask(task_2);
        HttpTaskServer.taskManager.createEpic(epic_1);
        HttpTaskServer.taskManager.createSubtask(subtask_1);
        HttpTaskServer.taskManager.createSubtask(subtask_2);
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritizedFromManager = HttpTaskServer.taskManager.getPrioritizedTasks();

        assertEquals(200, response.statusCode());
        assertNotNull(prioritizedFromManager, "Задачи не возвращаются");
        assertEquals(4, prioritizedFromManager.size(), "Некорректное количество задач");
    }
}