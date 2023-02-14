package model;

public class Player {

    private static Integer attack;
    private static Integer health;
    private static String username;

    // EFFECTS: creates a player with given username
    // and initializes attack and health
    public Player(String username, int att, int health) {
        this.username = username;
        this.attack = att;
        this.health = health;
    }

    public static Integer getHealth() {
        return health;
    }

    public static String getUsername() {
        return username;
    }

    public static void setHealth(Integer health) {
        Player.health = health;
    }

    public static Integer getAttack() {
        return attack;
    }
}