package persistence;

import model.Inventory;
import model.items.Item;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Inventory inv = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyInventory() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyInventory.json");

        try {
            Inventory inv = reader.read();
            assertTrue(inv.getItems().isEmpty());
            assertTrue(inv.getNumberOfItems().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralInventory() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralInventory.json");
        try {
            Inventory inv = reader.read();
            assertFalse(inv.getItems().isEmpty());
            List<Item> items = inv.getItems();
            List<Integer> itemNum = inv.getNumberOfItems();
            assertEquals(2, items.size());
            assertEquals(2, itemNum.size());
            checkItem("Wood", items.get(0));
            checkItem("Wooden Planks", items.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
