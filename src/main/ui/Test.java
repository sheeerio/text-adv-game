package ui;

import model.Inventory;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.*;

import static ui.Game.*;

public class Test {
    private Inventory inv;
    private JFrame frame;
    private JTextArea recipeTextArea;
    private JButton recipeButton;
    private JPanel recipePanel;


    public static void main(String[] args) {
        new Test(new Inventory(), 300, 300);
    }

    public Test(Inventory inv, int x, int y) {

        this.inv = inv;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                frame = new JFrame("Testing");
                frame.setSize(x, y);
                frame.setBackground(Color.black);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        public TestPane() {
            setLayout(new GridLayout(2, 2));
            setSize(300,300);
            setBackground(Color.black);
            setForeground(Color.white);
            add(createLeftPanel());
            add(createRightPanel());
            add(createBottomLeftPanel());
            add(createBottomRightPanel());
        }

        protected JPanel createLeftPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.black);
            panel.setForeground(Color.white);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            for (int index = 0; index < inv.getItems().size(); index++) {
                JButton btn = new JButton(inv.getItemNames().get(index) + " ("
                        + inv.getNumberOfItems().get(index) + ")\n");
                btn.setBackground(Color.black);
                btn.setPreferredSize(new Dimension(120, 20));
                int finalIndex = index;
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getRecipe(btn);
                        inv.setCurrentItemTo(finalIndex);
                    }
                });
                panel.add(btn, gbc);
                btn.setTransferHandler(new ValueExportTransferHandler(inv.getItemNames().get(index) + " ("
                        + inv.getNumberOfItems().get(index)+ ")\n"));

                btn.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        JButton button = (JButton) e.getSource();
                        TransferHandler handle = button.getTransferHandler();
                        handle.exportAsDrag(button, e, TransferHandler.COPY);
                        getRecipe(button);
                    }
                });
            }
            return panel;
        }

        protected JPanel createRightPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.black);
            panel.setForeground(Color.white);
            JLabel label = new JLabel("Drop items");
            label.setForeground(Color.white);
            label.setBorder(new CompoundBorder(new LineBorder(Color.white), new EmptyBorder(20, 20, 20, 20)));
            label.setTransferHandler(new ValueImportTransferHandler());
            panel.add(label);
            return panel;
        }

        protected JPanel createBottomLeftPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.black);
            panel.setForeground(Color.white);

            JButton button = new JButton("Done");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                }
            });
            panel.add(button);
            return panel;
        }

        protected JPanel createBottomRightPanel() {
            recipePanel = new JPanel(new GridBagLayout());
            recipePanel.setBackground(Color.black);
            recipePanel.setForeground(Color.white);
            recipeTextArea = new JTextArea("This is where the recipes will be!");
            recipeTextArea.setBackground(Color.black);
            recipeTextArea.setForeground(Color.white);
            recipeTextArea.setLineWrap(true);
            recipeButton = new JButton();
            recipeButton.setBackground(Color.gray);
            recipePanel.add(recipeTextArea);
            recipeButton.setVisible(false);
            recipePanel.add(recipeButton);
            recipeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = recipeButton.getText();
                    if (text.contains("Sticks")) {
                        for (int i = 0; i < 4; i++) {
                            inv.addItem(sticks);
                        }
                        inv.removeNItems(woodenPlank, 2);
                    } else if (text.contains("Sword")) {
                        inv.addItem(woodenSword);
                    }
                    inv.removeNItems(sticks, 1);
                    inv.removeNItems(woodenPlank, 2);
                    frame.setVisible(false);
                    new Test(inv, getWidth(), getHeight());
                }
            });
            return recipePanel;
        }

    }

    public static class ValueExportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
        private String value;

        public ValueExportTransferHandler(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            Transferable t = new StringSelection(getValue());
            return t;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            super.exportDone(source, data, action);
            // Decide what to do after the drop has been accepted
        }

    }

    public static class ValueImportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

        public ValueImportTransferHandler() {
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean accept = false;
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                    if (value instanceof String) {
                        Component component = support.getComponent();
                        if (component instanceof JLabel) {
                            ((JLabel) component).setText(value.toString());
                            accept = true;
                        }
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
            return accept;
        }
    }

    public void getRecipe(JButton button) {
        if (button.getText().contains("Wooden Planks") &&
                inv.getNumberOfItems().get(inv.getItemNames().indexOf("Wooden Planks")) >= 2) {
            recipeTextArea.setText("Do you want to create sticks?");
            recipeButton.setText("Create Sticks");
            recipeButton.setVisible(true);
        } else if (button.getText().contains("Sticks") && inv.getItemNames().contains("Wooden Planks") &&
        inv.getNumberOfItems().get(inv.getItemNames().indexOf("Wooden Planks")) >= 2) {
            recipeTextArea.setText("Do you want to create a wooden sword?");
            recipeButton.setText("Create Wooden Sword");
            recipeButton.setVisible(true);
        }
    }
}
