import java.util.*;

public class TaskManager {
    private Integer id = 1;
    private Map<Integer, Task> taskMap;
    private Map<Integer, Epic> epicMap;
    private Map<Integer, Subtask> subtaskMap;

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
            System.out.println("Такой id в epic отсутствует.");
        } else {
            subtask.setId(id++);
            subtaskMap.put(subtask.getId(), subtask);
            Epic epic = epicMap.get(epicId);
            epic.addSubtask(subtask);

            epic.setStatus(Status.NEW);
            subtask.setEpic(epic);
        }
        return subtask;
    }

    public Map<Integer, Task> showTask() {
        if (!taskMap.isEmpty()) {
            System.out.println("Список задач категории task.");
            return taskMap;
        } else {
            System.out.println("Список пуст");
            return null;
        }
    }

    public Map<Integer, Epic> showEpic() {
        if (!epicMap.isEmpty()) {
            System.out.println("Список задач категории epic.");
            return epicMap;
        } else {
            System.out.println("Список пуст");
            return null;
        }
    }

    public Map<Integer, Subtask> showSubtask() {
        if (!epicMap.isEmpty()) {
            System.out.println("Список задач категории 'подзадачи'.");
            return subtaskMap;
        } else {
            System.out.println("Список пуст");
            return null;
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
            epic.deleteAllSubtasks();
        }
        System.out.println("Подзадачи удалены.");
    }

    public Task showByIdTask(Integer id) {
        System.out.println("Задача c id " + id + ".");
        return taskMap.get(id);
    }

    public Epic showByIdEpic(Integer id) {
        System.out.println("Epic-задача с id " + id + ".");
        return epicMap.get(id);
    }

    public Subtask showByIdSubtask(Integer id) {
        System.out.println("Подзадача " + id + ".");
        return subtaskMap.get(id);
    }

    public void updateTask(Task updateTasks) {
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
        Epic removeEpic = epicMap.remove(id);
        subtaskMap.remove(removeEpic.getId());

        System.out.println("Epic-задача с id" + id + "удалена.");
    }

    public void deleteSubtaskById(Integer id) {
        if (!subtaskMap.containsKey(id)) {
            System.out.println("Такого id нет.");
            return;
        }
        Subtask remove = subtaskMap.remove(id);
        Epic epic = remove.getEpic();
        epic.deleteSubtaskById(remove);
        updateEpicStatus(epic);
        System.out.println("Подзадача удалена.");
    }

    public void getSubtaskFromEpic(Integer epicId) {
        System.out.println("Задачи категории 'subtask' определенного epic с id " + epicId + ".");
        System.out.println(epicMap.get(epicId).getSubtasks());
    }
}