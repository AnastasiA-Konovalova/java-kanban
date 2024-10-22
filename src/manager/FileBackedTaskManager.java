package manager;

import exeptions.ManagerSaveException;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasktype.TaskType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;

    public FileBackedTaskManager(String fileName) {
        this(Paths.get(fileName));
    }

    public FileBackedTaskManager(Path path) {
        super();
        this.path = path;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(path.toFile())) {
            fileWriter.write("id,type,name,status,description,startTime,duration,endTime,epic\n");
            taskMap.values()
                    .forEach(ThrowableConsumer.of(item -> fileWriter.write(appendItemsToFile(item).toString()),
                            ex -> new ManagerSaveException("Ошибка в чтении задачи формата task")));

            epicMap.values()
                    .forEach(ThrowableConsumer.of(item -> fileWriter.write(appendItemsToFile(item).toString()),
                            ex -> new ManagerSaveException("Ошибка в чтении задачи формата epic")));

            subtaskMap.values().forEach(ThrowableConsumer.of(
                    item -> {
                        StringBuilder sb = appendItemsToFile(item);
                        sb.append(item.getEpic().getId()).append("\n");
                        fileWriter.write(sb.toString());
                    },
                    ex -> new ManagerSaveException("Ошибка в чтении задачи формата subtask")
            ));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения " + e.getMessage());
        }
    }

    private StringBuilder appendItemsToFile(Task item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getId()).append(",")
                .append(item.getClass().getSimpleName()).append(",")
                .append(item.getName()).append(",")
                .append(item.getStatus().toString()).append(",")
                .append(item.getDescription());

        if ((item.getClass().getSimpleName().equalsIgnoreCase("Task")
                || item.getClass().getSimpleName().equalsIgnoreCase("Epic"))
                && item.getStartTime() != null) {
            sb.append("," + item.getStartTime()).append(",");
            sb.append(item.getDuration()).append(",");
            sb.append(item.getEndTime()).append("\n");
        } else if (item.getClass().getSimpleName().equals("Subtask")) {
            sb.append(",").append(item.getStartTime()).append(",");
            sb.append(item.getDuration()).append(",");
            sb.append(item.getEndTime()).append(",");
        } else if (item.getStartTime() == null) {
            sb.append("\n");
        }
        return sb;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file.getName());

        List<String> readFile = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.lines().forEach(readFile::add);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла " + e.getMessage());
        }

        if (readFile.isEmpty()) {
            return taskManager;
        }

        readFile.remove(0);
        int maxId = 1;
        for (int i = 0; i < readFile.size(); i++) {
            String[] split = readFile.get(i).split(",");
            int id = Integer.parseInt(split[0]);
            if (maxId < id) {
                maxId = id;
            }
            String name = split[2];
            Status status = Status.valueOf(split[3]);
            String description = split[4];

            if (TaskType.Task.name().equalsIgnoreCase(split[1])) {
                Task task = new Task();
                task.setId(id);
                task.setName(name);
                task.setStatus(status);
                task.setDescription(description);

                if (split.length > 5) {
                    loadStartTimeAndDuration(task, split);
                }
                taskManager.validateTask(task);
                taskManager.taskMap.put(task.getId(), task);
            }

            if (TaskType.Epic.name().equalsIgnoreCase(split[1])) {
                Epic epic = new Epic();
                epic.setId(id);
                epic.setName(name);
                epic.setStatus(status);
                epic.setDescription(description);

                taskManager.epicMap.put(epic.getId(), epic);
            }

            if (TaskType.Subtask.name().equalsIgnoreCase(split[1])) {
                Subtask subtask = new Subtask();
                subtask.setId(id);
                subtask.setName(name);
                subtask.setStatus(status);
                subtask.setDescription(description);

                if (split[5].equals("null") || split[6].equals("null")) {
                    subtask.setStartTime(null);
                    subtask.setDuration(null);

                } else {
                    loadStartTimeAndDuration(subtask, split);
                }

                subtask.setEpic(taskManager.epicMap.get(Integer.parseInt(split[8])));
                if (subtask.getEpic().getSubtasks() == null) {
                    subtask.getEpic().setSubtasks(new ArrayList<>(subtask.getId()));
                }
                subtask.getEpic().addSubtask(subtask);
                taskManager.validateTask(subtask);
                taskManager.subtaskMap.put(subtask.getId(), subtask);
            }
        }
        taskManager.id = maxId + 1;
        return taskManager;
    }

    private static void loadStartTimeAndDuration(Task task, String[] split) {
        String startTime = "";
        String duration = "";
        if (split.length > 5) {
            startTime = split[5];
            duration = split[6];
        }

        Instant instantStartTime = Instant.parse(startTime);
        Duration durationParse = Duration.parse(duration);
        task.setStartTime(instantStartTime);
        task.setDuration(durationParse);
    }

    @Override
    public Task createTask(Task task) {
        Task savedTask = super.createTask(task);
        save();
        return savedTask;
    }

    @Override
    public Epic createEpic(Epic epicTask) {
        Epic savedEpic = super.createEpic(epicTask);
        save();
        return savedEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask saveSubtask = super.createSubtask(subtask);
        save();
        return saveSubtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void updateTask(Task updateTasks) {
        super.updateTask(updateTasks);
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
        save();
    }
}