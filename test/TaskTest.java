import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {

    private Task task_1;
    private Task task_2;
    private Task task_3;
    private Instant instant_1;
    private Instant instant_2;
    private Instant instant_3;
    private ZoneId zoneId;

    @BeforeEach
    void setUp() {
        zoneId = ZoneId.of("Europe/Moscow");

        LocalDateTime localDateTime_1 = LocalDateTime.of(2024, Month.DECEMBER, 15, 15, 10);
        ZonedDateTime zonedDateTime_1 = localDateTime_1.atZone(zoneId);
        instant_1 = zonedDateTime_1.toInstant();

        LocalDateTime localDateTime_2 = LocalDateTime.of(2024, Month.APRIL, 10, 10, 0);
        ZonedDateTime zonedDateTime_2 = localDateTime_2.atZone(zoneId);
        instant_2 = zonedDateTime_2.toInstant();

        LocalDateTime localDateTime_3 = LocalDateTime.of(2024, Month.JULY, 14, 9, 50);
        ZonedDateTime zonedDateTime_3 = localDateTime_3.atZone(zoneId);
        instant_3 = zonedDateTime_3.toInstant();

        task_1 = new Task("NameTask_1", "DescriptionTask_1", instant_1, Duration.ofSeconds(8000));
        task_2 = new Task("NameTask_2", "DescriptionTask_2", instant_2, Duration.ofSeconds(9000));
        task_3 = new Task("NameTask_3", "DescriptionTask_3", instant_3, Duration.ofSeconds(10000));
    }

    @Test
    void testUniqueIdForNewTask() {
        Task task1 = new Task("NameTask_1", "DescriptionTask_1");
        task1.setId(1);

        Task task2 = new Task("NameTask_2", "DescriptionTask_2");
        task2.setId(1);

        Task task3 = new Task("NameTask_3", "DescriptionTask_3");
        task3.setId(2);

        assertEquals(task1, task1, "Должны быть равны");
        assertNotEquals(task2, task3, "Равны быть не должны");
    }

    @Test
    public void testGetEndTime() {
        task_1.getEndTime();
        task_2.getEndTime();
        task_3.getEndTime();

        assertEquals("2024-12-15T14:23:20Z", task_1.getEndTime().toString());
        assertEquals("2024-04-10T09:30:00Z", task_2.getEndTime().toString());
        assertEquals("2024-07-14T09:36:40Z", task_3.getEndTime().toString());
    }
}