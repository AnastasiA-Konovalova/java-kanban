import adapter.DurationAdapter;
import adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
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

public class TaskHandlerTest {
    HttpClient client;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    private Task task_1;
    private Task task_2;
    private Task task_3;
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

        task_1 = new Task("NameTask_1", "DescriptionTask_1", instant_1, Duration.ofSeconds(8000));
        task_2 = new Task("NameTask_2", "DescriptionTask_2", instant_2, Duration.ofSeconds(7000));
        task_3 = new Task("NameTask_3_Clone_Task_1", "DescriptionTask_1", instant_1, Duration.ofSeconds(7000));

        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void testGetTaskSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createTask(task_1);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getTaskList();

        assertEquals(200, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetTaskByIdSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createTask(task_1);
        HttpTaskServer.taskManager.createTask(task_2);
        URI url = URI.create("http://localhost:8080/tasks/" + task_1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task = HttpTaskServer.taskManager.getByIdTask(task_1.getId());

        assertEquals(200, response.statusCode());
        assertNotNull(task);
        assertEquals("NameTask_1", task.getName());
        assertEquals("DescriptionTask_1", task.getDescription());
    }

    @Test
    public void testGetTaskByIdFail() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/900");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddTaskSuccess() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task_1);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getTaskList();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testAddTaskFailInteractions() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createTask(task_3);
        String taskJson = gson.toJson(task_1);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getTaskList();

        assertEquals(406, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("NameTask_3_Clone_Task_1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTaskSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createTask(task_1);
        task_2.setId(task_1.getId());
        String taskJson = gson.toJson(task_2);
        URI url = URI.create("http://localhost:8080/tasks/" + task_1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getTaskList();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testUpdateTaskFailInteractions() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createTask(task_1);
        HttpTaskServer.taskManager.createTask(task_2);
        task_3.setId(task_2.getId());
        String taskJson = gson.toJson(task_3);
        URI url = URI.create("http://localhost:8080/tasks/" + task_2.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    public void testDeleteTaskSuccess() throws IOException, InterruptedException {
        HttpTaskServer.taskManager.createTask(task_1);
        URI url = URI.create("http://localhost:8080/tasks/" + task_1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getTaskList();

        assertEquals(200, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }
}