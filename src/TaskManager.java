import java.util.*;

public class TaskManager {
    private Integer id = 1;
    Map<Integer, Task> taskMap;
    Map<Integer, Epic> epicMap;
    Map<Integer, Subtask> subtaskMap;

    public TaskManager() {
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
    }

    public Task createTask(Task task) {
        task.setId(id++);
        taskMap.put(task.getId(), task);

        return task;
    }

    public Epic createEpic(Epic epicTask) {
        epicTask.setId(id++);
        epicMap.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public Subtask createSubtask(Subtask subtask, Integer epicId) {
        if (!epicMap.containsKey(epicId)) {
            System.out.println("Такое id в epic отсутствует");
        }
        subtask.setId(id++);
        subtaskMap.put(subtask.getId(), subtask);
        Epic epic = epicMap.get(epicId);
        System.out.println("!!!!" + epicMap);
        System.out.println(epicId);
        List<Subtask> subtasks = epic.getSubtasks();
        subtasks.add(subtask);

        subtask.setEpic(epic);
        return subtask;
    }

    public void showTask() {
        if (!taskMap.isEmpty()) {
            for (Task task : taskMap.values()) {
                System.out.println("Список задач категории task " + task + ".");
            }
        } else {
            System.out.println("Список пуст");
        }
    }

    public void showEpic() {
        if (!epicMap.isEmpty()) {
            for (Epic epic : epicMap.values()) {
                System.out.println("Список задач категории epic " + epic + ".");
            }
        } else {
            System.out.println("Список пуст");
        }
    }

    public void showSubtask() {
        for (Subtask subtask : subtaskMap.values()) {
            System.out.println("Список задач категории 'подзадачи'" + subtask + ".");
        }
    }

    public void deleteAllTasks() {
        taskMap.clear();
        System.out.println("Задачи удалены.");
    }

    public void deleteAllEpics() {
        epicMap.clear();
        subtaskMap.clear();
        System.out.println("Epic-задачи удалены.");
    }

    public void deleteAllSubtasks() {
        subtaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.getSubtasks().clear();
            epic.setStatus(Status.NEW);
        }
        System.out.println("Подзадачи удалены.");
    }

    public void showByIdTask(Integer id) {
        System.out.println("Задача c id " + id + " " + taskMap.get(id) + ".");
    }

    public void showByIdEpic(Integer id) {

        System.out.println("Epic-задача с id" + id + " " + epicMap.get(id) + ".");
    }

    public void showByIdSubtask(Integer id) {

        System.out.println("Подзадача " + id + " " + subtaskMap.get(id) + ".");
    }

    public void updateTask(Task updateTasks) { //есть ли такой id
        Task task = taskMap.get(updateTasks.getId());
        task.setName(updateTasks.getName());
        task.setDescription(updateTasks.getDescription());
        task.setStatus(updateTasks.getStatus());
    }

    public void updateEpic(Epic updateEpic) {
        Epic epic = epicMap.get(updateEpic.getId());
        if (epic != null) {
            epic.setName(updateEpic.getName());
            epic.setDescription(updateEpic.getDescription());
            updateEpicStatus(epic);
        }
    }
    public void updateSubtask(Subtask updateSubtask) {
        Subtask subtask = subtaskMap.get(updateSubtask.getId());
        subtask.setName(updateSubtask.getName());
        subtask.setDescription(updateSubtask.getDescription());
        subtask.setStatus(updateSubtask.getStatus());

        updateEpicStatus(subtask.getEpic());
    }

    public void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            boolean allNew = true;
            boolean allDone = true;

            for (Subtask subtask : subtasks) {
                if (!subtask.getStatus().equals(Status.NEW)) {
                    allNew = false;
                }
                if (!subtask.getStatus().equals(Status.DONE)) {
                    allDone = false;
                }
            }
            if (allNew) {
                epic.setStatus(Status.NEW);
            } else if (allDone) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    protected void deleteTaskById(Integer id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Такого id нет");
            return;
        }
        taskMap.remove(id);
        System.out.println("Задача с id " + id + " удалена.");
    }

    public void deleteEpicById(Integer id) {
        if (!epicMap.containsKey(id)) {
            System.out.println("Такого id нет");
            return;
        }
        epicMap.remove(id);
        System.out.println("Epic-задача с id" + id + "удалена.");
    }

    public void deleteSubtaskById(Integer id) {
        if (!subtaskMap.containsKey(id)) {
            System.out.println("Такого id нет");
            return;
        }
        subtaskMap.remove(id);
        System.out.println("Подзадача удалена.");
    }

    public List<Subtask> showSubtaskFromEpic(Integer epicId) {
        return epicMap.get(epicId).getSubtasks();
    }
}