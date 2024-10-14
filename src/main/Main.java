package main;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import save.FileBackedTaskManager;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Поехали!");
        //инициализация объекта
        Path path = Path.of("saveTasksInfo.csv");
        InMemoryTaskManager inMemoryTaskManager = new FileBackedTaskManager(path);


       //create instants for tasks
        //1
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 4, 7, 1);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        //2
        ZoneId zoneId2 = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, Month.OCTOBER, 5, 8, 3);
        ZonedDateTime zonedDateTime2 = localDateTime2.atZone(zoneId2);
        Instant instant2 = zonedDateTime2.toInstant();
        //3
        ZoneId zoneId3 = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, Month.SEPTEMBER, 6, 9, 50);
        ZonedDateTime zonedDateTime3 = localDateTime3.atZone(zoneId3);
        Instant instant3 = zonedDateTime3.toInstant();

        //4
        ZoneId zoneId4 = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime4 = LocalDateTime.of(2024, Month.MARCH, 14, 5, 30);
        ZonedDateTime zonedDateTime4 = localDateTime4.atZone(zoneId4);
        Instant instant4 = zonedDateTime4.toInstant();

        //5
        ZoneId zoneId5 = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime5 = LocalDateTime.of(2024, Month.MAY, 10, 19, 00);
        ZonedDateTime zonedDateTime5 = localDateTime5.atZone(zoneId5);
        Instant instant5 = zonedDateTime5.toInstant();

        //tasks.Task
        Task task1 = new Task("Task_1", "Go away", instant4,Duration.ofSeconds(7000));
        Task task2 = new Task("Task_2", "Cook", instant5, Duration.ofSeconds(5000));
        inMemoryTaskManager.createTask(task1);// create
        inMemoryTaskManager.createTask(task2);

        //updateTask
        //inMemoryTaskManager.updateTask(task1);
        //inMemoryTaskManager.updateTask(task2);


        //tasks.Epic
        Epic epic1 = new Epic("Epic_1", "Buy everything");
        Epic epic2 = new Epic("Epic_2", "Cut carrot");
        Epic epic3 = inMemoryTaskManager.createEpic(epic1);
        Epic epic4 = inMemoryTaskManager.createEpic(epic2);

        //updateEpic
//        inMemoryTaskManager.updateEpic(epic3);
//        inMemoryTaskManager.updateEpic(epic4);

        //tasks.Subtask
        Subtask subtask1 = new Subtask("Subtask_1", "Plane", epic3);
        Subtask subtask2 = new Subtask("Subtask_2", "Reserve", epic4);

        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);

        //updateSubtask
        //inMemoryTaskManager.updateSubtask(subtask1);
        //inMemoryTaskManager.updateSubtask(subtask2);


        //................Проверка истории..............
        //открываем задачи
        inMemoryTaskManager.getByIdTask(1);
        inMemoryTaskManager.getByIdTask(2);
        inMemoryTaskManager.getByIdEpic(3);
        inMemoryTaskManager.getByIdEpic(4);
        inMemoryTaskManager.getByIdSubtask(5);
        inMemoryTaskManager.getByIdSubtask(6);
        inMemoryTaskManager.getByIdTask(1);
        inMemoryTaskManager.getByIdTask(2);
        inMemoryTaskManager.getByIdEpic(3);
        inMemoryTaskManager.getByIdEpic(4);
        inMemoryTaskManager.getByIdSubtask(5);
        inMemoryTaskManager.getByIdSubtask(6);

        inMemoryTaskManager.getHistory();

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
//        inMemoryTaskManager.deleteAllTasks();
        //inMemoryTaskManager.deleteTaskById(task1.getId());
        //System.out.println("History2" + inMemoryTaskManager.getHistory());
//        List<Task> history = inMemoryTaskManager.getHistory();
//        System.out.println(history.size());
////просмотренные задачи (удаление всех)
//        //inMemoryTaskManager.deleteAllTasks();
//        System.out.println(inMemoryTaskManager.getTaskList());
//        inMemoryTaskManager.getHistory();

//        inMemoryTaskManager.deleteAllEpics();
//        System.out.println(inMemoryTaskManager.getEpicList());
//        inMemoryTaskManager.getHistory();

        //inMemoryTaskManager.deleteAllSubtasks();
//        System.out.println(inMemoryTaskManager.getSubtaskList());
//        inMemoryTaskManager.getHistory();
//
//        inMemoryTaskManager.deleteEpicById(4);
//        System.out.println(inMemoryTaskManager.getHistory());

        //historyManager.removeNode();
//        InMemoryTaskManager inMemoryTaskManager1 = new InMemoryTaskManager();
//        Epic epic5 = new Epic("NameEpic1", "DescriptionEpic1");
//        epic5.setId(4);
//        Subtask subtask5 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic5);
//        inMemoryTaskManager1.createSubtask(subtask5, 4);

        //task1.setId(1);
//        task2.setId(task1.getId());
//        historyManager.add(task1);
//        historyManager.add(task2);
//        System.out.println(historyManager.getHistory());


        //saveFunction
        // Path path = Path.of("saveTasksInfo.csv");
        //FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path);


        System.out.println("______DateTime___________");
         //LocalDateTime, Instant, Duration
        System.out.println("getEndTime " + task1.getEndTime());
        System.out.println(task1);


        Epic epicForCheck1 = new Epic("NameOfEpicForCheck1", "Deck");
        inMemoryTaskManager.createEpic(epicForCheck1);
        Subtask sub1 = new Subtask("Sub1", "Dedc", epicForCheck1, instant, Duration.ofHours(3));
        Subtask sub2 = new Subtask("Sub2", "Dedc", epicForCheck1, instant2, Duration.ofMinutes(50));
        Subtask sub3 = new Subtask("Sub3", "Dedc", epicForCheck1, instant3, Duration.ofMinutes(30));
        inMemoryTaskManager.createSubtask(sub1);
        inMemoryTaskManager.createSubtask(sub2);
        inMemoryTaskManager.createSubtask(sub3);
        //start time for epic

        inMemoryTaskManager.getStartTimeForEpic(epicForCheck1);
        //System.out.println(epicForCheck1.getSubtasks());
        //sub2.getStartTime();
        //duration for epic
        inMemoryTaskManager.getDurationForEpic(epicForCheck1);

        //endTime
        inMemoryTaskManager.getEndTimeForEpic(epicForCheck1);

        //prioritizedTask
        System.out.println("Tasks " + inMemoryTaskManager.getTaskList());
        System.out.println("Subtasks " + inMemoryTaskManager.getSubtaskList());
        //System.out.println(inMemoryTaskManager.getPrioritizedTasks());

        //((FileBackedTaskManager) inMemoryTaskManager).save();
        //FileBackedTaskManager.loadFromFile(path.toFile());

        //loading
        ((FileBackedTaskManager) inMemoryTaskManager).save();
        FileBackedTaskManager.loadFromFile(path.toFile());

//
        //System.out.println("!!!!!!!!!!!" + inMemoryTaskManager.getHistory() + "!!!!!!!" + inMemoryTaskManager.getHistory().size());
        //Update


    }
}
