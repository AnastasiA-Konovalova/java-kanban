public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        //инициализация объекта
        TaskManager taskManager = new TaskManager();

        //Task
        Task task1 = new Task("Task_1", "Go away");
        Task task2 = new Task("Task_2", "Cook");
        System.out.println("Create task1");
        Task task3 = taskManager.createTask(task1);// create
        System.out.println(task3);
        System.out.println("Create task2");
        Task task4 = taskManager.createTask(task2);
        System.out.println(task4);

        taskManager.showByIdTask(1);
        taskManager.showByIdTask(2);
        //taskManager.deleteAllTasks();

        System.out.println("Change task1");
        taskManager.updateTask(task3);
        System.out.println("Change task2");
        task4.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task4);

        taskManager.deleteTaskById(2);
        System.out.println(taskManager.showTask());
        System.out.println(task4);


        //Epic
        Epic epic1 = new Epic("Travel", "Buy everything");
        epic1.setName("Epic_1");
        Epic epic2 = new Epic("Epic_2", "Cut carrot");
        System.out.println("Create epic1");
        Epic epic3 = taskManager.createEpic(epic1);
        System.out.println("Create task2");
        Epic epic4 = taskManager.createEpic(epic2);

        System.out.println(taskManager.showByIdEpic(3));
        //taskManager.deleteAllEpics();

        System.out.println("Change epic1");
        taskManager.updateEpic(epic3);
        System.out.println("Change epic2");
        taskManager.updateEpic(epic4);

        //taskManager.deleteEpicById(2);
        System.out.println(taskManager.showEpic());


        //Subtask
        Subtask subtask1 = new Subtask("Subtask_1", "Plane", epic3);
        Subtask subtask2 = new Subtask("Subtask_2", "Reserve", epic4);

        System.out.println("Создание подзадачи subtask1");
        System.out.println(taskManager.createSubtask(subtask1, 3));
        System.out.println("Создание подзадачи subtask2");
        System.out.println(taskManager.createSubtask(subtask2, 3));

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);


        taskManager.showByIdSubtask(subtask1.getId());
        //taskManager.deleteAllSubtasks();

        System.out.println("Change subtask1");
        taskManager.updateSubtask(subtask1);
        System.out.println("Change subtask2");
        taskManager.updateSubtask(subtask2);

        taskManager.showEpic();

        //taskManager.deleteSubtaskById(3);

        //taskManager.showSubtask();
        //taskManager.deleteAllSubtasks();
        //taskManager.showEpic();
        System.out.println("Show subtasks from Epic");
        taskManager.getSubtaskFromEpic(epic3.getId());
        System.out.println();

        //taskManager.deleteSubtaskById(6);
    }
}