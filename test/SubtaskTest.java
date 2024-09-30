import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class SubtaskTest {
    @Test
    void testUniqueIdForNewSubtask() {
        Epic epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        Subtask subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", epic1);
        subtask1.setId(1);

        Epic epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        Subtask subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", epic2);
        subtask2.setId(1);

        Epic epic3 = new Epic("NameEpic3", "DescriptionEpic3");
        Subtask subtask3 = new Subtask("NameSubtask3", "DescriptionSubtask3", epic3);
        subtask3.setId(2);

        assertEquals(subtask1, subtask2, "Должны быть равны");
        assertNotEquals(subtask2, subtask3, "Равны быть не должны");
    }
}