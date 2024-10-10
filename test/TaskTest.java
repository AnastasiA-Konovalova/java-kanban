import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {

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
}