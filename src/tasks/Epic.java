package tasks;

import status.Status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> idSubtask;

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    private Instant endTime;

    public Epic(String name, String descriptions) {
        super(name, descriptions);
        idSubtask = new ArrayList<>();
    }

    public Epic() {

    }

    public List<Integer> getSubtasks() {
        return idSubtask;
    }

    public void setSubtasks(List<Integer> subtasks) {
        this.idSubtask = subtasks;
    }

    public void addSubtask(Subtask subtask) {
        idSubtask.add(subtask.getId());
    }

    public void deleteAllSubtasks() {
        getSubtasks().clear();
        setStatus(Status.NEW);
    }

    public void removeSubtask(Subtask subtask) {
        for (int i = 0; i < idSubtask.size(); i++) {
            if (idSubtask.get(i) == subtask.getId()) {
                idSubtask.remove(i);
                return;
            }
        }
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", epicStatus=" + status +
                ", idSubtasks=" + idSubtask +
                '}';
    }
}