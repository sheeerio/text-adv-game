package model.items;

public class Food implements Item {


    private String name;
    private int hpHealed;

    // EFFECTS: create a food item with given name and
    // sets the amount of health it heals when eaten by player
    public Food(String name, int hp) {
        this.name = name;
        this.hpHealed = hp;
    }


    @Override
    public String getName() {
        return this.name;
    }


    public int getHpHealed() {
        return hpHealed;
    }
}
