package com.nvh.view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public class FenetrePartie extends JInternalFrame {
    static JTable tableP;
    static String[] titres = {"", "Tirage", "Mot", "Pos", "Pts"};
    static JScrollPane scrollPaneP = new JScrollPane();

    public FenetrePartie() {
        setTitle("Joueur : TOP");
        setVisible(true);
        setBounds(868, 11, 307, 380);
        getContentPane().setLayout(null);
        getContentPane().setLayout(new BorderLayout());

        scrollPaneP.setBounds(0, 0, 291, 350);
        getContentPane().add(scrollPaneP);
        tableP = new JTable() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableP.setAlignmentX(RIGHT_ALIGNMENT);


        tableP.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tableP.getSelectedRow() > -1 && tableP.getSelectedRow() < Main.partie.getTour())
                    FenetreDefinitions.affiche(Main.partie.getSolutions().get(tableP.getSelectedRow()).getM().getMot());
            }
        });
    }

    public static void update() {
        String[][] tabPartie = new String[Main.partie.getTour() - 1][5];
        for (int i = 0; i < Main.partie.getTour() - 1; i++) {
            tabPartie[i][0] = String.valueOf(i + 1);
            tabPartie[i][1] = Main.partie.getHistoTirage().get(i);
            tabPartie[i][2] = Main.partie.getSolutions().get(i).getMotJokers();
            tabPartie[i][3] = Main.partie.getSolutions().get(i).getM().toCoor();
            tabPartie[i][4] = String.valueOf(Main.partie.getSolutions().get(i).getPoints());
        }
        DefaultTableModel m = (DefaultTableModel) tableP.getModel();
        m.setDataVector(tabPartie, titres);

        tableP.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableP.getColumnModel().getColumn(0).setResizable(false);
        tableP.getColumnModel().getColumn(1).setPreferredWidth(58);
        tableP.getColumnModel().getColumn(1).setResizable(false);
        tableP.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableP.getColumnModel().getColumn(2).setResizable(false);
        tableP.getColumnModel().getColumn(3).setPreferredWidth(22);
        tableP.getColumnModel().getColumn(3).setResizable(false);
        tableP.getColumnModel().getColumn(4).setPreferredWidth(30);
        tableP.getColumnModel().getColumn(4).setResizable(false);
        tableP.getTableHeader().setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 12));
        tableP.setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 12));
        FenetrePrincipale.framePartie.setTitle("Partie : TOP");
        scrollPaneP.setViewportView(tableP);
    }

    public static void updatePlayer(int j) {
        String[][] tabPartie = new String[Main.partie.getTour() - 1][5];
        for (int i = 0; i < Main.partie.getTour() - 1; i++) {
            tabPartie[i][0] = String.valueOf(i + 1);
            tabPartie[i][1] = Main.partie.getHistoTirage().get(i);
            tabPartie[i][2] = Main.partie.getJoueur(j).getCoupJoue(i).getMotJokers();
            tabPartie[i][3] = Main.partie.getJoueur(j).getCoupJoue(i).getM().toCoor();
            tabPartie[i][4] = String.valueOf(Main.partie.getJoueur(j).getCoupJoue(i).getPoints());
        }
        DefaultTableModel m = (DefaultTableModel) tableP.getModel();
        m.setDataVector(tabPartie, titres);
        tableP.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableP.getColumnModel().getColumn(0).setResizable(false);
        tableP.getColumnModel().getColumn(1).setPreferredWidth(58);
        tableP.getColumnModel().getColumn(1).setResizable(false);
        tableP.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableP.getColumnModel().getColumn(2).setResizable(false);
        tableP.getColumnModel().getColumn(3).setPreferredWidth(22);
        tableP.getColumnModel().getColumn(3).setResizable(false);
        tableP.getColumnModel().getColumn(4).setPreferredWidth(30);
        tableP.getColumnModel().getColumn(4).setResizable(false);
        tableP.getTableHeader().setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 12));
        tableP.setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 12));
        FenetrePrincipale.framePartie.setTitle("Partie : " + Main.partie.getJoueur(j).getNom());
        scrollPaneP.setViewportView(tableP);
    }
}