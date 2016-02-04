package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;
import com.nvh.scrabble.Launcher;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class ScoreWindow extends JInternalFrame implements Observer {
    public static JTable scoreTable;
    static String[] titres = {"Nom", "Score", "%"};
    static JScrollPane scrollPaneS;


    public ScoreWindow() {
        setTitle("Scores");
        setBounds(675, 11, 190, 209);
        setVisible(true);


        getContentPane().setLayout(new BorderLayout());
        scrollPaneS = new JScrollPane();
        scrollPaneS.setBounds(0, 0, 230, 350);
        getContentPane().add(scrollPaneS);
        scoreTable = new JTable() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        scoreTable.setAlignmentX(CENTER_ALIGNMENT);
        scoreTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (scoreTable.getSelectedRow() > 0 &&
                        scoreTable.getSelectedRow() < Launcher.partie.getNbJoueurs())
                    GameWindow.updatePlayer(scoreTable.getSelectedRow());
                else if (scoreTable.getSelectedRow() == 0 &&
                        scoreTable.getSelectedRow() < Launcher.partie.getNbJoueurs())
                    GameWindow.update();
            }
        });
    }

    @Override
    public void update(Observable partie, Object arg1) {

        String[][] r = new String[((Scrabble) partie).getNbJoueurs()][3];
        for (int i = 0; i < ((Scrabble) partie).getNbJoueurs(); i++) {
            r[i][0] = ((Scrabble) partie).getJoueur(i).getNom();
            r[i][1] = String.valueOf(((Scrabble) partie).getJoueur(i).getPoints());
            if (((Scrabble) partie).getJoueur(0).getPoints() == 0) r[i][2] = "---";
            else {
                double pct = Launcher.partie.getJoueur(i).getPoints() * 100 / ((Scrabble) partie).getJoueur(0).getPoints();
                if (Launcher.partie.getJoueur(i).getNbCoupsJoues() >
                        Launcher.partie.getSolutions().size()) r[i][2] = "OK !";
                else r[i][2] = (int) (pct) + "%";

            }
        }
        DefaultTableModel m = (DefaultTableModel) scoreTable.getModel();
        m.setDataVector(r, titres);
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        scoreTable.getColumnModel().getColumn(0).setResizable(false);
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        scoreTable.getColumnModel().getColumn(1).setResizable(false);
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(30);
        scoreTable.getColumnModel().getColumn(1).setResizable(false);
        scoreTable.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        scoreTable.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        scrollPaneS.setViewportView(scoreTable);
    }
}
