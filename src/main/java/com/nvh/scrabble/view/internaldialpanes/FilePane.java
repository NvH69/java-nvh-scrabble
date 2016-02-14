package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.controller.ScrabbleController;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.service.Serializer;
import com.nvh.scrabble.view.MainWindow;
import com.nvh.scrabble.view.internalwindows.BoardWindow;
import com.nvh.scrabble.view.internalwindows.GameWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

public class FilePane extends JFrame {
    private static final long serialVersionUID = -4105494649356150949L;
    private static URL directory;
    JTextArea textArea = new JTextArea();
    JButton actionButton = new JButton();
    Calendar calendar = Calendar.getInstance();
    private JTree tree;
    private JScrollPane scrollPane = new JScrollPane();

    public FilePane() {

        try {
            directory = new File(System.getProperty("user.dir")).toURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(directory);
        DefaultMutableTreeNode savedgames = new DefaultMutableTreeNode(directory.getPath());
        try {
            for (File file : new File(directory.getPath()).listFiles()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
                if (file.getName().endsWith(".dat") || file.isDirectory())
                    savedgames.add(this.listFile(file, node));
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

    public void writeDialogBox() {
        textArea.setVisible(true);
        textArea.setText(actualDate() + ".dat");
        actionButton.setText("Sauvegarder");
        actionButton.addActionListener(e -> {
            try {
                Serializer.write(ScrabbleController.game, textArea.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            dispose();
        });
    }

    public void readDialogBox(Scrabble game) {
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
