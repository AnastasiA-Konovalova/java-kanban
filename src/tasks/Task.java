package tasks;

import status.Status;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

import static java.util.Objects.requireNonNull;

public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected int id;
    protected Duration duration;
    protected Instant startTime;
    protected DateTimeFormatter formatter;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }
    public Task(String name, String description, Instant startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");
    }

    public Task() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
       if (getDuration() != null && getStartTime() != null) {
           return getStartTime().plus(getDuration());
       } else {
           return getStartTime(); //либо убрать иф, либо доб-ть Optional
       }
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String toString() {
        if (getEndTime() == null) {
            return "tasks.Task{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", taskStatus=" + status +
                    '}';
        } else {
            return "tasks.Task{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", taskStatus=" + status +
                    ", startTime= " + formatStartTime() +
                    ", duration= " + formatDuration() +
                    '}';
        }
    }

    protected String formatStartTime() {
        if (startTime == null) {
            return "Start time is not set";
        }
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm"); // Инициализация по умолчанию
        }

        ZonedDateTime zonedDateTime = startTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime().format(formatter);
    }

    protected String formatDuration() {
        long hour = duration.toHoursPart();
        long min = duration.toMinutesPart();
        long sec = duration.toSecondsPart();
        return String.format("%d:%02d:%02d", hour, min, sec);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}