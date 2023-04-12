package ui;

import model.Event;
import model.EventLog;
import model.Inventory;
import model.Mob;
import model.Player;
import model.items.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.*;


// Game represents information about the UI of game, including printing and scanning.
public class Game implements LogPrinter {

    JFrame window;
    Container con;
    JPanel titleNamePanel;
    JPanel startButtonPanel;
    JPanel mainTextPanel;
    JPanel choiceButtonPanel;
    JPanel playerPanel;
    JPanel startFieldPanel;
    JPanel inventoryTextPanel;
    JLabel titleNameLabel;
    JLabel hpLabel;
    JLabel weaponLabel;
    JLabel usernameTextLabel;
    Font titleFont = new Font("Times New Roman", Font.BOLD, 94);
    Font normalFont = new Font("Times New Roman", Font.PLAIN, 18);
    JButton startButton;
    JButton choice1;
    JButton choice2;
    JButton choice3;
    JButton choice4;
    JButton loadButton;
    JButton saveButton;
    SceneKonami mainTextArea;
    SceneKonami inventoryTextArea;
    JTextField usernameTextField;
    JScrollPane scroll;
    String position;

    TitleScreenHandler tsHandler = new TitleScreenHandler();
    ChoiceHandler choiceHandler = new ChoiceHandler();
    ActionListener chopListener;

    // Initialize JSON classes
    private static final String JSON_STORE = "./data/game.json";
    private static JsonReader jsonReader = new JsonReader(JSON_STORE);
    private static JsonWriter jsonWriter = new JsonWriter(JSON_STORE);

    // Initialize day
    private static int timeState = 1; // 1 : day; 2 : night
    int woodChopped = 0;
    boolean whichOne = false;


    // Initialize neutral mobs
    static Mob spider = new Mob("Spider", 16, 2, .4);
    static Mob enderman = new Mob("Enderman", 40, 7, 0.0);

    // Initialize hostile mobs
    static Mob blaze = new Mob("Blaze", 20, 6, 0.1);
    static Mob chickenJockey = new Mob("Chicken Jockey", 24, 3, .8);
    static Mob creeper = new Mob("Creeper", 20, 43, .4);
    static Mob skeleton = new Mob("Skeleton", 20, 4, 0.3);
    static Mob zombie = new Mob("Zombie", 20, 3, 0.23);
    String[] animalMobs = {"wolf", "cow", "pig", "sheep"};
    ArrayList<Mob> hostileMobList = new ArrayList<>(Arrays.asList(spider, enderman, blaze,
            chickenJockey, creeper, skeleton, zombie));
    Mob enemy = new Mob(null, null, null, null);

    // Initialize blocks
    static Block wood = new Block("Wood", null);
    static Food rawMeat = new Food("Raw Meat", 5);
    static Item woodenPlank = new Block("Wooden Planks", null);
    static Item sticks = new Misc("Sticks");
    static Item woodenSword = new Weapon("Wooden Sword", "sword");
    static Item gold = new Misc("Gold");

    // Initialize inventory
    static Inventory inventory = new Inventory();

    // Initialize player
    static Player player;
    static boolean win = false;

    // Intialize scanner
    static Scanner in = new Scanner(System.in);
    int enemyHealth = 0;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    // EFFECT: prints the current inventory
    public static void printInventory() {

        for (Item i : inventory.getItems()) {
            System.out.println(inventory.getItems().indexOf(i) + 1 + ".  "
                    + inventory.getItemNames().get(inventory.getItems().indexOf(i)) + " ("
                    + inventory.getNumberOfItems().get(inventory.getItems().indexOf(i)) + ")");
        }
//        System.out.println("Press the item number to change current item.");
//        System.out.println("Press E to close the inventory");
//        System.out.println("Press S to save the inventory");
//        System.out.println("Press L to read the inventory");
    }

