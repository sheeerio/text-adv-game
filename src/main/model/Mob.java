package model;

// Mob class contains information about mob stats that would be during the Game fight.
public class Mob {

    private String name;
    private Integer hp;
    private Integer baseAttack;
    private Double speed;

    // EFFECTS: creates a mob and initializes its name, health,
    // base attack, and speed
    public Mob(String name, Integer hp, Integer att, Double speed) {
        this.name = name;
        this.hp = hp;
        this.baseAttack = att;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public Integer getHp() {
        return hp;
    }

    public Integer getBaseAttack() {
        return baseAttack;
    }

    public Double getSpeed() {
        return speed;
    }

}
