package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Set;

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

    void deleteTaskById(Integer id);

    void deleteEpicById(Integer id);

    void deleteSubtaskById(Integer id);

    List<Subtask> getSubtaskFromEpic(Integer epicId);
    Set<Task> getPrioritizedTasks(); //убрать?

    boolean validateTask(Task task); // убрать?
}
