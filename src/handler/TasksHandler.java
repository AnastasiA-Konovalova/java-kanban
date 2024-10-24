package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exeptions.ManagerIntersectionException;
import exeptions.ManagerNotContainTaskException;

import java.io.IOException;

import static server.HttpTaskServer.taskManager;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Gson gson = getGsonAdapt();

        String[] split = path.split("/");
        switch (method) {
            case "GET":
                if (path.equals("/tasks")) {
                    try {
                        String tasks = gson.toJson(taskManager.getTaskList());
                        sendText(exchange, tasks);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                } else if (path.matches("/tasks/\\d+")) {
                    try {
                        String jsonFormatTask = gson.toJson(taskManager.getByIdTask(Integer.valueOf(split[2])));
                        sendText(exchange, jsonFormatTask);
                    } catch (ManagerNotContainTaskException e) {
                        sendNotFound(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                }
                break;
            case "POST":
                if (path.equals("/tasks")) {
                    try {
                        taskManager.createTask(inputStreamToTask(exchange));
                        sendSuccessEmptyVoid(exchange);
                    } catch (ManagerIntersectionException e) {
                        sendHasInteractions(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                } else if (path.matches("/tasks/\\d+")) {
                    try {
                        taskManager.updateTask(inputStreamToTask(exchange));
                        sendSuccessEmptyVoid(exchange);
                    } catch (ManagerIntersectionException exc) {
                        sendHasInteractions(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                }
                break;
            case "DELETE":
                if (path.matches("/tasks/\\d+")) {
                    try {
                        String jsonFormatTask = gson.toJson(taskManager.deleteTaskById(Integer.valueOf(split[2])));
                        sendText(exchange, jsonFormatTask);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                }
                break;
            default:
                sendInternalServerError(exchange);
        }
    }

}


