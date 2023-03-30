package model.items;

import java.util.Objects;

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

    // EFFECTS: returns this as JSON object


    public int getHpHealed() {
        return hpHealed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Food food = (Food) o;
        return Objects.equals(name, food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
