package tasks;

import java.time.Duration;
import java.time.Instant;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String descriptions, Epic epic) {
        super(name, descriptions);
        this.epic = epic;
    }

    public Subtask(String name, String descriptions, Epic epic, Instant startTime, Duration duration) {
        super(name, descriptions, startTime, duration);
        this.epic = epic;
    }

    public Subtask() {

    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        if (getEndTime() == null) {
            return "tasks.Subtask{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    '}';
        } else {
            return "tasks.Subtask{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    ", startTime= " + formatStartTime() +
                    ", duration= " + formatDuration() +
                    '}';
        }

    }
}