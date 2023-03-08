package model.items;

// Misc class contains information about miscellaneous items such as sticks
public class Misc implements Item {

    private String name;

    // EFFECTS: creates a miscellaneous item with given name
    public Misc(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
