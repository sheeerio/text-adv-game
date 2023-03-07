package model.items;

import org.json.JSONObject;

import java.util.ArrayList;

// Block class contains information about the blocks used in early game for creating other items
public class Block implements Item {

    private String name;
    ArrayList<Item> requiredItems;

    // EFFECTS: creates a block with given name and list
    // of required items
    public Block(String name, ArrayList<Item> reqs) {
        this.name = name;
        requiredItems = reqs;
    }

    @Override
    public String getName() {
        return this.name;
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        return json;
    }

    public ArrayList<Item> getRequiredItems() {
        return requiredItems;
    }
}
