package model.items;

import java.util.ArrayList;

public class Blocks implements Item {

    private static String name;
    ArrayList<Item> requiredItems = new ArrayList<>();

    public Blocks(String name, ArrayList<Item> reqs) {
        this.name = name;
        requiredItems = reqs;
    }

    @Override
    public void requiredItems() {

    }

    @Override
    public void use() {

    }

    @Override
    public String getName() {
        return this.name;
    }
}
