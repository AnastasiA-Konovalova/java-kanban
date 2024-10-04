package saveTasks;

import exeption.ManagerSaveException;
import manager.InMemoryTaskManager;
import status.Status;
import taskType.TaskType;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;

    public FileBackedTaskManager(String fileName) throws IOException {
        this(Paths.get(fileName));
    }

    public FileBackedTaskManager(Path path) throws IOException {
        super();
        if (Files.exists(path)) {
            this.path = path;
        } else {
            this.path = Files.createFile(path);
        }
    }

    public void save() {
        List<String> allTasks = new ArrayList<>();
        allTasks.add(epicMap.toString());
        allTasks.add(subtaskMap.toString());

        System.out.println("File created: " + path.getFileName());

        try (FileWriter fileWriter = new FileWriter(path.toFile())) {
            fileWriter.write("id,type,name,status,description,epic\n");

            StringBuilder sb = new StringBuilder();
            for (Task item : taskMap.values()) {
                appendItemsToFile(sb, item);

                fileWriter.write(sb.toString());
                sb = new StringBuilder();
            }

            for (Epic item : epicMap.values()) {
                appendItemsToFile(sb, item);

                fileWriter.write(sb.toString());
                sb = new StringBuilder();
            }

            for (Subtask item : subtaskMap.values()) {
                sb = appendItemsToFile(sb, item);
                sb.append(item.getDescription()).append(",");
                sb.append(item.getEpic().getId()).append("\n");

                fileWriter.write(sb.toString());
                sb = new StringBuilder();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл " + e.getMessage());
        }
    }

    private StringBuilder appendItemsToFile(StringBuilder sb, Task item) {
        sb.append(item.getId()).append(",")
                .append(item.getClass().getSimpleName()).append(",")
                .append(item.getName()).append(",")
                .append(item.getStatus().toString()).append(",");
        if (!item.getClass().getSimpleName().equals("Subtask")) {
            sb.append(item.getDescription()).append("\n");
        }
        return sb;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file.getName());

        List<String> readFile = new ArrayList<>();
        List<String> splitList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                readFile.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла " + e.getMessage());
        }

        readFile.remove(0);

        for (int i = 0; i < readFile.size(); i++) {
            String[] split = readFile.get(i).split(",");
            if (TaskType.Task.name().equalsIgnoreCase(split[1])) {
                Task task = new Task();
                task.setId(Integer.parseInt(split[0]));
                task.setName(split[2]);
                task.setStatus(Status.valueOf(split[3]));
                task.setDescription(split[4]);

                taskManager.taskMap.put(task.getId(), task);
            }

            if (TaskType.Epic.name().equalsIgnoreCase(split[1])) {
                Epic epic = new Epic();
                epic.setId(Integer.parseInt(split[0]));
                epic.setName(split[2]);
                epic.setStatus(Status.valueOf(split[3]));
                epic.setDescription(split[4]);
                taskManager.epicMap.put(epic.getId(), epic);
            }

            if (TaskType.Subtask.name().equalsIgnoreCase(split[1])) {
                Subtask subtask = new Subtask();
                subtask.setId(Integer.parseInt(split[0]));
                subtask.setName(split[2]);
                subtask.setStatus(Status.valueOf(split[3]));
                subtask.setDescription(split[4]);

                if (split.length > 5) {
                    for (int j = 5; j < split.length; j++) {
                        subtask.setEpic(taskManager.epicMap.get(Integer.parseInt(split[j])));
                        if (subtask.getEpic().getSubtasks() == null) {
                            subtask.getEpic().setSubtasks(new ArrayList<>(subtask.getId()));
                        } else {
                            System.out.println();
                        }
                        subtask.getEpic().addSubtask(subtask);
                        System.out.println("Epic " + subtask.getEpic());
                    }
                    taskManager.subtaskMap.put(subtask.getId(), subtask);
                }
            }
        }
        return taskManager;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epicTask) {
        super.createEpic(epicTask);
        save();
        return epicTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
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
}