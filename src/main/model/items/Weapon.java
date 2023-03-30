package model.items;

import java.util.Objects;

// Weapon class contains information about items that will be used in the fight
public class Weapon implements Item {

    private String name;
    private String classs;
    private String verb;
    private String infVerb;
    private Integer attack;

    // EFFECTS: creates a Weapon with given name and class
    public Weapon(String name, String classs) {
        this.name = name;
        this.classs = classs;
        this.verb = "punched";
        this.infVerb = "punch";
        this.attack = 12;
    }

    // MODIFIES: this
    // EFFECTS: sets the word to use when equipped by player
    public void use() {
        this.verb = "sliced";
        this.infVerb = "kill";
    }

    @Override
    public String getName() {
        return this.name;
    }


    public String getClasss() {
        return classs;
    }

    public String getVerb() {
        return verb;
    }

    public String getInfVerb() {
        return infVerb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Weapon weapon = (Weapon) o;
        return Objects.equals(name, weapon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
