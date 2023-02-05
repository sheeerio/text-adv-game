package model;

import model.Player;

import java.util.Random;

public class Mob {

    private String name;
    private Integer hp;
    private Double baseAttack;
    private Double speed;
    Random random = new Random();

    public Mob(String name, Integer hp, Double att, Double speed) {
        this.name = name;
        this.hp = hp;
        this.baseAttack = att;
        this.speed = speed;
    }

    public void getHitbyPlayer() {
        this.hp -= Player.getAttack();
    }

    public void hitPlayer() {
        this.baseAttack *= random.nextDouble();
    }

    public String getName() {
        return name;
    }

}
