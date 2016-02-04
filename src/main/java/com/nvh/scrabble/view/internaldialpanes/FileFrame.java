package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.model.Serializer;
import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.view.MainWindow;
import com.nvh.scrabble.view.internalwindows.GameWindow;
import com.nvh.scrabble.view.internalwindows.BoardWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class FileFrame extends JFrame {
    private static final long serialVersionUID = -4105494649356150949L;
    private JPanel contentPane;
    private JTree arbre;
    private DefaultMutableTreeNode racine;
    private JScrollPane arbreScroll = new JScrollPane();
    JTextArea textSave = new JTextArea();
    JButton btnAction = new JButton();
    String retour = new String("wait");
    String chemin = Dictionary.chemin + "/savedgames/";
    Calendar c = Calendar.getInstance();

    public FileFrame() {

        this.setBounds(600, 460, 276, 407);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        arbreScroll.setBounds(10, 11, 250, 269);
        contentPane.add(arbreScroll);

        setResizable(false);
        setVisible(true);
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnAction.setBounds(171, 344, 89, 23);
        contentPane.add(btnAction);
        btnAnnuler.setBounds(10, 344, 89, 23);
        contentPane.add(btnAnnuler);
        textSave.setBounds(20, 291, 227, 32);
        contentPane.add(textSave);
        treeAfficher();
    }

    public void treeAfficher() {
        this.racine = new DefaultMutableTreeNode("Scrabble");
        DefaultMutableTreeNode fichiers = new DefaultMutableTreeNode("savedgames");
        try {
            for (File nom : new File(chemin).listFiles()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
                fichiers.add(this.listFile(nom, node));
            }
        } catch (NullPointerException e) {
        }

        this.racine.add(fichiers);

        arbre = new JTree(this.racine);
        arbre.expandRow(arbre.getRowCount() - 1);
        arbreScroll.setViewportView(arbre);
        arbreScroll.setVisible(true);
        arbre.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                textSave.setText(arbre.getLastSelectedPathComponent().toString());

            }
        });
    }

    public void dialEcrire() {
        textSave.setVisible(true);
        textSave.setText(dateActuelle() + ".dat");
        btnAction.setText("Sauvegarder");
        btnAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Serializer.ecrire(Launcher.partie, textSave.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
            }
        });

    }

    public void dialLire(Scrabble partie) {
        textSave.setVisible(false);
        btnAction.setText("Reprendre");
        btnAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Scrabble partieChargee = null;
                if (!arbre.isSelectionEmpty()) {
                    try {
                        try {
                            partieChargee = Serializer.lire(arbre.getLastSelectedPathComponent().toString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } catch (ClassNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    //red�finit la partie en cours avec les attributs de la partie charg�e
                    partie.copyOf(partieChargee);
                    //dessine la grille
                    BoardWindow.displayGrille(partie.getGrille(),
                            MainWindow.frameGrille);

                    //invalide les menus de d�marrage si la partie est commenc�e
                    if (partie.isPartieEncours()) {
                        MainWindow.mntmAjouterJoueur.setEnabled(false);
                        MainWindow.mntmLancer.setEnabled(false);
                        if (partie.isAutoTop()) MainWindow.chckbxmntmTopAutomatique.setSelected(true);
                        if (partie.isAuto()) MainWindow.rdbtnmntmTirageAutomatique.setSelected(true);
                        else MainWindow.rdbtnmntmTirageManuel.setSelected(true);

                    }
                    Launcher.phase = 0;
                    Launcher.currentTurn = partie.getTour();

                    GameWindow.update();
                    dispose();
                }
            }
        });
    }

    public String dateActuelle() {
        int jour = c.get(Calendar.DAY_OF_MONTH);
        int mois = c.get(Calendar.MONTH);
        int annee = c.get(Calendar.YEAR);
        String reponse = String.valueOf(jour) + "_" + String.valueOf(mois) + "_" + String.valueOf(annee);
        return reponse;

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
