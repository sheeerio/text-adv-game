package model.items;

import java.util.ArrayList;

public class Block implements Item {

    private static String name;
    ArrayList<Item> requiredItems = new ArrayList<>();


    public Block(String name, ArrayList<Item> reqs) {
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

    @Override
    public void getRecipe() {

    }

}
