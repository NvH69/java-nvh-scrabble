package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.controller.ScrabbleController;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class ScoreWindow extends JInternalFrame implements Observer {
    public static JTable scoreTable;
    static String[] titles = {"Nom", "Score", "%"};
    static JScrollPane scoreScrollPane;


    public ScoreWindow() {
        setTitle("Scores");
        setBounds(675, 11, 190, 209);
        setVisible(true);

        getContentPane().setLayout(new BorderLayout());
        scoreScrollPane = new JScrollPane();
        scoreScrollPane.setBounds(0, 0, 230, 350);
        getContentPane().add(scoreScrollPane);
        scoreTable = new JTable() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        scoreTable.setAlignmentX(CENTER_ALIGNMENT);
        scoreTable.getSelectionModel().addListSelectionListener(e -> {
            if (scoreTable.getSelectedRow() > 0 &&
                    scoreTable.getSelectedRow() < ScrabbleController.game.getNumberOfPlayers())
                GameWindow.updatePlayer(scoreTable.getSelectedRow());
            else if (scoreTable.getSelectedRow() == 0 &&
                    scoreTable.getSelectedRow() < ScrabbleController.game.getNumberOfPlayers())
                GameWindow.update();
        });
    }

    @Override
    public void update(Observable game, Object arg1) {

        String[][] r = new String[((Scrabble) game).getNumberOfPlayers()][3];
        for (int i = 0; i < ((Scrabble) game).getNumberOfPlayers(); i++) {
            r[i][0] = ((Scrabble) game).getPlayer(i).getName();
            r[i][1] = String.valueOf(((Scrabble) game).getPlayer(i).getPoints());
            if (((Scrabble) game).getPlayer(0).getPoints() == 0) r[i][2] = "---";
            else {
                double pct = ScrabbleController.game.getPlayer(i).getPoints() * 100 / ((Scrabble) game).getPlayer(0).getPoints();
                if (ScrabbleController.game.getPlayer(i).getWordsCount() >
                        ScrabbleController.game.getSolutions().size()) r[i][2] = "OK !";
                else r[i][2] = (int) (pct) + "%";

            }
        }
        DefaultTableModel defaultTableModel = (DefaultTableModel) scoreTable.getModel();
        defaultTableModel.setDataVector(r, titles);
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        scoreTable.getColumnModel().getColumn(0).setResizable(false);
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        scoreTable.getColumnModel().getColumn(1).setResizable(false);
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(30);
        scoreTable.getColumnModel().getColumn(1).setResizable(false);
        scoreTable.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        scoreTable.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        scoreScrollPane.setViewportView(scoreTable);
    }
}
