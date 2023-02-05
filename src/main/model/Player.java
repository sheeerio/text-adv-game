package model;

import model.items.Item;

public class Player {

    private static Integer attack;
    private static Integer health;
    private static Item currentItem = null;
    private static boolean isRunning;
    private static String username;


    public Player(String username, int attack, int health, boolean state) {
        this.username = username;
        this.attack = attack;
        this.health = health;
        this.isRunning = state;
    }

    public static Integer getAttack() {
        return attack;
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

}