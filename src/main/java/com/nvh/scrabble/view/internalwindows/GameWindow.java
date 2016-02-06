package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public class GameWindow extends JInternalFrame {
    static JTable gameTable;
    static String[] titles = {"", "Tirage", "Word", "Pos", "Pts"};
    static JScrollPane gameScrollPane = new JScrollPane();

    public GameWindow() {
        setTitle("Joueur : TOP");
        setVisible(true);
        setBounds(868, 11, 307, 380);
        getContentPane().setLayout(null);
        getContentPane().setLayout(new BorderLayout());

        gameScrollPane.setBounds(0, 0, 291, 350);
        getContentPane().add(gameScrollPane);
        gameTable = new JTable() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        gameTable.setAlignmentX(RIGHT_ALIGNMENT);


        gameTable.getSelectionModel().addListSelectionListener(e -> {
            if (gameTable.getSelectedRow() > -1 && gameTable.getSelectedRow() < Launcher.game.getTurn())
                DefinitionsWindow.display(Launcher.game.getSolutions().get(gameTable.getSelectedRow()).getWord().getWord());
        });
    }

    public static void update() {
        String[][] gameTable = new String[Launcher.game.getTurn() - 1][5];
        for (int i = 0; i < Launcher.game.getTurn() - 1; i++) {
            gameTable[i][0] = String.valueOf(i + 1);
            gameTable[i][1] = Launcher.game.getDrawingHistory().get(i);
            gameTable[i][2] = Launcher.game.getSolutions().get(i).getWildcardedWord();
            gameTable[i][3] = Launcher.game.getSolutions().get(i).getWord().toCoordinates();
            gameTable[i][4] = String.valueOf(Launcher.game.getSolutions().get(i).getPoints());
        }
        DefaultTableModel m = (DefaultTableModel) GameWindow.gameTable.getModel();
        m.setDataVector(gameTable, titles);

        GameWindow.gameTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        GameWindow.gameTable.getColumnModel().getColumn(0).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(1).setPreferredWidth(58);
        GameWindow.gameTable.getColumnModel().getColumn(1).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        GameWindow.gameTable.getColumnModel().getColumn(2).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(3).setPreferredWidth(22);
        GameWindow.gameTable.getColumnModel().getColumn(3).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(4).setPreferredWidth(30);
        GameWindow.gameTable.getColumnModel().getColumn(4).setResizable(false);
        GameWindow.gameTable.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        GameWindow.gameTable.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        MainWindow.gameFrame.setTitle("Partie : TOP");
        gameScrollPane.setViewportView(GameWindow.gameTable);
    }

    public static void updatePlayer(int player) {
        String[][] gameTable = new String[Launcher.game.getTurn() - 1][5];
        for (int i = 0; i < Launcher.game.getTurn() - 1; i++) {
            gameTable[i][0] = String.valueOf(i + 1);
            gameTable[i][1] = Launcher.game.getDrawingHistory().get(i);
            gameTable[i][2] = Launcher.game.getPlayer(player).getWord(i).getWildcardedWord();
            gameTable[i][3] = Launcher.game.getPlayer(player).getWord(i).getWord().toCoordinates();
            gameTable[i][4] = String.valueOf(Launcher.game.getPlayer(player).getWord(i).getPoints());
        }
        DefaultTableModel m = (DefaultTableModel) GameWindow.gameTable.getModel();
        m.setDataVector(gameTable, titles);
        GameWindow.gameTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        GameWindow.gameTable.getColumnModel().getColumn(0).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(1).setPreferredWidth(58);
        GameWindow.gameTable.getColumnModel().getColumn(1).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        GameWindow.gameTable.getColumnModel().getColumn(2).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(3).setPreferredWidth(22);
        GameWindow.gameTable.getColumnModel().getColumn(3).setResizable(false);
        GameWindow.gameTable.getColumnModel().getColumn(4).setPreferredWidth(30);
        GameWindow.gameTable.getColumnModel().getColumn(4).setResizable(false);
        GameWindow.gameTable.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        GameWindow.gameTable.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        MainWindow.gameFrame.setTitle("Partie : " + Launcher.game.getPlayer(player).getName());
        gameScrollPane.setViewportView(GameWindow.gameTable);
    }
}