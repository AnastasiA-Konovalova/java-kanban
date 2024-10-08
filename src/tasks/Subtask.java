package tasks;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String descriptions, Epic epic) {
        super(name, descriptions);
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
    public String toString() {
        return "tasks.Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}