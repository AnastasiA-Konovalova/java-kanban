package server;

import adapter.DurationAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handler.EpicHandler;
import handler.HistoryHandler;
import handler.PrioritizedHandler;
import handler.SubtaskHandler;
import handler.TasksHandler;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class HttpTaskServer {
    private final static int PORT = 8080;
    private static HttpServer httpServer;
    public static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws IOException {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime_1 = LocalDateTime.of(2024, Month.DECEMBER, 15, 15, 10);
        ZonedDateTime zonedDateTime_1 = localDateTime_1.atZone(zoneId);
        Instant instant_1 = zonedDateTime_1.toInstant();

        LocalDateTime localDateTime_3 = LocalDateTime.of(2024, Month.JULY, 14, 9, 50);
        ZonedDateTime zonedDateTime_3 = localDateTime_3.atZone(zoneId);
        Instant instant_3 = zonedDateTime_3.toInstant();

        LocalDateTime localDateTime_4 = LocalDateTime.of(2024, Month.MAY, 1, 6, 30);
        ZonedDateTime zonedDateTime_4 = localDateTime_4.atZone(zoneId);
        Instant instant_4 = zonedDateTime_4.toInstant();

        Task task_1 = new Task("NameTask_1", "DescriptionTask_1", instant_1, Duration.ofSeconds(8000));
        Task task_2 = new Task("NameTask_2", "DescriptionTask_2");
        Epic epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        Epic epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");
        Subtask subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1);
        Subtask subtask_3 = new Subtask("NameSubtask_3", "DescriptionSubtask_1", epic_1, instant_3, Duration.ofSeconds(1000));
        Subtask subtask_4 = new Subtask("NameSubtask_4", "DescriptionSubtask_1", epic_2, instant_4, Duration.ofSeconds(60000));

        taskManager.createTask(task_1);
        taskManager.createTask(task_1);
        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_2);
        taskManager.createSubtask(subtask_1);
        taskManager.createSubtask(subtask_3);
        taskManager.createSubtask(subtask_4);

        //taskManager.getByIdTask(task_1.getId());
        //taskManager.getByIdTask(task_2.getId());
        taskManager.getByIdEpic(epic_2.getId());
        taskManager.getByIdSubtask(subtask_4.getId());
        taskManager.getByIdSubtask(subtask_3.getId());

        //System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
        start();
        //tmrMethodForCheck();
        System.out.println("Уведомление о запуске порта");

        // TODO: 22.10.2024 убрать уведомление о запуске перед отправкой на ревью


    }

    public static void tmrMethodForCheck() {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime_1 = LocalDateTime.of(2024, Month.DECEMBER, 15, 15, 10);
        ZonedDateTime zonedDateTime_1 = localDateTime_1.atZone(zoneId);
        Instant instant_1 = zonedDateTime_1.toInstant();

        Task task_1 = new Task("NameTask_1", "DescriptionTask_1", instant_1, Duration.ofSeconds(8000));
        Task task_2 = new Task("NameTask_2", "DescriptionTask_2");
        Epic epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        Epic epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");

        taskManager.createTask(task_1);
        taskManager.createTask(task_2);
        taskManager.createEpic(epic_1);
        taskManager.createEpic(epic_2);
    }

    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
        //stop();
    }

    public static void stop() {
        httpServer.stop(1);
        System.out.println("Уведомление о закрытии порта");
        // TODO: 23.10.2024 убрать уведомление перед отправкой на ревью
    }
}
