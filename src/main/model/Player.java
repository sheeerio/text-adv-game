package model;

import org.json.JSONObject;
import persistence.Writable;

// Player class contains information about the Player statistics that will be used in the fight
public class Player implements Writable {

    private static Integer health;
    private static String username;

    // EFFECTS: creates a player with given username
    // and initializes attack and health
    public Player(String username, int health) {
        this.username = username;
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

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("stats", statsToJson());
        return json;
    }

    public JSONObject statsToJson() {
        JSONObject json = new JSONObject();
        json.put("hp", health);
        json.put("username", username);
        return json;
    }
}