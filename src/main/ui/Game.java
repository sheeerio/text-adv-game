package ui;

import model.Mob;
import model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class Game {

    // Initialize neutral mobs
    static Mob spider = new Mob("Spider", 16, 2.0, .4);
    static Mob wolf = new Mob("Wolf", 8, 4.0, 0.3);
    static Mob enderman = new Mob("Enderman", 40, 7.0, 0.0);
    static Mob ironGolem = new Mob("Iron Golem", 100, 21.5, 0.25);

    // Initialize hostile mobs
    static Mob blaze = new Mob("Blaze", 20, 6.0, 0.1);
    static Mob chickenJockey = new Mob("Chicken Jockey", 24, 3.0, .8);
    static Mob creeper = new Mob("Creeper", 20, 43.0, .4);
    static Mob skeleton = new Mob("Skeleton", 20, 4.0, 0.3);
    static Mob zombie = new Mob("Zombie", 20, 3.0, 0.23);

    public static void main(String[] args) {
        ArrayList<Mob> neutralMobList = new ArrayList<>(Arrays.asList(spider, wolf, enderman, ironGolem));
        ArrayList<Mob> hostileMobList = new ArrayList<>(Arrays.asList(blaze, chickenJockey, creeper, skeleton, zombie));

        Scanner in  = new Scanner(System.in);
        Random rand = new Random();

        // Game variables
        String[] neutralMobs = {"Spider", "Wolf", "Enderman", "Iron Golem"};
        String[] hostileMobs = {"Blaze", "Chicken Jockey", "Creeper", "Skeleton", "Zombie"};

        System.out.println("Enter your username: ");
        String username = in.nextLine();

        Player player = new Player(username, 1, 20, false);
        System.out.println("Welcome to the world, " + username);

        boolean running = true;

        GAME:
        while (running) {
            System.out.println("-----------------------------------------------------------------");
        }
    }
}
