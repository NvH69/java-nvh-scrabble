package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.model.Serializer;
import com.nvh.scrabble.view.MainWindow;
import com.nvh.scrabble.view.internalwindows.BoardWindow;
import com.nvh.scrabble.view.internalwindows.GameWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class FileFrame extends JFrame {
    private static final long serialVersionUID = -4105494649356150949L;
    private JTree tree;
    private JScrollPane scrollPane = new JScrollPane();
    JTextArea textArea = new JTextArea();
    JButton actionButton = new JButton();
    String response = "wait";
    String path = Dictionary.path + "/savedgames/";
    Calendar calendar = Calendar.getInstance();

    public FileFrame() {

        this.setBounds(600, 460, 276, 407);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        scrollPane.setBounds(10, 11, 250, 269);
        contentPane.add(scrollPane);

        setResizable(false);
        setVisible(true);
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.addActionListener(e -> dispose());
        actionButton.setBounds(171, 344, 89, 23);
        contentPane.add(actionButton);
        btnAnnuler.setBounds(10, 344, 89, 23);
        contentPane.add(btnAnnuler);
        textArea.setBounds(20, 291, 227, 32);
        contentPane.add(textArea);
        displayTree();
    }

    public void displayTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Scrabble");
        DefaultMutableTreeNode savedgames = new DefaultMutableTreeNode("savedgames");
        try {
            for (File nom : new File(path).listFiles()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
                savedgames.add(this.listFile(nom, node));
            }
        } catch (NullPointerException ignored) {
        }

        root.add(savedgames);

        tree = new JTree(root);
        tree.expandRow(tree.getRowCount() - 1);
        scrollPane.setViewportView(tree);
        scrollPane.setVisible(true);
        tree.getSelectionModel().addTreeSelectionListener(
                e -> textArea.setText(tree.getLastSelectedPathComponent().toString()));
    }

    public void writeDial() {
        textArea.setVisible(true);
        textArea.setText(actualDate() + ".dat");
        actionButton.setText("Sauvegarder");
        actionButton.addActionListener(e -> {
            try {
                Serializer.write(Launcher.game, textArea.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            dispose();
        });

    }

    public void readDial(Scrabble game) {
        textArea.setVisible(false);
        actionButton.setText("Reprendre");
        actionButton.addActionListener(e -> {
            Scrabble partieChargee = null;
            if (!tree.isSelectionEmpty()) {
                try {
                    try {
                        partieChargee = Serializer.read(tree.getLastSelectedPathComponent().toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                //redéfinit la game en cours avec les attributs de la game chargée
                game.copyOf(partieChargee);
                //dessine la grille
                BoardWindow.displayGrid(game.getGrid(),
                        MainWindow.gridFrame);

                //invalide les menus de démarrage si la game est commencée
                if (game.isRunning()) {
                    MainWindow.addPlayerMenuItem.setEnabled(false);
                    MainWindow.LauncherMenuItem.setEnabled(false);
                    if (game.isAutoTop()) MainWindow.autoTopCheckBox.setSelected(true);
                    if (game.isAutoDrawing()) MainWindow.autoDrawingButton.setSelected(true);
                    else MainWindow.manualDrawingButton.setSelected(true);
                }
                Launcher.phase = 0;
                Launcher.currentTurn = game.getTurn();

                GameWindow.update();
                dispose();
            }
        });
    }

    public String actualDate() {
        int jour = calendar.get(Calendar.DAY_OF_MONTH);
        int mois = calendar.get(Calendar.MONTH);
        int annee = calendar.get(Calendar.YEAR);
        return String.valueOf(jour) + "_" + String.valueOf(mois) + "_" + String.valueOf(annee);
    }

    private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node) {

        if (file.isFile())
            return new DefaultMutableTreeNode(file.getName());
        else {
            for (File nom : file.listFiles()) {
                DefaultMutableTreeNode subNode;
                if (nom.isDirectory()) {
                    subNode = new DefaultMutableTreeNode(nom.getName() + "\\");
                    node.add(this.listFile(nom, subNode));
                } else {
                    subNode = new DefaultMutableTreeNode(nom.getName());
                }
                node.add(subNode);
            }
            return node;
        }
    }
}
