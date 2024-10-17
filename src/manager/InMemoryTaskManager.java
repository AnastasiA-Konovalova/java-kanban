package manager;

import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

public class InMemoryTaskManager implements TaskManager {
    protected Integer id = 1;
    protected Map<Integer, Task> taskMap;
    protected Map<Integer, Epic> epicMap;
    protected Map<Integer, Subtask> subtaskMap;
    private HistoryManager historyManager;
    private Set<Task> sortedSet = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    });

    public InMemoryTaskManager() {
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
        System.out.println("Список просмотренных задач ");
        historyManager.getHistory()
                .forEach(task -> System.out.println("Задача " + historyManager.getHistory()));

        return historyManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        if (!validateTask(task)) {
            task.setId(id++);
            taskMap.put(task.getId(), task);
        } else {
            throw new RuntimeException("Создаваемая задача пересекается по времени с существующей");
        }
        if (task.getStartTime() != null) {
            sortedSet.add(task);
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epicTask) {
        epicTask.setId(id++);
        epicMap.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask == null) {
            System.out.println("В качестве subtask передан null.");
            return null;
        }
        Epic epic = subtask.getEpic();
        if (epic == null || !epicMap.containsKey(epic.getId())) {
            System.out.println("Такой epic не существует.");
        }
        if (!validateTask(subtask)) {
            subtask.setId(id++);
            subtaskMap.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);

            updateEpicStatus(epic);
            subtask.setEpic(epic);
        } else {
            throw new RuntimeException("Создаваемая задача пересекается по времени с существующей");
        }
        if (subtask.getStartTime() != null) {
            sortedSet.add(subtask);
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
        List<Integer> taskId = new ArrayList<>(taskMap.keySet());
        taskId.forEach(integerId -> historyManager.remove(integerId));
        taskId.forEach(integerId -> sortedSet.remove(taskMap.get(integerId)));
        taskMap.clear();
        System.out.println("Задачи удалены.");
    }

    @Override
    public void deleteAllEpics() {
        List<Integer> epicId = new ArrayList<>(epicMap.keySet());
        epicId.forEach(integerId -> historyManager.remove(integerId));
        List<Integer> subtaskId = new ArrayList<>(subtaskMap.keySet());
        subtaskId.forEach(integerId -> historyManager.remove(integerId));

        epicMap.clear();
        subtaskMap.clear();
        System.out.println("Epic-задачи удалены.");
    }

    @Override
    public void deleteAllSubtasks() {
        List<Integer> subtaskId = new ArrayList<>(subtaskMap.keySet());
        subtaskId.forEach(integerId -> historyManager.remove(integerId));
        subtaskId.forEach(integerId -> sortedSet.remove(subtaskMap.get(integerId)));
        subtaskMap.clear();
        epicMap.values().forEach(Epic::deleteAllSubtasks);
        System.out.println("Подзадачи удалены.");
    }

    @Override
    public Task getByIdTask(Integer id) {
        System.out.println("Задача c id " + id + ".");
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getByIdEpic(Integer id) {
        System.out.println("tasks.Epic-задача с id " + id + ".");
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getByIdSubtask(Integer id) {
        System.out.println("Подзадача " + id + ".");
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public void updateTask(Task oldTask, Task updateTask) {
        if (taskMap.containsKey(oldTask.getId())) {
            Task task = taskMap.get(oldTask.getId());
            if (task.getStartTime() != null) {
                sortedSet.remove(task);
            }
            if (!validateTask(updateTask)) {
                taskMap.put(oldTask.getId(), updateTask);
                historyManager.remove(task.getId());
                updateTask.setId(oldTask.getId());
                if (oldTask.getEndTime() != null) {
                    sortedSet.add(oldTask);
                }
            } else {
                if (task.getEndTime() != null) {
                    sortedSet.add(task);
                }
            }
        } else {
            throw new RuntimeException("Задачи по такому id нет");
        }
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
    public void updateSubtask(Subtask subtask, Subtask updateSubtask) {
        if (subtaskMap.containsKey(subtask.getId())) {
            Subtask tmpSub = subtaskMap.get(subtask.getId());
            if (tmpSub.getStartTime() != null) {
                sortedSet.remove(tmpSub);
            }
            if (!validateTask(updateSubtask)) {
                subtaskMap.put(subtask.getId(), updateSubtask);
                historyManager.remove(subtask.getId());
                updateSubtask.setId(subtask.getId());

                if (subtask.getEndTime() != null) {
                    sortedSet.add(subtask);
                }
            } else {
                if (tmpSub.getEndTime() != null) {
                    sortedSet.add(tmpSub);
                }
            }
        } else {
            throw new RuntimeException("Задачи по такому id нет");
        }
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            boolean allNew = subtasks.stream()
                    .allMatch(subtask -> subtaskMap.get(subtask).getStatus().equals(Status.NEW));
            boolean allDone = subtasks.stream()
                    .allMatch(subtask -> subtaskMap.get(subtask).getStatus().equals(Status.DONE));

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
        historyManager.remove(id);
        if (taskMap.get(id).getStartTime() != null) {
            sortedSet.remove(taskMap.get(id));
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

        subtasks.forEach(subtask -> {
            subtaskMap.remove(subtask);
            historyManager.remove(subtask);
        });

        historyManager.remove(id);
        System.out.println("tasks.Epic-задача с id " + id + " удалена.");
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (!subtaskMap.containsKey(id)) {
            System.out.println("Такого id нет.");
            return;
        }
        //Epic removeEpic = epicMap.remove(id);
        Subtask remove = subtaskMap.remove(id);
        Epic epic = remove.getEpic();
        epic.removeSubtask(remove);

        historyManager.remove(id);
        updateEpicStatus(epic);
        if (remove.getStartTime() != null) {
            sortedSet.remove(subtaskMap.get(id));
        }

        System.out.println("Подзадача удалена.");

//        Epic removeEpic = epicMap.remove(id);
//        Subtask remove = subtaskMap.remove(id);
//        Epic epic = remove.getEpic();
//        epic.deleteSubtaskById(remove);
//
//        updateEpicStatus(epic);
//        historyManager.remove(id);
//        System.out.println("Подзадача удалена.");
//    }

//            if (subtasks.get(id).getEndTime().isPresent())
//                prioritizedTasks.remove(subtasks.get(id));
//            SubTask subtask = subtasks.remove(id);
//            historyManager.remove(id);
//            if (subtask == null) {
//                return;
//            }
//            Epic epic = epics.get(subtask.getEpicId());
//            epic.removeSubtask(id);
//            updateEpicStatus(epic);
//            epic
    }

    @Override
    public List<Subtask> getSubtaskFromEpic(Integer epicId) {
        Epic epic = epicMap.get(epicId);
        if (epic == null) {
            return List.of();
        }
        List<Subtask> idSubtasksList = new ArrayList<>();
        epic.getSubtasks().stream()
                .map(subtaskId -> subtaskMap.get(subtaskId))
                .filter(Objects::nonNull)
                .forEachOrdered(idSubtasksList::add);
        return idSubtasksList;
    }

    public Instant getStartTimeForEpic(Epic epic) {
        return getSubtaskFromEpic(epic.getId())
                .stream()
                .map(Subtask::getStartTime)
                .min(Comparator.comparing(Function.identity()))
                .orElse(null);
    }

    public Duration getDurationForEpic(Epic epic) {
        return getSubtaskFromEpic(epic.getId())
                .stream()
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public Instant getEndTimeForEpic(Epic epic) {
        return getSubtaskFromEpic(epic.getId())
                .stream()
                .map(Subtask::getEndTime)
                .max(Comparator.comparing(Function.identity()))
                .orElse(null);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedSet);
    }

    public boolean validateTask(Task taskForCheck) {
        List<Task> sortedList = getPrioritizedTasks();
        if (taskForCheck.getStartTime() == null) {
            return false;
        }
        return sortedList.stream()
                .anyMatch(task ->
                        !taskForCheck.getStartTime().isAfter(task.getEndTime()) &&
                                !taskForCheck.getEndTime().isBefore(task.getStartTime())
                );
    }
}