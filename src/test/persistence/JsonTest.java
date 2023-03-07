package persistence;

import model.items.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkItem(String name, Item item) {
        assertEquals(name, item.getName());
    }
}
