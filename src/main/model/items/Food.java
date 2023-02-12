package model.items;

public class Food implements Item {


    private String name;
    private int hpHealed;

    public Food(String name, int hp) {
        this.name = name;
        this.hpHealed = hp;
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

    public int getHpHealed() {
        return hpHealed;
    }
}
