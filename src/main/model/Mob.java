package model;

import model.Player;

import java.util.Random;

public class Mob {

    private String name;
    private Integer hp;
    private Integer baseAttack;
    private Double speed;
    Random random = new Random();

    public Mob(String name, Integer hp, Integer att, Double speed) {
        this.name = name;
        this.hp = hp;
        this.baseAttack = att;
        this.baseAttack += random.nextInt(this.baseAttack) + baseAttack;
        this.speed = speed;
    }

    public void getHitbyPlayer() {
        this.hp -= Player.getAttack();
    }

    public void hitPlayer() {
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
