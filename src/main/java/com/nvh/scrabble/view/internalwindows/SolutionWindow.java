package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.model.Scrabble.Solution;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class SolutionWindow extends JInternalFrame implements Observer {
    public static JTable table;
    static String[] titles = {"N°", "Word", "Pos", "Score", ""};
    public static JScrollPane solutionScrollPane;
    public static JButton showButton = new JButton("Voir toutes les solutions");

    public SolutionWindow() {
        solutionScrollPane = new JScrollPane();
        solutionScrollPane.setBounds(0, 23, 483, 297);
        solutionScrollPane.setVisible(false);
        setVisible(true);

        setBounds(675, 400, 499, 359);
        Action action = new SwingAction();
        showButton.setAction(action);
        showButton.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 16));
        showButton.setBounds(10, 0, 463, 23);

        getContentPane().setLayout(null);
        getContentPane().add(solutionScrollPane);
        getContentPane().add(showButton);
        getContentPane().setLayout(new BorderLayout());

        table = new JTable() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table.setAlignmentX(RIGHT_ALIGNMENT);
        table.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        table.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
        table.setSelectionMode(0);
    }

    private class SwingAction extends AbstractAction {

        public SwingAction() {
            putValue(NAME, "Afficher les solutions");
            putValue(SHORT_DESCRIPTION, "Affiche / masque la grille de solutions");
        }

        public void actionPerformed(ActionEvent e) {

            solutionScrollPane.setVisible(!solutionScrollPane.isVisible());
            if (solutionScrollPane.isVisible()) showButton.setText("Masquer les solutions");
            else showButton.setText("Afficher les solutions");
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        if (arg instanceof ArrayList) {

            DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
            Object[][] solutions = new Object[((List<?>) arg).size() + 1][5];

            int index = 0;
            solutions[index++][1] = "-------";
            solutions[index][3] = "0";
            for (Object solution : (List<?>) arg) {
                solutions[index][0] = index;
                solutions[index][1] = ((Solution) solution).getWildcardedWord();
                solutions[index][2] = ((Solution) solution).getWord().toCoordinates();
                solutions[index][3] = ((Solution) solution).getPoints();
                if (Objects.equals(((Solution) solution).getInformation()[1], "[]") && Launcher.game.getDrawing().length() > 6)
                    solutions[index][4] = "Scrabble!";
                else solutions[index][4] = "";
                index++;
            }

            defaultTableModel.setDataVector(solutions, titles);
            table.getColumnModel().getColumn(0).setPreferredWidth(20);
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setPreferredWidth(20);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setPreferredWidth(30);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setPreferredWidth(120);
            table.getColumnModel().getColumn(4).setResizable(false);
            table.getTableHeader().setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
            table.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 14));
            solutionScrollPane.setViewportView(table);
            solutionScrollPane.setVisible(false);
        }
    }
}
