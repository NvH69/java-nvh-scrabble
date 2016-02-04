package com.nvh.view.internalwindows;

import com.nvh.controller.Scrabble;
import com.nvh.view.MainWindow;
import com.nvh.Launcher;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class ScoreWindow extends JInternalFrame implements Observer {
    public static JTable tableS;
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
        tableS = new JTable() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableS.setAlignmentX(CENTER_ALIGNMENT);
        tableS.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tableS.getSelectedRow() > 0 &&
                        tableS.getSelectedRow() < Launcher.partie.getNbJoueurs())
                    GameWindow.updatePlayer(tableS.getSelectedRow());
                else if (tableS.getSelectedRow() == 0 &&
                        tableS.getSelectedRow() < Launcher.partie.getNbJoueurs())
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
        DefaultTableModel m = (DefaultTableModel) tableS.getModel();
        m.setDataVector(r, titres);
        tableS.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableS.getColumnModel().getColumn(0).setResizable(false);
        tableS.getColumnModel().getColumn(1).setPreferredWidth(30);
        tableS.getColumnModel().getColumn(1).setResizable(false);
        tableS.getColumnModel().getColumn(2).setPreferredWidth(30);
        tableS.getColumnModel().getColumn(1).setResizable(false);
        tableS.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        tableS.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        scrollPaneS.setViewportView(tableS);
    }
}
