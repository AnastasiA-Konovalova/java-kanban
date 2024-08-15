package tasks;

import status.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> idSubtask;

    public Epic(String name, String descriptions) {
        super(name, descriptions);
        idSubtask = new ArrayList<>();
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

    public void deleteSubtaskById(Subtask subtask) {
        idSubtask.remove(subtask.getId());
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