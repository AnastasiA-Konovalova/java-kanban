package main;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        //инициализация объекта
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        //tasks.Task
        Task task1 = new Task("Task_1", "Go away");
        Task task2 = new Task("Task_2", "Cook");
        System.out.println("Create task1");
        Task task3 = inMemoryTaskManager.createTask(task1);// create
        System.out.println(task3);
        System.out.println("Create task2");
        Task task4 = inMemoryTaskManager.createTask(task2);
        System.out.println(task4);

        //inMemoryTaskManager.getByIdTask(1);
        //inMemoryTaskManager.getByIdTask(2);

        //taskManager.deleteAllTasks();

        System.out.println("Change task1");
        inMemoryTaskManager.updateTask(task3);
        System.out.println("Change task2");
        task4.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task4);

        //inMemoryTaskManager.deleteTaskById(2);
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(task4);


        //tasks.Epic
        Epic epic1 = new Epic("Travel", "Buy everything");
        epic1.setName("Epic_1");
        Epic epic2 = new Epic("Epic_2", "Cut carrot");
        System.out.println("Create epic1");
        Epic epic3 = inMemoryTaskManager.createEpic(epic1);
        System.out.println("Create task2");
        Epic epic4 = inMemoryTaskManager.createEpic(epic2);

        //System.out.println(inMemoryTaskManager.getByIdEpic(3));
        //taskManager.deleteAllEpics();

        System.out.println("Change epic1");
        inMemoryTaskManager.updateEpic(epic3);
        System.out.println("Change epic2");
        inMemoryTaskManager.updateEpic(epic4);

        //taskManager.deleteEpicById(3);
        System.out.println(inMemoryTaskManager.getEpicList());


        //tasks.Subtask
        Subtask subtask1 = new Subtask("Subtask_1", "Plane", epic3);
        Subtask subtask2 = new Subtask("Subtask_2", "Reserve", epic4);

        System.out.println("Создание подзадачи subtask1");
        System.out.println(inMemoryTaskManager.createSubtask(subtask1));
        System.out.println("Создание подзадачи subtask2");
        System.out.println(inMemoryTaskManager.createSubtask(subtask2));

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);

        //inMemoryTaskManager.getByIdSubtask(subtask1.getId());
        //taskManager.deleteAllSubtasks();

        System.out.println("Change subtask1");
        inMemoryTaskManager.updateSubtask(subtask1);
        inMemoryTaskManager.getEpicList();
        System.out.println("Change subtask2");
        inMemoryTaskManager.updateSubtask(subtask2);

        inMemoryTaskManager.getEpicList();

        //taskManager.deleteSubtaskById(3);

        //taskManager.showSubtask();
        //taskManager.deleteAllSubtasks();
        //taskManager.showEpic();
        System.out.println("Show subtasks from tasks.Epic");
        System.out.println(inMemoryTaskManager.getSubtaskFromEpic(epic3.getId()));
        System.out.println();

        //taskManager.deleteSubtaskById(6);
        //проверка метода deleteEpicById();
        System.out.println(inMemoryTaskManager.getEpicList());
        //inMemoryTaskManager.deleteEpicById(3);
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println(inMemoryTaskManager.getSubtaskList());

        //проверка метода getSubtaskFromEpic (предварительно закомментировать taskManager.deleteEpicById(3);)
        //System.out.println(taskManager.getSubtaskFromEpic(3));

        //................Проверка истории..............
        //открываем задачи
        System.out.println("-------------------------------------------------------------------");
        inMemoryTaskManager.getByIdTask(1);
        inMemoryTaskManager.getByIdTask(2);
        inMemoryTaskManager.getByIdEpic(3);
        inMemoryTaskManager.getByIdEpic(4);
        inMemoryTaskManager.getByIdSubtask(5);
        inMemoryTaskManager.getByIdSubtask(6);
//        inMemoryTaskManager.getByIdTask(1);
//        inMemoryTaskManager.getByIdTask(2);
//        inMemoryTaskManager.getByIdEpic(3);
//        inMemoryTaskManager.getByIdEpic(4);
//        inMemoryTaskManager.getByIdSubtask(5);
//        inMemoryTaskManager.getByIdSubtask(6);

        //inMemoryTaskManager.getHistory();



        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

//        historyManager.add(epic1);
//        historyManager.add(epic2);
//        historyManager.add(epic2);
//        historyManager.add(task1);
//        historyManager.add(task2);
//        historyManager.add(task2);
//        historyManager.linkAddLast(subtask1);
//        historyManager.linkAddLast(subtask2);
//        historyManager.linkAddLast(epic1);
//        historyManager.linkAddLast(task1);
        //System.out.println(historyManager.getTasks());
        System.out.println("History1" + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.deleteAllTasks();
        //inMemoryTaskManager.deleteTaskById(task1.getId());
        System.out.println("History2" + inMemoryTaskManager.getHistory());
        List<Task> history = inMemoryTaskManager.getHistory();
        System.out.println(history.size());
//просмотренные задачи (удаление всех)
        inMemoryTaskManager.deleteAllTasks();
        System.out.println(inMemoryTaskManager.getTaskList());
        inMemoryTaskManager.getHistory();

//        inMemoryTaskManager.deleteAllEpics();
//        System.out.println(inMemoryTaskManager.getEpicList());
//        inMemoryTaskManager.getHistory();

        inMemoryTaskManager.deleteAllSubtasks();
        System.out.println(inMemoryTaskManager.getSubtaskList());
        inMemoryTaskManager.getHistory();

        //historyManager.removeNode();
//        InMemoryTaskManager inMemoryTaskManager1 = new InMemoryTaskManager();
//        Epic epic5 = new Epic("NameEpic1", "DescriptionEpic1");
//        epic5.setId(4);
//        Subtask subtask5 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic5);
//        inMemoryTaskManager1.createSubtask(subtask5, 4);
    }
}