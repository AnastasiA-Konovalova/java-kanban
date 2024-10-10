import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class SubtaskTest {
    @Test
    void testUniqueIdForNewSubtask() {
        Epic epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        Subtask subtask_1 = new Subtask("NameSubtask_1", "DescriptionSubtask_1", epic_1);
        subtask_1.setId(1);

        Epic epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");
        Subtask subtask_2 = new Subtask("NameSubtask_2", "DescriptionSubtask_2", epic_2);
        subtask_2.setId(1);

        Epic epic_3 = new Epic("NameEpic_3", "DescriptionEpic_3");
        Subtask subtask_3 = new Subtask("NameSubtask_3", "DescriptionSubtask_3", epic_3);
        subtask_3.setId(2);

        assertEquals(subtask_1, subtask_2, "Должны быть равны");
        assertNotEquals(subtask_2, subtask_3, "Равны быть не должны");
    }
}