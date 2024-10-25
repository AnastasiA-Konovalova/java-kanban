package server;

import com.sun.net.httpserver.HttpServer;
import handler.EpicHandler;
import handler.HistoryHandler;
import handler.PrioritizedHandler;
import handler.SubtaskHandler;
import handler.TasksHandler;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;
    public static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws IOException {
        System.out.println("Уведомление о запуске порта");
        start();
    }

    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
        stop();
    }

    public static void stop() {
        System.out.println("Уведомление о закрытии порта");
        //httpServer.stop(1);
    }
}
