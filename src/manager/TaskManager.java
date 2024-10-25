package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    List<Task> getTaskList();

    List<Epic> getEpicList();

    List<Subtask> getSubtaskList();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getByIdTask(Integer id);

    Epic getByIdEpic(Integer id);

    Subtask getByIdSubtask(Integer id);

    void updateTask(Task updateTasks);

    void updateEpic(Epic updateEpic);

    void updateSubtask(Subtask updateSubtask);

    Task deleteTaskById(Integer id);

    Epic deleteEpicById(Integer id);

    Subtask deleteSubtaskById(Integer id);

    List<Subtask> getSubtaskFromEpic(Integer epicId);

    List<Task> getPrioritizedTasks();
}
