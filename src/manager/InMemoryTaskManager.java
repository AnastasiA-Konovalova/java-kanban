package manager;

import exeptions.ManagerIntersectionException;
import exeptions.ManagerNotContainTaskException;
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
    private Set<Task> sortedSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        if (task == null || task.getName() == null || task.getDescription() == null) {
            throw new ManagerNotContainTaskException("Task пуст");
        }
        if (validateTaskForCreate(task)) {
            task.setId(id++);
            taskMap.put(task.getId(), task);
        } else {
            throw new ManagerIntersectionException("Создаваемая задача пересекается по времени с существующей");
        }
        if (task.getStartTime() != null) {
            sortedSet.add(task);
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epicTask) {
        if (epicTask == null) {
            throw new ManagerNotContainTaskException("Epic пуст");
        }
        if (epicTask.getName() == null || epicTask.getName().equals("null")) {
            throw new ManagerNotContainTaskException("Имя не должно быть null");
        }
        if (epicTask.getDescription() == null|| epicTask.getDescription().equals("null")) {
            throw new ManagerNotContainTaskException("Описание не должно быть null");
        }
        epicTask.setId(id++);
        epicMap.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask == null) {
            throw new ManagerNotContainTaskException("В качестве subtask передан null.");
        }
        Epic epic = subtask.getEpic();
        if (epic == null || !epicMap.containsKey(epic.getId())) {
            throw new ManagerNotContainTaskException("Такого epic не существует.");
        }
        if (validateTaskForCreate(subtask)) {
            subtask.setId(id++);
            subtaskMap.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
        } else {
            throw new ManagerIntersectionException("Создаваемая задача пересекается по времени с существующей");
        }
        if (subtask.getStartTime() != null) {
            sortedSet.add(subtask);
        }

        updateEpicStatus(epic);
        epic.setStartTime(getStartTimeForEpic(epic));
        epic.setDuration(getDurationForEpic(epic));
        epic.setEndTime(getEndTimeForEpic(epic));

        return subtask;
    }

    @Override
    public List<Task> getTaskList() {
        System.out.println("Список задач категории task.");
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getEpicList() {
        System.out.println("Список задач категории epic.");
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getSubtaskList() {
        System.out.println("Список задач категории 'подзадачи'.");
        return new ArrayList<>(subtaskMap.values());
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
        if (!taskMap.containsKey(id)) {
            throw new ManagerNotContainTaskException("Task с id " + id + " отсутствует в списке");
        }
        // TODO: 23.10.2024 убрать
        System.out.println(taskMap.get(id));
        //
        System.out.println("Задача c id " + id + ".");
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getByIdEpic(Integer id) {
        if (!epicMap.containsKey(id)) {
            throw new ManagerNotContainTaskException("Epic с id " + id + " отсутствует в списке");
        }
        System.out.println("Epic-задача с id " + id + ".");
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getByIdSubtask(Integer id) {
        if (!subtaskMap.containsKey(id)) {
            throw new ManagerNotContainTaskException("Subtask с id " + id + " отсутствует в списке");
        }
        System.out.println("Подзадача " + id + ".");
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public void updateTask(Task updateTask) {
        if (!taskMap.containsKey(updateTask.getId())) {
            throw new ManagerNotContainTaskException("Такой task отсутствует в списке");
        }
        Task task = taskMap.get(updateTask.getId());

        if (validateTask(updateTask)) {
            if (updateTask.getName() != null) {
                task.setName(updateTask.getName());
            }
            if (updateTask.getDescription() != null) {
                task.setDescription(updateTask.getDescription());
            }
            if (updateTask.getStatus() != null) {
                task.setStatus(updateTask.getStatus());
            }
            task.setStartTime(updateTask.getStartTime());
            task.setDuration(updateTask.getDuration());
        } else {
            throw new ManagerIntersectionException("Задачи пересекаются, обновленная задача не подлежит добавлению");
        }
        sortedSet.remove(task);
        if (task.getStartTime() != null) {
            sortedSet.add(task);
        }
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        if (!epicMap.containsKey(updateEpic.getId())) {
            throw new ManagerNotContainTaskException("Такой epic отсутсвует в списке");
        }
        Epic epic = epicMap.get(updateEpic.getId());
        if (epic != null) {
            epic.setName(updateEpic.getName());
            epic.setDescription(updateEpic.getDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        if (!subtaskMap.containsKey(updateSubtask.getId())) {
            throw new ManagerNotContainTaskException("Такой subtask отсутсвует в списке");
        }
        Subtask subtask = subtaskMap.get(updateSubtask.getId());
        if (updateSubtask.getEpic() != subtask.getEpic()) {
            throw new ManagerNotContainTaskException("Subtask не содержит такого эпика");
        }
        Epic epic = subtask.getEpic();

        if (validateTask(subtask)) {
            if (updateSubtask.getName() != null) {
                subtask.setName(updateSubtask.getName());
            }
            if (updateSubtask.getDescription() != null) {
                subtask.setDescription(updateSubtask.getDescription());
            }
            if (updateSubtask.getStatus() != null) {
                subtask.setStatus(updateSubtask.getStatus());
            }
            subtask.setStartTime(updateSubtask.getStartTime());
            subtask.setDuration(updateSubtask.getDuration());
        } else {
            throw new ManagerIntersectionException("Задачи пересекаются, обновленная задача не подлежит добавлению");
        }
        updateEpicStatus(epic);
        epic.setStartTime(getStartTimeForEpic(epic));
        epic.setDuration(getDurationForEpic(epic));
        epic.setEndTime(getEndTimeForEpic(epic));

        sortedSet.remove(subtask);
        if (subtask.getStartTime() != null) {
            sortedSet.add(subtask);
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
    public Task deleteTaskById(Integer id) {
        historyManager.remove(id);

        Task task = taskMap.remove(id);
        if (task == null) {
            return null;
        }

        if (task.getStartTime() != null) {
            sortedSet.remove(taskMap.get(id));
        }
        System.out.println("Задача с id " + id + " удалена.");
        return task;
    }

    @Override
    public Epic deleteEpicById(Integer id) {
        Epic removeEpic = epicMap.remove(id);
        if (removeEpic == null) {
            return null;
        }

        List<Integer> subtasks = removeEpic.getSubtasks();

        subtasks.forEach(subtask -> {
            subtaskMap.remove(subtask);
            historyManager.remove(subtask);
        });

        historyManager.remove(id);
        System.out.println("tasks.Epic-задача с id " + id + " удалена.");
        return removeEpic;
    }

    @Override
    public Subtask deleteSubtaskById(Integer id) {
        Subtask removeSubtask = subtaskMap.remove(id);
        if (removeSubtask == null) {
            return null;
        }
        Epic epic = removeSubtask.getEpic();
        epic.removeSubtask(removeSubtask);

        historyManager.remove(id);
        updateEpicStatus(epic);
        if (removeSubtask.getStartTime() != null) {
            sortedSet.remove(removeSubtask);
        }
        System.out.println("Подзадача удалена.");
        return removeSubtask;
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
                .filter(Objects::nonNull)
                .min(Comparator.comparing(Function.identity()))
                .orElse(null);
    }

    public Duration getDurationForEpic(Epic epic) {
        return getSubtaskFromEpic(epic.getId())
                .stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public Instant getEndTimeForEpic(Epic epic) {
        return getSubtaskFromEpic(epic.getId())
                .stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
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
            return true;
        }
        if (sortedList.isEmpty()) {
            return true;
        }
        if (sortedList.size() == 1 && sortedList.get(0).getId() == taskForCheck.getId()) {
            return true;
        }
        return sortedList.stream()
                .filter(task -> task.getId() != taskForCheck.getId())
                .anyMatch(task ->
                        taskForCheck.getStartTime().isAfter(task.getEndTime()) ||
                                taskForCheck.getEndTime().isBefore(task.getStartTime())
                );
    }

    public boolean validateTaskForCreate(Task taskForCheck) {
        List<Task> sortedList = getPrioritizedTasks();
        if (taskForCheck.getStartTime() == null) {
            return true;
        }
        if (sortedList.isEmpty()) {
            return true;
        }
        return sortedList.stream()
                .anyMatch(task ->
                        taskForCheck.getStartTime().isAfter(task.getEndTime()) ||
                                taskForCheck.getEndTime().isBefore(task.getStartTime())
                );
    }
}
