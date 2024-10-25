import adapter.DurationAdapter;
import adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Subtask;

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

public class SubtaskHandlerTest {
    HttpClient client;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    private Epic epic_1;
    private Subtask subtask_1;
    private Subtask subtask_2;
    private Subtask subtask_3;
    private Subtask subtask_4;
    private Instant instant_1;
    private Instant instant_2;
    private ZoneId zoneId;

    @BeforeEach
    public void setUp() throws IOException {
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

        epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1, instant_1, Duration.ofSeconds(1000));
        subtask_2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_1, instant_2, Duration.ofSeconds(60000));
        subtask_3 = new Subtask("NameSubtask_3", "DescriptionSubtask_3", epic_1, instant_1, Duration.ofSeconds(60000));
        subtask_4 = new Subtask("NameSubtask_4", "DescriptionSubtask_4", epic_1, instant_1, Duration.ofSeconds(60000));
        HttpTaskServer.start();
    }


    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void testGetSubtaskSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createEpic(epic_1);
        HttpTaskServer.taskManager.createSubtask(subtask_1);
        HttpTaskServer.taskManager.createSubtask(subtask_2);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasksFromManager = HttpTaskServer.taskManager.getSubtaskList();

        assertEquals(200, response.statusCode());
        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("NameSubtask_1", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("NameSubtask_2", subtasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtaskByIdSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createEpic(epic_1);
        HttpTaskServer.taskManager.createSubtask(subtask_1);
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask_1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = HttpTaskServer.taskManager.getByIdSubtask(subtask_1.getId());

        assertEquals(200, response.statusCode());
        assertNotNull(subtask);
        assertEquals("NameSubtask_1", subtask.getName());
        assertEquals("DescriptionSubtask_1", subtask.getDescription());
    }

    @Test
    public void testGetSubtaskByIdFail() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/900");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddSubtaskSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createEpic(epic_1);
        String subtaskJson = gson.toJson(subtask_1);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtaskFromManager = HttpTaskServer.taskManager.getSubtaskList();

        assertEquals(201, response.statusCode());
        assertNotNull(subtaskFromManager, "Задачи не возвращаются");
        assertEquals(1, subtaskFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testUpdateSubtaskSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createEpic(epic_1);
        HttpTaskServer.taskManager.createSubtask(subtask_1);
        subtask_2.setId(subtask_1.getId());
        String subtaskJson = gson.toJson(subtask_2);
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask_1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasksFromManager = HttpTaskServer.taskManager.getSubtaskList();

        assertEquals(201, response.statusCode());
        assertEquals(1, subtasksFromManager.size());
        assertEquals(subtask_2.getName(), subtasksFromManager.get(0).getName());
    }

    @Test
    public void testUpdateEpicFailInteractions() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createEpic(epic_1);
        HttpTaskServer.taskManager.createSubtask(subtask_2);
        HttpTaskServer.taskManager.createSubtask(subtask_3);
        subtask_4.setId(subtask_2.getId());
        String subtaskJson = gson.toJson(subtask_4);
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask_2.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    public void testDeleteEpicSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createEpic(epic_1);
        HttpTaskServer.taskManager.createSubtask(subtask_1);
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask_1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasksFromManager = HttpTaskServer.taskManager.getSubtaskList();

        assertEquals(200, response.statusCode());
        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(0, subtasksFromManager.size(), "Некорректное количество задач");
    }
}