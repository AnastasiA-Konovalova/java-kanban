package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exeptions.ManagerNotContainTaskException;

import java.io.IOException;

import static server.HttpTaskServer.taskManager;

public class EpicHandler extends TasksHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Gson gson = getGsonAdapt();

        String[] split = path.split("/");
        switch (method) {
            case "GET":
                if (path.equals("/epics")) {
                    try {
                        String jsonFormatEpic = gson.toJson(taskManager.getEpicList());
                        sendText(exchange, jsonFormatEpic);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                } else if (path.matches("/epics/\\d+")) {
                    try {
                        String jsonFormatEpic = gson.toJson(taskManager.getByIdEpic(Integer.valueOf(split[2])));
                        sendText(exchange, jsonFormatEpic);
                    } catch (ManagerNotContainTaskException e) {
                        sendNotFound(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                }
                break;
            case "POST":
                if (path.equals("/epics")) {
                    try {
                        gson.toJson(taskManager.createEpic(inputStreamToEpic(exchange)));
                        sendSuccessEmptyVoid(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                } else if (path.matches("/epics/\\d+")) {
                    try {
                        taskManager.updateEpic(inputStreamToEpic(exchange));
                        sendSuccessEmptyVoid(exchange);
                    } catch (ManagerNotContainTaskException exc) {
                        sendNotFound(exchange);
                    } catch (Exception e) {
                        sendInternalServerError(exchange);
                    }
                }
                break;
            case "DELETE":
                if (path.matches("/epics/\\d+")) {
                    try {
                        String jsonFormatTask = gson.toJson(taskManager.deleteEpicById(Integer.valueOf(split[2])));
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
