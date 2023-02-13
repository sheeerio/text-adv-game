package model.items;

import java.util.ArrayList;

public class Block implements Item {

    private String name;
    ArrayList<Item> requiredItems = new ArrayList<>();

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

    public ArrayList<Item> getRequiredItems() {
        return requiredItems;
    }
}
