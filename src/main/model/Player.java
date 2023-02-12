package model;

import model.items.Item;
import model.items.Weapon;

public class Player {

    private static Integer attack;
    private static Integer health;
    private static Item currentItem = null;
    private static boolean isRunning;
    private static String username;
    private static Weapon currentWeapon;
    Weapon hands = new Weapon("hands", "hands");
    Item woodenSword = new Weapon("Wooden Sword", "sword");

    public Player(String username, int attack, int health, boolean state) {
        this.username = username;
        this.attack = 10;
        this.health = health;
        this.isRunning = state;
        this.currentWeapon = hands;
    }

    public static Integer getAttack() {
        return currentWeapon.getAttack();
    }

    public static Integer getHealth() {
        return health;
    }

    public static Item getCurrentItem() {
        return currentItem;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static String getUsername() {
        return username;
    }

    public static void setHealth(Integer health) {
        Player.health = health;
    }
}