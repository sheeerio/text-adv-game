package persistence;

import model.Inventory;
import model.Player;
import model.items.Block;
import model.items.Food;
import model.items.Item;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Inventory inv = new Inventory();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyInventory() {
        try {
            Inventory inv = new Inventory();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyInventory.json");
            writer.open();
            Player p = new Player("muchagraciasofficial", 19);
            writer.writeInventory(inv, p);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyInventory.json");
            inv = reader.read();
            assertTrue(inv.getItems().isEmpty());
            assertEquals(0, inv.getNumberOfItems().size());
        } catch (IOException e) {
            fail("Failed to open the file");
        }
    }

    @Test
    void testWriterGeneralInventory() {
        try {
            Item wood = new Block("Wood", null);
            Item woodenPlank = new Block("Wooden Planks", null);
            Item rawMeat  = new Food("Raw Meat", 5);
            Inventory inv = new Inventory();
            Player p = new Player("muchagraciasofficial", 20);
            inv.addItem(wood);
            inv.addItem(wood);
            inv.addItem(woodenPlank);
            inv.addItem(rawMeat);
            inv.addItem(rawMeat);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralInventory.json");
            writer.open();
            writer.writeInventory(inv, p);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralInventory.json");
            inv = reader.read();
            assertEquals(3, inv.getItems().size());
            assertEquals(3, inv.getNumberOfItems().size());
            List<Item> itemList = inv.getItems();
            List<Integer> itemNumList = inv.getNumberOfItems();
            String currentItem = inv.getCurrentItem();
            assertEquals(3, itemList.size());
            assertEquals("Wood", itemList.get(0).getName());
            assertEquals("Wooden Planks", itemList.get(1).getName());
            assertEquals("Raw Meat", itemList.get(2).getName());
            assertEquals(3, itemNumList.size());
            assertEquals(2, itemNumList.get(0));
            assertEquals(1, itemNumList.get(1));
            assertEquals(2, itemNumList.get(2));
            assertEquals("Raw Meat", currentItem);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