    // EFFECT: prints the current item
    public static void printCurrentItem() {
        System.out.println("Your current item is " + inventory.getCurrentItem());
        getRecipeOfCurrentItem();
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    // EFFECT: prints the recipe of curent item
    public static void getRecipeOfCurrentItem() {
        if (inventory.getCurrentItem() == "Wood") {
            System.out.println("Press B to create Wooden Planks (4)");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 4; i++) {
                    inventory.addItem(woodenPlank);
                }
                inventory.removeNItems(wood, 1);
                inventory.setCurrentItemTo(inventory.getItems().indexOf(woodenPlank));
                printInventory();
            }
        } else if (inventory.getCurrentItem() == "Wooden Planks" && inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Wooden Planks")) >= 2) {
            System.out.println("Press B to create Sticks (2)");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 2; i++) {
                    inventory.addItem(sticks);
                }
                inventory.removeNItems(woodenPlank, 2);
                inventory.setCurrentItemTo(inventory.getItems().indexOf(sticks));
                printInventory();
            }
        } else if (Objects.equals(inventory.getCurrentItem(), "Sticks")
                && inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Wooden Planks")) >= 2
                && inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Sticks")) > 0) {
            System.out.println("Press B to create Wooden Sword");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 1; i++) {
                    inventory.addItem(woodenSword);
                    inventory.removeNItems(woodenPlank, 2);
                    inventory.removeNItems(sticks, 1);
                    inventory.setCurrentItemTo(inventory.getItems().indexOf(woodenSword));
                    printInventory();
                }
            }
        }
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    // EFFECT: main
    public static void main(String[] args) {
        new Game();
        String difficulty = "normal";


        Scanner in = new Scanner(System.in);
        Random rand = new Random();

        // Game variables

        System.out.println("Choose Route:\n\n1. Pacifist\n2. Survival");
        String route = in.nextLine();

        if (route.equals("1") || route.equals("Pacifist") || route.equals("pacifist")) {
            difficulty = "hard";
        }
        System.out.println("Enter your username: ");
        String username = in.nextLine();

        System.out.println("Welcome to the world, " + username + "\n\n\n\n\n");
        player = new Player(username, 20);

        System.out.println("Press 1 to load previous game data");
        String baba = in.nextLine();

        if (baba.equals("1")) {
            try {
                inventory = jsonReader.read();
                System.out.println("Loaded inventory from " + JSON_STORE);
                if (inventory.getItems().contains(gold)) {
                    win = true;
                }
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        }

        Weapon weapon = new Weapon("hands", "hands");
        boolean running = true;

        Timer timer = new Timer();
        long startTime = System.currentTimeMillis();
        WOW:
        while (running) {

            System.out.println("Press 1 to " + weapon.getInfVerb() + " tree.");
            String chop = in.nextLine();
            int sum = 0;
            boolean oneY = false;
            if (chop.equals("1")) {
                oneY = true;
            } else {
                System.out.println("You pressed the wrong button!");
                break WOW;
            }
            while (oneY && !chop.equals("E")) {
                Random lund = new Random();
                int num = lund.nextInt(4) + 1;
                for (int i = 0; i < num; i++) {
                    inventory.addItem(wood);
                }

                makeWait();
                System.out.println("You " + weapon.getVerb() + " " + num + " wooden logs!");
                System.out.println("You now have " + inventory.count(wood) + " wooden logs!\n");
                System.out.println("Press E to view your inventory.");
                System.out.println("Press Enter to quit chopping wood.");
                System.out.println("Press 1 to " + weapon.getInfVerb() + " tree");

                chop = in.nextLine();
                if (chop.equals("E")) {
                    printInventory();
                } else if (chop.isEmpty()) {
                    continue;
                } else {
                    sum += 1;
                }
                if (sum % 5 == 0) {
                    System.out.println("Are you done? (y/n)");
                    String chopEnd = in.nextLine();
                    if (chopEnd.equals("y")) {
                        break;
                    } else {
                        chop = "1";
                    }
                }
            }


            System.out.println("Do you want to kill it for meat? (y/n)");
            String killAnimal = in.nextLine();
            if (killAnimal.equals("y")) {
                makeWait();
                if (difficulty.equals("hard")) {
                    System.out.println("The animal died and you got nothing...");
                } else {
                    int drop = rand.nextInt(3) + 1;
                    for (int i = 0; i < drop; i++) {
                        inventory.addItem(rawMeat);
                    }
                    System.out.println("You got " + drop + " Raw Meat.");
                }
            }


            makeWait();
            long endTime = System.currentTimeMillis();
            setTimeState(endTime);
            if (timeState % 2 == 0) {
                if (inventory.getItemNames().contains("Wooden Sword") && inventory.getCurrentItem() != "Wooden Sword") {
                    String out = "You should equip your sword.";
                    System.out.println("\nDusk is approaching..." + out);
                    System.out.println("Press E to open your inventory.\n");
                    String inv = in.nextLine();

                    if (inv.equals("E") || inv.equals("e")) {
                        printInventory();
                    }
                } else if (!inventory.getItemNames().contains("Wooden Sword")) {
                    String out = "You should craft a wooden sword.";
                    System.out.println("\nDusk is approaching..." + out);
                    System.out.println("Press E to open your inventory.\n");
                    String inv = in.nextLine();

                    if (inv.equals("E") || inv.equals("e")) {
                        printInventory();
                    }
                } else {
                    System.out.println("Dusk is approaching... good luck.");
                }
            }
            if (difficulty.equals("hard")) {
                System.out.println("Quit the game!");
                System.exit(0);
            }

            makeWait();
            boolean enemyfight = true;
            GAME:
            while (enemyfight) {
                ArrayList<Mob> hostileMobList = new ArrayList<>(Arrays.asList(spider, enderman, blaze,
                        chickenJockey, creeper, skeleton, zombie));
                Mob enemy = hostileMobList.get(rand.nextInt(hostileMobList.size()));
                int enemyHealth = enemy.getHp();
                String enemyName = enemy.getName();
                System.out.println("\n--------------------------");
                System.out.println("\t# " + enemyName + " has appeared! #\n");

                while (enemyHealth > 0) {
                    System.out.println("\tYour HP: " + Player.getHealth());
                    System.out.println("\t" + enemyName + "'s HP: " + enemyHealth);
                    System.out.println("\n\t What would you like to do?");
                    System.out.println("\t1. Attack");
                    if (inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Raw Meat")) > 0) {
                        System.out.println("\t2. Eat raw meat ("
                                + inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Raw Meat")) + ")");
                    } else {
                        System.out.println("\t2. Eat raw meat (0)");
                    }
                    System.out.println("\t3. Run!");

                    String input = in.nextLine();
                    if (input.equals("1")) {
                        int damageDealt = rand.nextInt(20) + 10;
                        int damageTaken = enemy.getBaseAttack();

                        enemyHealth -= damageDealt;
                        Player.setHealth(Player.getHealth() - damageTaken);

                        System.out.println("\t> You strike the " + enemyName + " for " + damageDealt + " damage.");
                        System.out.println("\t> You receive " + damageTaken + " in retaliation");

                        if (Player.getHealth() < 1) {
                            System.out.println("\t> You have taken too much damage, you are too weak to go on!");
                        }
                    } else if (input.equals("2")) {
                        if (inventory.count(rawMeat) > 0) {
                            Player.setHealth(Player.getHealth() + 10);
                            inventory.removeNItems(rawMeat, 1);
                            if (inventory.getItemNames().contains("Raw Meat")) {
                                System.out.println("\t> You eat raw meat, healing yourself for 10 HP."
                                        + "\n\t> You now have " + Player.getHealth() + " HP."
                                        + "\n\t> You have "
                                        + inventory.count(rawMeat)
                                        + " raw meat.");
                            } else {
                                System.out.println("You don't have enough meat... get fucked!");
                                break;
                            }
                        } else {
                            System.out.println("\t> You have no health items left! Defeat enemies for a chance to get one!");
                        }
                    } else if (input.equals("3")) {
                        System.out.println("\t> You run away from the " + enemyName + "!");
                        continue GAME;
                    } else {
                        System.out.println("\tInvalid command!");
                    }
                }

                if (Player.getHealth() < 1) {
                    System.out.println(Player.getUsername() + " limps out of the cave, weak from battle.");
                    break;
                }

                System.out.println("-----------------------");
                System.out.println(" # " + enemyName + " was defeated # ");
                System.out.println(" # " + Player.getUsername() + " has " + Player.getHealth() + " HP left");
                int drop = rand.nextInt(4) + 1;
                if (rand.nextInt(10) < 7) {
                    for (int i = 0; i < drop; i++) {
                        inventory.addItem(rawMeat);
                    }
                    System.out.println(" # The " + enemyName + " dropped " + drop + " raw meat.");
                    System.out.println(" # You now have "
                            + inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Raw Meat"))
                            + " raw meat");
                }
                System.out.println("---------------");
                System.out.println("What would you like to do now?");
                System.out.println("1. Continue fighting");
                System.out.println("2. Exit cave");
                System.out.println("3. Save Game");
                System.out.println("4. Load Game (Current game data will be lost)");
                System.out.println("5. Go outside to chop wood");

                String input = in.nextLine();

                if (input.equals("3")) {
                    try {
                        jsonWriter.open();
                        jsonWriter.writeInventory(inventory, player);
                        jsonWriter.close();
                        System.out.println("Saved Game to " + JSON_STORE);
                    } catch (FileNotFoundException e) {
                        System.out.println("Unable to read from file: " + JSON_STORE);
                    }
                }
                if (input.equals("5")) {
                    continue WOW;
                }

                if (input.equals("4")) {
                    try {
                        inventory = jsonReader.read();
                        System.out.println("Loaded Game from " + JSON_STORE);
                    } catch (IOException e) {
                        System.out.println("Unable to read from file: " + JSON_STORE);
                    }
                }

                while (!input.equals("1") && !input.equals("2")) {

                    System.out.println("Invalid command");
                    input = in.nextLine();
                }

                if (input.equals("1")) {
                    System.out.println("You continue on...");
                } else if (input.equals("2")) {
                    System.out.println("You exit the cave...");
                    break;
                }
            }

            System.out.println("########################");
            System.out.println("#  THANKS FOR PLAYING! #");
            System.out.println("########################");
            System.exit(0);


        }


    }

    // EFFECT: returns the first-person subject agreement of verb for animal
    public static String animalSound(String animal) {
        if (animal == "wolf") {
            return "barks";
        } else if (animal == "pig") {
            return "oinks";
        } else if (animal == "cow") {
            return "moos";
        } else {
            return "baahs";
        }
    }

    // EFFECT: delays execution of next line after makeWait() by 2 seconds
    public static void makeWait() {
        System.out.print(".");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            //Don't worry about it.
        }
        System.out.print(".");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            //Don't worry about it.
        }
        System.out.print(".");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            //Don't worry about it.
        }
    }

    // MODIFIES: this
    // EFFECT: changes state of time
    public static void setTimeState(long time) {
        if (time > 100000) {
            timeState++;
        }
    }

    // EFFECTS: constructor for game class
    public Game() {
        extracted1();

        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);
        startButtonPanel.add(loadButton);
        startButton.setFont(normalFont);
        startButton.addActionListener(tsHandler);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player = new Player(usernameTextField.getText(), 100);
            }
        });
        con.add(titleNamePanel);
        con.add(startButtonPanel);
        con.add(startFieldPanel);
        window.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates a window and panel for the title screen
    private void extracted1() {
        loadButton = new JButton("Load Game");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    inventory = jsonReader.read();
                    usernameTextField.setText("Loaded game from " + JSON_STORE);
                    win = true;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        extracted2();

        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100, 100, 600, 150);
        titleNamePanel.setBackground(new Color(0,0,0,125));
        titleNameLabel = new JLabel("12 SECONDS");
        titleNameLabel.setText("12 SECONDS");
        titleNameLabel.setForeground(Color.white);
        titleNameLabel.setFont(titleFont);

        extracted();
    }

    // MODIFIES: this
    // EFFECTS: creates the window for the game class
    private void extracted2() {
        window = new JFrame();

        try {
            JLabel background = new JLabel(
                    new ImageIcon(ImageIO.read(new File("src/main/ui/background.jpg"))));
            window.setContentPane(background);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(new Color(0,0,0,125));
        window.setLayout(null);
        con = window.getContentPane();
    }

    // MODIFIES: this
    // EFFECTS: creates panel and labels for the game window class
    private void extracted() {
        startFieldPanel = new JPanel();
        startFieldPanel.setBounds(200, 240, 400, 40);
        startFieldPanel.setBackground(new Color(0, 0, 0,125));
        startFieldPanel.setForeground(Color.white);

        usernameTextLabel = new JLabel();
        usernameTextField = new JTextField("Enter Username", 40);
        startFieldPanel.setLayout(new GridLayout(1, 2));
        startFieldPanel.add(usernameTextField);

        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(300, 350, 200, 100);
        startButtonPanel.setBackground(new Color(0,0,0,125));

        startButton = new JButton("START");
        startButtonPanel.setBackground(new Color(0,0,0,125));
        startButtonPanel.setForeground(Color.white);
    }

    // MODIFIES: this
    // EFFECTS: constructs title game screen
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void createGameScreen() {
        extracted3();

        extracted4();

        extracted5();

        saveButton = new JButton("save");
        saveButton.setFont(normalFont);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jsonWriter.open();
                    jsonWriter.writeInventory(inventory, player);
                    jsonWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                mainTextArea.setText("saved game to " + JSON_STORE);
            }
        });
        playerPanel.add(saveButton);

        inventoryTextPanel = new JPanel();
        inventoryTextPanel.setBounds(500, 100, 100, 250);
        inventoryTextPanel.setBackground(new Color(0,0,0,125));
        con.add(inventoryTextPanel);

        inventoryTextArea = new SceneKonami("Inventory: \n");
        inventoryTextArea.setBounds(100, 100, 600, 250);
        inventoryTextArea.setBackground(new Color(0,0,0,125));
        inventoryTextArea.setForeground(Color.white);
        inventoryTextArea.setFont(normalFont);
        inventoryTextPanel.add(inventoryTextArea);

        villageGate();
    }

    // MODIFIES: this
    // EFFECTS: creates hpLabel and weaponLabel for the playerPanel
    private void extracted5() {
        hpLabel = new JLabel("HP: " + player.getHealth().toString());
        hpLabel.setFont(normalFont);
        hpLabel.setForeground(Color.white);
        playerPanel.add(hpLabel);

        weaponLabel = new JLabel("Current Item: " + inventory.getCurrentItem());
        weaponLabel.setFont(normalFont);
        weaponLabel.setForeground(Color.white);
        playerPanel.add(weaponLabel);
    }

    // MODIFIES: this
    // EFFECTS: adds the main buttons to the game window
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void extracted4() {
        choice1 = new JButton("chop wood");
        choice1.setBackground(new Color(0,0,0,125));
        choice1.setFont(normalFont);
        choice1.addActionListener(choiceHandler);
        choiceButtonPanel.add(choice1);
        choice1.setActionCommand("c1");

        choice2 = new JButton("Choice 2");
        choice2.setBackground(new Color(0,0,0,125));
        choice2.setFont(normalFont);
        choice2.addActionListener(choiceHandler);
        choiceButtonPanel.add(choice2);
        choice2.setActionCommand("c2");

        choice3 = new JButton("Choice 3");
        choice3.setBackground(new Color(0,0,0,125));
        choice3.setFont(normalFont);
        choice3.addActionListener(choiceHandler);
        choiceButtonPanel.add(choice3);
        choice3.setActionCommand("c3");

        choice4 = new JButton("Check Inventory");
        choice4.setBackground(new Color(0,0,0,125));
        choice4.setFont(normalFont);
        choiceButtonPanel.add(choice4);
        choice4.setActionCommand("c4");
        choice4.addActionListener(choiceHandler);

        playerPanel = new JPanel();
        playerPanel.setBounds(100, 15, 600, 50);
        playerPanel.setBackground(new Color(0,0,0,125));
        playerPanel.setLayout(new GridLayout(1, 3));
        con.add(playerPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds main text panel to the game window
    private void extracted3() {
        titleNamePanel.setVisible(false);
        startButtonPanel.setVisible(false);
        startFieldPanel.setVisible(false);

        mainTextPanel = new JPanel();
        mainTextPanel.setBounds(100, 100, 600, 250);
        mainTextPanel.setBackground(new Color(0,0,0,125));
        con.add(mainTextPanel);

        mainTextArea = new SceneKonami("Welcome to the world, " + player.getUsername());
        mainTextArea.setBounds(100, 100, 600, 250);

        mainTextArea.setFont(normalFont);
        scroll = new JScrollPane(mainTextArea);
        mainTextPanel.add(mainTextArea);
        mainTextPanel.add(scroll);

        choiceButtonPanel = new JPanel();
        choiceButtonPanel.setBounds(250, 350, 300, 150);
        choiceButtonPanel.setBackground(new Color(0,0,0,125));
        choiceButtonPanel.setLayout(new GridLayout(4, 1));
        con.add(choiceButtonPanel);
    }

    // TitleScreenHandler contains information about the title action listener
    public class TitleScreenHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            createGameScreen();
        }
    }

    // MODIFIES: this
    // EFFECTS: prints the number of wood/wooden planks chopped and adds wood/wooden planks to the inventory
    public void callWoodPrint() {

        Random lund = new Random();
        int which = lund.nextInt(2);
        int num = lund.nextInt(4) + 1;
        if (which == 0) {
            whichOne = true;
            for (int i = 0; i < num; i++) {
                inventory.addItem(wood);
            }
        } else {
            whichOne = false;
            for (int i = 0; i < num; i++) {
                inventory.addItem(woodenPlank);
            }
        }
        woodChopped = num;
        if (whichOne == true) {
            mainTextArea.setText("You chopped " + woodChopped + " wood!");
        } else {
            mainTextArea.setText("You chopped " + woodChopped + " wooden planks!");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new Inventory window and sets the current item to display
    public void openInventory() {
        position = "openInventory";
        new InventoryWindow(inventory, 300, 300);
        weaponLabel.setText("Current Item: " + inventory.getCurrentItem());
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation representing the villageGate
    public void villageGate() {
        position = "villageGate";
        String text = "You, " + player.getUsername() + ", are at the gate of the village.<BR> A guard is "
                + "standing in front of you.<BR><BR>What do you do?";
        text = "<HTML>" + text + "</HTML>";
        mainTextArea.setText(text);
        choice2.setVisible(true);
        choice3.setVisible(true);
        choice4.setVisible(true);
        choice1.setText("Talk to the guard");
        choice2.setText("Attack the guard");
        choice3.setText("Leave");
        choice4.setText("");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation representing a conversation with the guard
    public void talkGuard() {
        position = "talkGuard";
        mainTextArea.setText("<HTML>Guard: Hello stranger, I have never seen your face.<BR> I'm sorry but we "
                + "cannot let a stranger enter the village<HTML>");
        choice1.setText("Go back");
        choice2.setText("Why not?");
        choice3.setText("Okay.");
        choice4.setText("True true");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation representing a conversation with the guard
    public void okay() {
        position = "okay";
        mainTextArea.setText("<HTML>Guard: Hello stranger, I have never seen your face.<BR> I'm sorry but we "
                + "cannot let a stranger enter the village<BR><BR>" + player.getUsername() + ": Okay<HTML>");
        choice1.setText("Go back");
        choice2.setVisible(false);
        choice3.setVisible(false);
        choice4.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation representing a conversation with the guard
    public void trueTrue() {
        position = "trueTrue";
        mainTextArea.setText("<HTML>Guard: Hello stranger, I have never seen your face.<BR> I'm sorry but we "
                + "cannot let a stranger enter the village<BR><BR>" + player.getUsername() + ": True true<HTML>");
        choice1.setText("Go back");
        choice2.setVisible(false);
        choice3.setVisible(false);
        choice4.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation representing a conversation with the guard
    public void whyNot() {
        position = "whyNot";
        mainTextArea.setText("<HTML>Guard: Hello stranger, I have never seen your face.<BR> I'm sorry but we "
                + "cannot let a stranger enter the village<BR><BR>" + player.getUsername()
                + ": Why not?<BR><BR>Guard: Because it is the command of thine lord and not because <BR>we're short "
                + "of gold. No, not at all...<HTML>");
        choice1.setText("Okay, I guess");
        choice2.setVisible(false);
        choice3.setVisible(false);
        choice4.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation representing a conversation with the guard
    public void whyNotOkay() {
        position = "whyNotOkay";
        mainTextArea.setText("<HTML>Guard: Hello stranger, I have never seen your face.<BR> I'm sorry but we "
                + "cannot let a stranger enter the village<BR><BR>" + player.getUsername()
                + ": Why not?<BR><BR>Guard: Because it is the command of thine lord and not because <BR>we're short "
                + "of gold. No, not at all...<BR><BR>" + player.getUsername() + ": Okay, I guess<HTML>");
        choice1.setText("Go back");
        choice2.setVisible(false);
        choice3.setVisible(false);
        choice4.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation a fight with the guard
    public void attackGuard() {
        position = "attackGuard";
        mainTextArea.setText("<HTML>Guard: Hey don't be stupid<BR><BR>The guard fought back and hit you!</HTML>");
        if (player.getHealth() > 3) {
            player.setHealth(player.getHealth() - 3);
        } else {
            player.setHealth(0);
            died();
            player.setHealth(20);
            hpLabel.setText("HP: 20");
            return;
        }
        hpLabel.setText("HP: " + player.getHealth());
        choice1.setText("Go back");
        choice2.setVisible(false);
        choice3.setVisible(false);
        choice4.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text with the situation of being in a cross road.
    public void crossRoad() {
        position = "crossRoad";
        mainTextArea.setText("You are in the middle of nowhere. If you go south, you will go back to the village.");
        choice1.setText("Go north");
        choice2.setText("Go east");
        choice3.setText("Go south");
        choice4.setText("Go west");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player being dead.
    public void died() {
        position = "died";
        mainTextArea.setText("<HTML>You are dead!<BR><BR>Respawn?</HTML>");
        choice1.setText("Yes");
        choice2.setText("No");
        choice3.setText("");
        choice4.setText("");
    }

    // ChoiceHandler represents the information containing the choices for the 4 buttons created
    public class ChoiceHandler implements ActionListener {


        @Override
        @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
        public void actionPerformed(ActionEvent e) {

            String yourChoice = e.getActionCommand();

            switch (position) {
                case "whyNotOkay":
                case "trueTrue":
                case "okay":
                case "attackGuard":
                    switch (yourChoice) {
                        case "c1":
                            villageGate();
                            break;
                    }
                    break;
                case "whyNot":
                    switch (yourChoice) {
                        case "c1":
                            whyNotOkay();
                            break;
                    }
                    break;
                case "villageGate":
                    switch (yourChoice) {
                        case "c1":
                            if (win) {
                                ending();
                            } else {
                                talkGuard();
                            }
                            break;
                        case "c2":
                            attackGuard();
                            break;
                        case "c3":
                            crossRoad();
                            break;
                        case "c4":
                            break;
                    }
                    break;
                case "talkGuard":
                    switch (yourChoice) {
                        case "c1":
                            villageGate();
                            break;
                        case "c2":
                            whyNot();
                            break;
                        case "c3":
                            okay();
                            break;
                        case "c4":
                            trueTrue();
                            break;
                    }
                    break;
                case "crossRoad":
                    switch (yourChoice) {
                        case "c1":
                            getWood();
                            break;
                        case "c2":
                            east();
                            break;
                        case "c3":
                            villageGate();
                            break;
                        case "c4":
                            south();
                            break;
                    }
                    break;
                case "north":
                    switch (yourChoice) {
                        case "c1":
                            callWoodPrint();
                            break;
                        case "c2":
                            break;
                        case "c3":
                            crossRoad();
                            break;
                        case "c4":
                            openInventory();
                            break;
                    }
                    break;
                case "east":
                    switch (yourChoice) {
                        case "c1":
                            killAnimals();
                            break;
                        case "c2":
                            watchAnimals();
                            break;
                        case "c3":
                            openInventory();
                            break;
                        case "c4":
                            crossRoad();
                            break;
                    }
                    break;
                case "died":
                    switch (yourChoice) {
                        case "c1":
                            player.setHealth(20);
                            villageGate();
                            break;
                        case "c2":
                            System.exit(0);
                            break;
                    }
                    break;
                case "watchAnimals":
                    switch (yourChoice) {
                        case "c1":
                            killAnimals();
                            break;
                        case "c4":
                            crossRoad();
                            break;
                    }
                    break;
                case "killAnimals":
                    switch (yourChoice) {
                        case "c1":
                            killAnimals();
                            break;
                        case "c3":
                            openInventory();
                            break;
                        case "c4":
                            crossRoad();
                            break;
                    }
                    break;
                case "south":
                    switch (yourChoice) {
                        case "c1":
                            fight();
                            break;
                        case "c2":
                            crossRoad();
                            break;
                        case "c3":
                            eatMeat();
                            break;
                    }
                    break;
                case "fight":
                    switch (yourChoice) {
                        case "c1":
                            playerAttack();
                            break;
                        case "c2":
                            crossRoad();
                            break;
                        case "c3":
                            eatMeat();
                            break;
                    }
                    break;
                case "openInventory":
                    switch (yourChoice) {
                        case "c1":
                            callWoodPrint();
                            break;
                        case "c3":
                            crossRoad();
                            break;
                        case "c4":
                            openInventory();
                            break;
                    }
                    break;
                case "playerAttack":
                    switch (yourChoice) {
                        case "c1":
                            if (enemyHealth < 1) {
                                win();
                            } else {
                                monsterAttack();
                            }
                            break;
                    }
                    break;
                case "monsterAttack":
                    switch (yourChoice) {
                        case "c1":
                            if (player.getHealth() < 1) {
                                lose();
                            } else {
                                fight();
                            }
                    }
                    break;
                case "win":
                    switch (yourChoice) {
                        case "c1":
                            crossRoad();
                            break;
                    }
                    break;
                case "eatMeat":
                    switch (yourChoice) {
                        case "c1":
                            fight();
                            break;
                    }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player being in a forest.
    public void getWood() {
        position = "north";
        mainTextArea.setText("<HTML>You're in a dark oak wood forest.<BR><BR>What do you do?</HTML>");
        choice1.setText("Chop wood!");
        choice2.setText("");
        choice3.setText("Go back");
        choice4.setText("Open inventory");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player being in a grassland.
    public void east() {
        position = "east";
        mainTextArea.setText("<HTML>You go east and see a herd of sheep in the open grasslands.<BR>"
                + "You can kill them for some meat and wool.<BR> What do you do?</HTML");
        choice1.setText("Murder the animals");
        choice2.setText("Watch the animals graze");
        choice3.setText("Check your inventory");
        choice4.setText("Go west");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player watching animals.
    public void watchAnimals() {
        position = "watchAnimals";
        choice1.setText("Kill it");
        choice2.setText("");
        choice3.setText("");
        choice4.setText("Go west");

        Random rand = new Random();
        String animal = animalMobs[rand.nextInt(animalMobs.length)];
        String text = "A " + animal + " " + animalSound(animal) + " near you...\n";
        mainTextArea.setText(text);
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player killing animals.
    public void killAnimals() {
        position = "killAnimals";
        Random rand = new Random();
        String animal = animalMobs[rand.nextInt(animalMobs.length)];
        String text = "A " + animal + " " + animalSound(animal) + " near you...\n";
        int drop = rand.nextInt(3) + 1;
        for (int i = 0; i < drop; i++) {
            inventory.addItem(rawMeat);
        }
        String ret = "You got " + drop + " Raw Meat.\n";
        mainTextArea.setText(ret + text);
        choice1.setText("Kill it");
        choice2.setText("");
        choice3.setText("Check inventory");
        choice4.setText("Go west");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player going south to a cave.
    public void south() {
        position = "south";
        Random rand = new Random();
        Mob enemy = hostileMobList.get(rand.nextInt(hostileMobList.size()));

        String enemyName = enemy.getName();
        this.enemy = enemy;
        enemyHealth = enemy.getHp();
        mainTextArea.setText("<HTML>" + enemy.getName() + " HP: " + enemyHealth
                + "<BR><BR> A " + enemyName + " appeared!<BR>"
                + " What do you do?</HTML>");
        choice1.setText("Fight");
        choice2.setText("Run");
        choice3.setText("Eat Meat");
        choice3.setOpaque(true);
        choice3.setBorderPainted(false);
        choice3.setBackground(Color.white);
        choice3.setForeground(new Color(0,0,0,125));
        choice4.setText("");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player fighting an enemy.
    public void fight() {
        position = "fight";
        mainTextArea.setText("<HTML>" + enemy.getName() + " HP: " + enemyHealth + "<BR><BR>"
                + "You have: " + inventory.count(rawMeat) + " raw meat.</HTML>");
        choice1.setText("Attack");
        choice2.setText("Run");
        choice3.setText("Eat Meat");
        choice4.setText("");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player attacking the enemy.
    public void playerAttack() {
        position = "playerAttack";

        Random rand = new Random();
        int playerDamage = rand.nextInt(5) + 10;

        if (inventory.getItems().contains(rawMeat)) {
            mainTextArea.setText("<HTML>You attacked the " + enemy.getName()
                    + " and dealt " + playerDamage + " damage!<BR><BR><BR>You have: "
                    + inventory.getNumberOfItems().get(inventory.getItems().indexOf(rawMeat)) + " raw meat</HTML>");
        } else {
            mainTextArea.setText("<HTML>You attacked the " + enemy.getName()
                    + " and dealt " + playerDamage + " damage!</HTML>");
        }
        enemyHealth -= playerDamage;

        choice1.setText(">");
        choice2.setText("");
        choice3.setText("");
        choice4.setText("");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player being attacked by the enemy.
    public void monsterAttack() {
        position = "monsterAttack";
        int damageTaken = enemy.getBaseAttack();

        mainTextArea.setText("The monster attacked you with " + damageTaken + " damage!");
        player.setHealth(player.getHealth() - damageTaken);
        hpLabel.setText("HP: " + player.getHealth());
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player winning the battle
    // against the enemy.
    public void win() {
        position = "win";
        Random rand = new Random();
        int next = rand.nextInt(5);
        if (next != 4) {
            int dropped = rand.nextInt(3);
            mainTextArea.setText("<HTML>You defeated the " + enemy.getName() + "!<BR><BR>"
                    + "The " + enemy.getName() + " dropped " + dropped + " raw meat!</HTML>");
            for (int i = 0; i < dropped; i++) {
                inventory.addItem(rawMeat);
            }
        } else {
            mainTextArea.setText("<HTML>You defeated the " + enemy.getName() + "!<BR><BR>"
                    + "The " + enemy.getName() + " dropped a gold ingot!</HTML>");
            inventory.addItem(gold);
            win = true;
        }
        choice1.setText("Go east");

    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player losing the fight.
    public void lose() {
        position = "lose";

        mainTextArea.setText("<HTML>You are dead.<BR><BR><BR>GAME OVER!");
        choice1.setText(">");
        choice2.setText("");
        choice3.setText("");
        choice4.setText("");
        choice1.setVisible(false);
        choice2.setVisible(false);
        choice3.setVisible(false);
        choice4.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player eating meat.
    // removes 1 raw meat from the inventory if the inventory contains it
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void eatMeat() {
        position = "eatMeat";
        if (inventory.count(rawMeat) > 0) {
            if (Player.getHealth() < 90) {
                Player.setHealth(Player.getHealth() + 10);
                hpLabel.setText("HP: " + Player.getHealth());
            } else {
                Player.setHealth(100);
                hpLabel.setText("HP: 100");
            }
            inventory.removeNItems(rawMeat, 1);
            if (inventory.getItemNames().contains("Raw Meat")) {
                mainTextArea.setText("<HTML> You eat raw meat, healing yourself for 10 HP.<BR><BR>"
                        + "You now have " + Player.getHealth() + " HP.<BR><BR>"
                        + "You have: "
                        + inventory.count(rawMeat)
                        + " raw meat.</HTML>");
            } else {
                mainTextArea.setText("You don't have enough meat... get fucked!");
            }
        } else {
            mainTextArea.setText("You have no health items left! Defeat enemies for a chance to get one!");
        }
        choice1.setText(">");
        choice2.setText("");
        choice3.setText("");
        choice4.setText("");
    }

    // MODIFIES: this
    // EFFECTS: replaces the main text given the situation of the player being dead.
    public void ending() {
        position = "ending";

        mainTextArea.setText("<HTML>Guard: You killed the enemies? Thank you for protecting us! <BR><BR><BR>"
                + "You can enter the town!</HTML>");
        choice1.setVisible(false);
        choice2.setVisible(false);
        choice3.setVisible(false);
        choice4.setVisible(false);
        printLog(EventLog.getInstance());
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));

    }

    // EFFECTS: prints logs to the console
    @Override
    public void printLog(EventLog events) {
        for (Event e : events) {
            System.out.println(e.toString() + "\n");
        }
    }

}
