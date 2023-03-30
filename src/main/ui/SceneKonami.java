package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// this class contains information about the typewriting animation panel used for displaying main text of the game
class SceneKonami extends javax.swing.JPanel {

    private int index = 0;
    private String message;
    private javax.swing.Timer timer;
    private javax.swing.JLabel texter;

    // EFFECTS: constructs a default panel and sets message to the given string
    public SceneKonami(String message) {
        initComponents();
        this.message = message;
    }

    // MODIFIES: this
    // EFFECTS: initializes the background and text label for the class
    private void initComponents() {

        texter = new javax.swing.JLabel();
        setBackground(new Color(0,0,0,125));
        texter.setBackground(new Color(0,0,0,125));
        texter.setForeground(Color.white);
        setLayout(new BorderLayout(5,5));
//        jButton1.setText("Type Message");
//        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
//        add(jButton1, BorderLayout.SOUTH);
        add(texter, BorderLayout.NORTH);
        setPreferredSize(new Dimension(600,200));
    }


    // MODIFIES: this
    // EFFECTS: uses a swing timer to print the given message as a type of animation
    public void slowPrint(String message) {

        if (timer != null && timer.isRunning()) {
            return;
        }
        index   = 0;
        texter.setText("");

        timer = new javax.swing.Timer(10,new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                texter.setText(texter.getText() + String.valueOf(message.charAt(index)));
                index++;
                if (index >= message.length()) {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    public void setText(String message) {
        this.message = message;
        slowPrint(message);
    }
}