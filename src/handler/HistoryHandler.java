package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static server.HttpTaskServer.taskManager;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Gson gson = getGsonAdapt();

        if ("GET".equals(method) && path.equals("/history")) {
            try {
                String jsonFormatTask = gson.toJson(taskManager.getHistory());
                sendText(exchange, jsonFormatTask);
            } catch (Exception e) {
                sendInternalServerError(exchange);
            }
            return;
        }
        sendInternalServerError(exchange);
    }
}


