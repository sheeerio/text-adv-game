package model.items;

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
        if (this.classs == "sword") {
            this.verb = "sliced";
            this.infVerb = "kill";
        } else if (this.classs == "axe") {
            this.verb = "chopped";
            this.infVerb = "chop";
        } else {
            this.verb = "punched";
            this.infVerb = "punch";
        }

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

    public Integer getAttack() {
        if (this.classs.equals("sword")) {
            return 20;
        } else {
            return 2;
        }
    }
}
