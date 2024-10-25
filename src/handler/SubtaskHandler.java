package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exeptions.ManagerIntersectionException;
import exeptions.ManagerNotContainTaskException;

import java.io.IOException;

import static server.HttpTaskServer.taskManager;

public class SubtaskHandler extends TasksHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Gson gson = getGsonAdapt();

        String[] split = path.split("/");
        switch (method) {
            case "GET":
                if (path.equals("/subtasks")) {
                    try {
                        String jsonFormatSubtask = gson.toJson(taskManager.getSubtaskList());
                        sendText(exchange, jsonFormatSubtask);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                } else if (path.matches("/subtasks/\\d+")) {
                    try {
                        String jsonFormatSubtask = gson.toJson(taskManager.getByIdSubtask(Integer.valueOf(split[2])));
                        sendText(exchange, jsonFormatSubtask);
                    } catch (ManagerNotContainTaskException e) {
                        sendNotFound(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                }
                break;
            case "POST":
                if (path.equals("/subtasks")) {
                    try {
                        gson.toJson(taskManager.createSubtask(inputStreamToSubtask(exchange)));
                        sendSuccessEmptyVoid(exchange);
                    } catch (ManagerIntersectionException e) {
                        sendHasInteractions(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                } else if (path.matches("/subtasks/\\d+")) {
                    try {
                        taskManager.updateSubtask(inputStreamToSubtask(exchange));
                        sendSuccessEmptyVoid(exchange);
                    } catch (ManagerIntersectionException e) {
                        sendHasInteractions(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                }
                break;
            case "DELETE":
                if (path.matches("/subtasks/\\d+")) {
                    try {
                        String subtask = gson.toJson(taskManager.deleteSubtaskById(Integer.valueOf(split[2])));
                        sendText(exchange, subtask);
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