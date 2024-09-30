import org.junit.jupiter.api.Test;
import tasks.Epic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EpicTest {

    @Test
    void testUniqueIdForNewEpic() {
        Epic epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        epic1.setId(1);

        Epic epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        epic2.setId(1);

        Epic epic3 = new Epic("NameEpic3", "DescriptionEpic3");
        epic3.setId(2);

        assertEquals(epic1, epic2, "Должны быть равны");
        assertNotEquals(epic2, epic3, "Равны быть не должны");
    }
}