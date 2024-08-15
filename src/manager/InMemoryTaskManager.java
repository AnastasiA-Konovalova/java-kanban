package manager;

import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 1;
    private Map<Integer, Task> taskMap;
    private Map<Integer, Epic> epicMap;
    private Map<Integer, Subtask> subtaskMap;
    HistoryManager historyManager; //InMemoryTaskManager обращается к менеджеру истории через интерфейс HistoryManager

    // и использует реализацию, которую возвращает метод getDefaultHistory.
    public InMemoryTaskManager() {
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
        if (historyManager.getHistory().isEmpty()) {
            System.out.println("Список просмотренных задач пуст");
            return List.of();
        } else {
            while (historyManager.getHistory().size() > 10) {
                historyManager.getHistory().remove(0);
            }
            System.out.println("Список просмотренных задач");
            for (Task tasks : historyManager.getHistory()) {
                System.out.println("Задача " + tasks);
            }
        }
        return historyManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(id++);
        taskMap.put(task.getId(), task);

        return task;
    }

    @Override
    public Epic createEpic(Epic epicTask) {
        epicTask.setId(id++);
        epicMap.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask, Integer epicId) {
        if (!epicMap.containsKey(epicId)) {
            System.out.println("Такой id в epic отсутствует.");
        } else {
            subtask.setId(id++);
            subtaskMap.put(subtask.getId(), subtask);
            Epic epic = epicMap.get(epicId);
            epic.addSubtask(subtask);

            updateEpicStatus(epic);
            subtask.setEpic(epic);
        }
        return subtask;
    }

    @Override
    public List<Task> getTaskList() {
        if (!taskMap.isEmpty()) {
            System.out.println("Список задач категории task.");
            return new ArrayList<>(taskMap.values());
        } else {
            System.out.println("Список пуст");
            return List.of();
        }
    }

    @Override
    public List<Epic> getEpicList() {
        if (!epicMap.isEmpty()) {
            System.out.println("Список задач категории epic.");
            List<Epic> a = new ArrayList<>(epicMap.values());
            System.out.println(a);
            return new ArrayList<>(epicMap.values());
        } else {
            System.out.println("Список пуст");
            return List.of();
        }
    }

    @Override
    public List<Subtask> getSubtaskList() {
        if (!epicMap.isEmpty()) {
            System.out.println("Список задач категории 'подзадачи'.");
            return new ArrayList<>(subtaskMap.values());
        } else {
            System.out.println("Список пуст");
            return List.of();
        }
    }

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
        System.out.println("Задачи удалены.");
    }

    @Override
    public void deleteAllEpics() {
        epicMap.clear();
        subtaskMap.clear();
        System.out.println("tasks.Epic-задачи удалены.");
    }

    @Override
    public void deleteAllSubtasks() {
        subtaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.deleteAllSubtasks();
        }
        System.out.println("Подзадачи удалены.");
    }

    @Override
    public Task getByIdTask(Integer id) {
        System.out.println("Задача c id " + id + ".");
        historyManager.getHistory().add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getByIdEpic(Integer id) {
        System.out.println("tasks.Epic-задача с id " + id + ".");
        historyManager.getHistory().add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getByIdSubtask(Integer id) {
        System.out.println("Подзадача " + id + ".");
        historyManager.getHistory().add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public void updateTask(Task updateTasks) {
        Task task = taskMap.get(updateTasks.getId());
        task.setName(updateTasks.getName());
        task.setDescription(updateTasks.getDescription());
        task.setStatus(updateTasks.getStatus());
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        Epic epic = epicMap.get(updateEpic.getId());
        if (epic != null) {
            epic.setName(updateEpic.getName());
            epic.setDescription(updateEpic.getDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        Subtask subtask = subtaskMap.get(updateSubtask.getId());
        subtask.setName(updateSubtask.getName());
        subtask.setDescription(updateSubtask.getDescription());
        subtask.setStatus(updateSubtask.getStatus());

        updateEpicStatus(subtask.getEpic());
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            boolean allNew = true;
            boolean allDone = true;

            for (Integer subtask : subtasks) {
                if (!subtaskMap.get(subtask).getStatus().equals(Status.NEW)) {
                    allNew = false;
                }
                if (!subtaskMap.get(subtask).getStatus().equals(Status.DONE)) {
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

    @Override
    public void deleteTaskById(Integer id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Такого id нет");
            return;
        }
        taskMap.remove(id);
        System.out.println("Задача с id " + id + " удалена.");
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (!epicMap.containsKey(id)) {
            System.out.println("Такого id нет");
            return;
        }
        Epic removeEpic = epicMap.remove(id);
        List<Integer> subtasks = removeEpic.getSubtasks();

        for (Integer subtask : subtasks) {
            subtaskMap.remove(subtask);
        }

        System.out.println("tasks.Epic-задача с id " + id + " удалена.");
    }

    @Override
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

    public List<Subtask> getSubtaskFromEpic(Integer epicId) {
        Epic epic = epicMap.get(epicId);

        if (epic == null) {
            return List.of();
        }
        List<Subtask> idSubtasksList = new ArrayList<>();
        for (Integer idSubtask : epic.getSubtasks()) {
            Subtask subtask = subtaskMap.get(idSubtask);
            if (subtask != null) {
                idSubtasksList.add(subtask);
            }
        }
        return idSubtasksList;
    }
}