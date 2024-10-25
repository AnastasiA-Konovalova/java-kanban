package handler;

import adapter.DurationAdapter;
import adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

public class BaseHttpHandler {

    private final Gson gson = getGsonAdapt();

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        if (text.isEmpty() || text.equals("null")) {
            sendInternalServerError(exchange);
            return;
        }
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendSuccessEmptyVoid(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        exchange.close();
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, 0);
        exchange.close();
    }

    protected Task inputStreamToTask(HttpExchange exchange) throws IOException {
        try {
            String bodyString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (bodyString.isEmpty()) {
                throw new NullPointerException();
            }
            return gson.fromJson(bodyString, Task.class);

        } catch (IOException exc) {
            throw new IOException();
        }
    }

    protected Epic inputStreamToEpic(HttpExchange exchange) throws IOException {
        try {
            String bodyString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (bodyString.isEmpty()) {
                throw new NullPointerException();
            }
            return gson.fromJson(bodyString, Epic.class);

        } catch (IOException exc) {
            throw new IOException();
        }
    }

    protected Subtask inputStreamToSubtask(HttpExchange exchange) throws IOException {
        try {
            String bodyString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (bodyString.isEmpty()) {
                throw new NullPointerException();
            }
            return gson.fromJson(bodyString, Subtask.class);

        } catch (IOException exc) {
            throw new IOException();
        }
    }

    protected Gson getGsonAdapt() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }
}