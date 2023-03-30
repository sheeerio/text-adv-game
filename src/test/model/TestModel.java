package model;

import model.items.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestModel {

    Block wood;
    Block woodenPlank;
    Food rawMeat;
    Food corn;
    Weapon woodenSword;
    Weapon stoneSword;
    Misc sticks;
    Inventory inv;
    Mob spider;
    Player p1;

    @BeforeEach
    public void setUp(){
        wood = new Block("Wood", null);
        woodenPlank = new Block("Wooden Planks", null);
        rawMeat  = new Food("Raw Meat", 5);
        woodenSword = new Weapon("Wooden Sword", "sword");
        stoneSword = new Weapon("Stone Sword", "sword");
        sticks = new Misc("Sticks");
        corn = new Food("Corn", 2);
        inv = new Inventory();
        spider = new Mob("Spider", 16, 2, .4);
        p1 = new Player("p1", 1);
    }

    @Test
    public void blockConstructorTest() {
        assertEquals("Wood", wood.getName());
        assertEquals(null, wood.getRequiredItems());
        assertEquals("Wooden Planks", woodenPlank.getName());
        assertEquals(null, woodenPlank.getRequiredItems());
    }

    @Test
    public void foodConstructorTest() {
        assertEquals("Raw Meat", rawMeat.getName());
        assertEquals(5, rawMeat.getHpHealed());
    }

    @Test
    public void weaponConstructorTest() {
        assertEquals("Wooden Sword", woodenSword.getName());
        assertEquals("sword", woodenSword.getClasss());
    }

    @Test
    public void weaponUseTest() {
        woodenSword.use();
        assertEquals("sliced", woodenSword.getVerb());
        assertEquals("kill", woodenSword.getInfVerb());
    }

    @Test
    public void miscConstructorTest() {
        assertEquals("Sticks", sticks.getName());
    }

    @Test
    public void inventoryConstructorTest() {
        assertEquals(null, inv.getCurrentItem());
        assertEquals(new ArrayList<>(), inv.getItems());
        assertEquals(new ArrayList<>(), inv.getItemNames());
        assertEquals(new ArrayList<>(), inv.getNumberOfItems());
    }

    @Test
    public void inventoryAddItemTest() {
        inv.addItem(wood);
        assertEquals(1, inv.getItems().size());
        assertEquals(1, inv.getItemNames().size());
        assertEquals(1, inv.getNumberOfItems().size());
        inv.addItem(wood);
        assertEquals(1, inv.getItems().size());
        assertEquals(1, inv.getItemNames().size());
        assertEquals(1, inv.getNumberOfItems().size());
        assertEquals(2, inv.count(wood));
    }

    @Test
    public void removeItemBunchTest() {
        inv.addItem(wood);
        inv.addItem(wood);
        inv.removeItemBunch(wood);
        assertEquals(null, inv.getCurrentItem());
        assertEquals(new ArrayList<>(), inv.getItems());
        assertEquals(new ArrayList<>(), inv.getItemNames());
        assertEquals(new ArrayList<>(), inv.getNumberOfItems());
    }

    @Test
    public void removeNItemsTest() {
        inv.addItem(wood);
        inv.addItem(wood);
        inv.addItem(woodenSword);
        for (int i = 0; i <  10; i++) {
            inv.addItem(sticks);
        }
        assertEquals(inv.count(sticks), 10);
        assertEquals(inv.getItems().size(), 3);
        inv.removeNItems(sticks, 4);
        assertEquals(inv.count(sticks), 6);
        assertEquals(inv.getItems().size(), 3);
        inv.removeNItems(sticks, 6);
        assertEquals(inv.count(sticks), 0);
        assertEquals(inv.getItems().size(), 2);
    }

    @Test
    public void countTest() {
        inv.addItem(wood);
        inv.addItem(wood);
        inv.addItem(woodenSword);
        for (int i = 0; i <  10; i++) {
            inv.addItem(sticks);
        }
        assertEquals(inv.count(sticks), 10);
        assertEquals(inv.count(rawMeat), 0);
        assertEquals(inv.count(wood), 2);
    }

    @Test
    public void mobConstructorTest() {
        assertEquals("Spider", spider.getName());
        assertEquals(16, spider.getHp());
        assertEquals(2, spider.getBaseAttack());
        assertEquals(.4, spider.getSpeed());
    }

    @Test
    public void playerConstructorTest() {
        assertEquals("p1", p1.getUsername());
        assertEquals(1, p1.getHealth());
    }

    @Test
    public void setHealthTest() {
        p1.setHealth(112);
        assertEquals(112, p1.getHealth());
    }

    @Test
    public void setCurrentItemToTest() {
        inv.addItem(wood);
        inv.addItem(sticks);
        inv.addItem(rawMeat);
        inv.setCurrentItemTo(2);
        assertEquals(inv.getCurrentItem(), "Raw Meat");
        inv.setCurrentItemTo(1);
        assertEquals(inv.getCurrentItem(), "Sticks");
    }

    @Test
    public void setCurrentItemDefaultTest() {
        inv.addItem(wood);
        inv.addItem(rawMeat);
        inv.addItem(wood);
        inv.setCurrentItemDefault();
        assertEquals(inv.getCurrentItem(), "Raw Meat");
    }

    @Test
    public void equalsTest() {
        assertTrue(wood.equals(wood));
        assertFalse(wood.equals(spider));
        assertFalse(wood.equals(rawMeat));
        assertFalse(wood.equals(null));
        assertFalse(rawMeat.equals(wood));
        assertFalse(rawMeat.equals(corn));
        assertFalse(rawMeat.equals(spider));
        assertTrue(rawMeat.equals(rawMeat));
        assertTrue(rawMeat.equals(woodenSword));
        assertFalse(rawMeat.equals(null));
        assertFalse(woodenSword.equals(wood));
        assertFalse(woodenSword.equals(spider));
        assertTrue(woodenSword.equals(woodenSword));
        assertTrue(woodenSword.equals(rawMeat));
        assertFalse(woodenSword.equals(null));
        assertFalse(woodenSword.equals(stoneSword));
    }

    @Test
    public void hashCodeTest() {
        assertEquals(wood.hashCode(), wood.hashCode());
        assertNotEquals(wood.hashCode(), rawMeat.hashCode());
        assertEquals(woodenSword.hashCode(), woodenSword.hashCode());
        assertNotEquals(woodenSword.hashCode(), woodenPlank.hashCode());
    }
}