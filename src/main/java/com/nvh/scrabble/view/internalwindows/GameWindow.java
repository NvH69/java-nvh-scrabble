package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public class GameWindow extends JInternalFrame {
    static JTable tableP;
    static String[] titres = {"", "Tirage", "Word", "Pos", "Pts"};
    static JScrollPane scrollPaneP = new JScrollPane();

    public GameWindow() {
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
                if (tableP.getSelectedRow() > -1 && tableP.getSelectedRow() < Launcher.game.getTurn())
                    DefinitionsWindow.affiche(Launcher.game.getSolutions().get(tableP.getSelectedRow()).getWord().getWord());
            }
        });
    }

    public static void update() {
        String[][] tabPartie = new String[Launcher.game.getTurn() - 1][5];
        for (int i = 0; i < Launcher.game.getTurn() - 1; i++) {
            tabPartie[i][0] = String.valueOf(i + 1);
            tabPartie[i][1] = Launcher.game.getDrawingHistory().get(i);
            tabPartie[i][2] = Launcher.game.getSolutions().get(i).getWildcardedWord();
            tabPartie[i][3] = Launcher.game.getSolutions().get(i).getWord().toCoordinates();
            tabPartie[i][4] = String.valueOf(Launcher.game.getSolutions().get(i).getPoints());
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
        tableP.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        tableP.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        MainWindow.framePartie.setTitle("Partie : TOP");
        scrollPaneP.setViewportView(tableP);
    }

    public static void updatePlayer(int j) {
        String[][] tabPartie = new String[Launcher.game.getTurn() - 1][5];
        for (int i = 0; i < Launcher.game.getTurn() - 1; i++) {
            tabPartie[i][0] = String.valueOf(i + 1);
            tabPartie[i][1] = Launcher.game.getDrawingHistory().get(i);
            tabPartie[i][2] = Launcher.game.getPlayer(j).getWord(i).getWildcardedWord();
            tabPartie[i][3] = Launcher.game.getPlayer(j).getWord(i).getWord().toCoordinates();
            tabPartie[i][4] = String.valueOf(Launcher.game.getPlayer(j).getWord(i).getPoints());
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
        tableP.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        tableP.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        MainWindow.framePartie.setTitle("Partie : " + Launcher.game.getPlayer(j).getName());
        scrollPaneP.setViewportView(tableP);
    }
}