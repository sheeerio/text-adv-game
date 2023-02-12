package model.items;

public class Misc implements Item {

    private String name;

    public Misc(String name) {
        this.name = name;
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
