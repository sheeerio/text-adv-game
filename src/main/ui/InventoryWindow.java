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

// InventoryWindow contains information about the inventory drag and drop system.
public class InventoryWindow {
    private Inventory inv;
    private JFrame frame;
    private JTextArea recipeTextArea;
    private JButton recipeButton;
    private JPanel recipePanel;


    // EFFECTS: constructs a new inventory window containing a new frame
    public InventoryWindow(Inventory inv, int x, int y) {

        this.inv = inv;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                         | UnsupportedLookAndFeelException ex) {
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

    // TestPane class contains information about the main panel of the InventoryWindow window.
    public class TestPane extends JPanel {

        // EFFECTS: constructs a JPanel and initializes four panels inside it
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

        // MODIFIES: this
        // EFFECTS: constructs a new panel and adds the inventory items as buttons
        protected JPanel createLeftPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.black);
            panel.setForeground(Color.white);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
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
                InventoryWindow.this.extracted(index, btn);
            }
            return panel;
        }

        // MODIFIES: this
        // EFFECTS: creates a right panel that contains a label
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

        // MODIFIES: this
        // EFFECTS: creates a bottom left panel that contains a button
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

        // MODIFIES: this
        // EFFECTS: creates a bottom left panel that contains a recipe label and a button to create it
        protected JPanel createBottomRightPanel() {
            extracted();
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
                    new InventoryWindow(inv, getWidth(), getHeight());
                }
            });
            return recipePanel;
        }

    }

    private void extracted(int index, JButton btn) {
        btn.setTransferHandler(new ValueExportTransferHandler(inv.getItemNames().get(index) + " ("
                + inv.getNumberOfItems().get(index) + ")\n"));

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

    // MODIFIES: this
    // EFFECTS: creates recipe panel for the window
    private void extracted() {
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
    }

    // ValueExportTransferHandler contains information about data exporting in the Drag and Drop system
    public static class ValueExportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
        private String value;

        // constructs and assigns the given value as the main value for the class
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

        // EFFECTS: prepares data to be transferable
        @Override
        protected Transferable createTransferable(JComponent c) {
            Transferable t = new StringSelection(getValue());
            return t;
        }

        // MODIFIES: source list
        // EFFECTS: clears items from source when move operation is performed
        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            super.exportDone(source, data, action);
            // Decide what to do after the drop has been accepted
        }

    }

    // ValueImportTransferHandler contains information about the data importing
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

    // MODIFIES: this
    // EFFECTS:
    public void getRecipe(JButton button) {
        if (button.getText().contains("Wooden Planks")
                && inv.getNumberOfItems().get(inv.getItemNames().indexOf("Wooden Planks")) >= 2) {
            recipeTextArea.setText("Do you want to create sticks?");
            recipeButton.setText("Create Sticks");
            recipeButton.setVisible(true);
        } else if (button.getText().contains("Sticks") && inv.getItemNames().contains("Wooden Planks")
                && inv.getNumberOfItems().get(inv.getItemNames().indexOf("Wooden Planks")) >= 2) {
            recipeTextArea.setText("Do you want to create a wooden sword?");
            recipeButton.setText("Create Wooden Sword");
            recipeButton.setVisible(true);
        }
    }
}
