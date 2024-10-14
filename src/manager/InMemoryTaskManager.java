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
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected Integer id = 1;
    protected Map<Integer, Task> taskMap;
    protected Map<Integer, Epic> epicMap;
    protected Map<Integer, Subtask> subtaskMap;
    private HistoryManager historyManager;
    private Set<Task> sortedSet;

    public InMemoryTaskManager() {
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
        System.out.println("Список просмотренных задач ");
        historyManager.getHistory().stream()
                .forEach(task -> System.out.println("Задача " + historyManager.getHistory()));

        return historyManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        System.out.println("boolean " + validateTask(task));
        if (!validateTask(task)) {
            task.setId(id++);
            taskMap.put(task.getId(), task);
        } else {
            System.out.println("Создаваемая задача пересекается по времени с существующей");
            return null;
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
            System.out.println("Создаваемая задача пересекается по времени с существующей");
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
        taskId.stream().forEach(integerId -> historyManager.remove(integerId));
        taskMap.clear();
        System.out.println("Задачи удалены.");
    }

    @Override
    public void deleteAllEpics() {
        List<Integer> epicId = new ArrayList<>(epicMap.keySet());
        epicId.stream().forEach(integerId -> historyManager.remove(integerId));
        List<Integer> subtaskId = new ArrayList<>(subtaskMap.keySet());
        subtaskId.stream().forEach(integerId -> historyManager.remove(integerId));
        epicMap.clear();
        subtaskMap.clear();
        System.out.println("tasks.Epic-задачи удалены.");
    }

    @Override
    public void deleteAllSubtasks() {
        List<Integer> subtaskId = new ArrayList<>(subtaskMap.keySet());
//        for (Integer integerId : taskId) {
//            historyManager.remove(integerId);
//        }
        subtaskId.stream().forEach(integerId -> historyManager.remove(integerId));
        subtaskMap.clear();
        epicMap.values().stream().forEach(Epic::deleteAllSubtasks);
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
    public void updateTask(Task updateTasks) {
        if (!validateTask(updateTasks)) {
            Task task = taskMap.get(updateTasks.getId());
            task.setName(updateTasks.getName());
            task.setDescription(updateTasks.getDescription());
            task.setStatus(updateTasks.getStatus());
        } else {
            System.out.println("Создаваемая задача пересекается по времени с существующей");
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
    public void updateSubtask(Subtask updateSubtask) {
        if (!validateTask(updateSubtask)) {
            Subtask subtask = subtaskMap.get(updateSubtask.getId());
            subtask.setName(updateSubtask.getName());
            subtask.setDescription(updateSubtask.getDescription());
            subtask.setStatus(updateSubtask.getStatus());

            updateEpicStatus(subtask.getEpic());
        } else {
            System.out.println("Создаваемая задача пересекается по времени с существующей");
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
//            boolean allNew = true;
//            boolean allDone = true;
//
//            for (Integer subtask : subtasks) {
//                if (!subtaskMap.get(subtask).getStatus().equals(Status.NEW)) {
//                    allNew = false;
//                }
//                if (!subtaskMap.get(subtask).getStatus().equals(Status.DONE)) {
//                    allDone = false;
//                }
//            }

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

        subtasks.stream().forEach(subtask -> {
            subtaskMap.remove(subtask);
            historyManager.remove(subtask);
        });
//        for (Integer subtask : subtasks) {
//            subtaskMap.remove(subtask);
//            historyManager.remove(subtask);
//        }
        historyManager.remove(id);

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
        historyManager.remove(id);
        System.out.println("Подзадача удалена.");
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
//        for (Integer idSubtask : epic.getSubtasks()) {
//            Subtask subtask = subtaskMap.get(idSubtask);
//            if (subtask != null) {
//                idSubtasksList.add(subtask);
//            }
//        }
        return idSubtasksList;
    }

    public void getStartTimeForEpic(Epic epic) { // list subtask contains Subtask that epic is including
        List<Integer> subtasksId = epic.getSubtasks();
        List<Subtask> subtasks = new ArrayList<>();

        subtasksId.stream()
                .filter(subtaskMap::containsKey)
                .map(subtaskMap::get)
                .forEachOrdered(subtasks::add);

        List<Instant> instants = new ArrayList<>();
        subtasks.stream()
                .map(Subtask::getStartTime)
                .forEachOrdered(instants::add);

        Instant min = instants.get(0);
        for (int i = 1; i < instants.size(); i++) {
            if (instants.get(i).isBefore(min)) {
                min = instants.get(i);
            }
        }
        epic.setStartTime(min);
        System.out.println("Min start time " + min);
        //временный цикл для ввода стартового времени сабтаск, удалить
        for (Subtask subtask : subtasks) {
            System.out.println(subtask.getStartTime());
        }
    }

    public void getDurationForEpic(Epic epic) { //этот метод и прошлый похожи, вынести часть в отдельный
        List<Integer> subtasksId = epic.getSubtasks();
        List<Subtask> subtasks = new ArrayList<>();
        subtasksId.stream()
                .filter(subtaskMap::containsKey)
                .map(subtaskMap::get)
                .forEachOrdered(subtasks::add);

        Duration totalDuration = subtasks.stream()
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus); // Суммируем все Duration
        System.out.println("Duration all subtasks" + totalDuration);
        epic.setDuration(totalDuration);

        //временный цикл для ввода стартового времени сабтаск, удалить
        for (Subtask subtask : subtasks) {
            System.out.println(subtask.getDuration());
        }
    }

    public Instant getEndTimeForEpic(Epic epic) {
        List<Integer> subtasksId = epic.getSubtasks();
        List<Subtask> subtasks = new ArrayList<>();
        subtasksId.stream()
                .filter(subtaskMap::containsKey)
                .map(subtaskMap::get)
                .forEachOrdered(subtasks::add);
        //System.out.println(subtasks + " subtask");

        Instant endTime = subtasks.get(0).getEndTime();
        for (int i = 0; i < subtasks.size(); i++) {
            subtasks.get(i).getStartTime().plus(subtasks.get(i).getDuration());
            if (subtasks.get(i).getEndTime().isAfter(endTime)) {
                endTime = subtasks.get(i).getEndTime();
            }
        }
        epic.setEndTime(endTime);
        System.out.println("Epic endTime" + epic.getEndTime());

        //временный цикл для ввода end времени сабтаск, удалить
        for (Subtask subtask : subtasks) {
            System.out.println(subtask.getEndTime());
        }
        return endTime;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        List<Task> tasks = getTaskList();
        List<Task> taskList = new ArrayList<>();
        tasks.stream().filter(task -> task.getStartTime() != null).forEachOrdered(taskList::add);

        List<Subtask> subtasks = getSubtaskList();
        List<Subtask> subtaskList = new ArrayList<>();
        subtasks.stream().filter(subtask -> subtask.getStartTime() != null).forEachOrdered(subtaskList::add);
        sortedSet = new TreeSet<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        sortedSet.addAll(taskList);
        sortedSet.addAll(subtaskList);
        //System.out.println("Sorted " + sortedSet);

        return sortedSet;
    }

    @Override
    public boolean validateTask(Task taskForCheck) {
        Set<Task> sortedList = getPrioritizedTasks();
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

//    1   s1________________e1                    startTime1 before startTime2
// 2         s2_________________e2            endTime1 before endTime2, but should be before start time
    /*if (startTime1.isBefore(startTime2) && (endTime1.isBefore(endTime2)) {
    return true;
    }
    */
// 1        s1__________________e1            startTime1 after startTime2
//2  s2_________________e2                    startTime1 before endTime2 but should be after
    /*if (startTime1.isAfter(startTime2) && (startTime1.isBefore(endTime2)) {
    return true;
    }
    */


//1        s1____________e1                   startTime1 after startTime2 and endTime1 before endTime2
//2  s1_________________________e1            startTime2 before startTime1 and endTime2 after endTime1
    /*if (startTime1.isAfter(startTime2) && (endTime1.isBefore(endTime2) && (startTime2.isBefore(startTime1) &&
    (endTime2.isAfter(endTime2)) {
    return true;
    }
    */


