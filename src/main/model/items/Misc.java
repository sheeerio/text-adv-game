package model.items;

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
