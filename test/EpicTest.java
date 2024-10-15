import org.junit.jupiter.api.Test;
import tasks.Epic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EpicTest {

    @Test
    void testUniqueIdForNewEpic() {
        Epic epic_1 = new Epic("NameEpic_1", "DescriptionEpic_1");
        Epic epic_2 = new Epic("NameEpic_2", "DescriptionEpic_2");
        Epic epic_3 = new Epic("NameEpic_3", "DescriptionEpic_3");
        epic_1.setId(1);
        epic_2.setId(1);
        epic_3.setId(2);

        assertEquals(epic_1, epic_2, "Должны быть равны");
        assertNotEquals(epic_2, epic_3, "Равны быть не должны");
    }
}