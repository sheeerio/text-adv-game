package model.items;

public class Weapon implements Item {

    private String name;
    private String classs;
    private String verb;
    private String infVerb;
    private Integer attack;

    public Weapon(String name, String classs) {
        this.name = name;
        this.classs = "hand";
        this.verb = "punched";
        this.infVerb = "punch";
        this.attack = 12;
    }

    @Override
    public void requiredItems() {

    }

    @Override
    public void use() {

        if (this.classs == "sword") {
            this.verb = "sliced";
            this.infVerb = "kill";
        } else if (this.classs == "axe") {
            this.verb = "chopped";
            this.infVerb = "chop";
        }

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void getRecipe() {

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
            return 5;
        } else {
            return 2;
        }
    }
}
