package model.items;

import java.util.ArrayList;
import java.util.Objects;

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
    public ArrayList<Item> getRequiredItems() {
        return requiredItems;
    }

    @Override
    // EFFECTS: changes the equals settings for the block class
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Block block = (Block) o;
        return Objects.equals(name, block.name);
    }

    @Override
    // EFFECTS: changes the hashcode settings for the block class
    public int hashCode() {
        return Objects.hash(name);
    }
}
