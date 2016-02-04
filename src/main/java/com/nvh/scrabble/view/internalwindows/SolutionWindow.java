package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Scrabble.Solution;
import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class SolutionWindow extends JInternalFrame implements Observer {
    public static JTable table;
    static String[] titres = {"N�", "Mot", "Pos", "Score", ""};
    public static JScrollPane scrollPane;
    public static JButton btnVoir = new JButton("Voir toutes les solutions");
    private final Action action = new SwingAction();

    public SolutionWindow() {
        scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 23, 483, 297);
        scrollPane.setVisible(false);
        setVisible(true);

        setBounds(675, 400, 499, 359);
        btnVoir.setAction(action);
        btnVoir.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 16));
        btnVoir.setBounds(10, 0, 463, 23);

        getContentPane().setLayout(null);
        getContentPane().add(scrollPane);
        getContentPane().add(btnVoir);
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

            scrollPane.setVisible(!scrollPane.isVisible());
            if (scrollPane.isVisible()) btnVoir.setText("Masquer les solutions");
            else btnVoir.setText("Afficher les solutions");
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        if (arg instanceof List<?>) {

            DefaultTableModel m = (DefaultTableModel) table.getModel();
            Object[][] sol = new Object[((List<?>) arg).size() + 1][5];

            int index = 0;
            sol[index++][1] = "-------";
            sol[index][3] = "0";
            for (Object s : (List<?>) arg) {
                sol[index][0] = index;
                sol[index][1] = ((Solution) s).getMotJokers();
                sol[index][2] = ((Solution) s).getM().toCoor();
                sol[index][3] = ((Solution) s).getPoints();
                if (((Solution) s).getRetour()[1] == "[]" && Launcher.partie.getTirage().length() > 6)
                    sol[index][4] = "Scrabble!";
                else sol[index][4] = "";
                index++;
            }

            m.setDataVector(sol, titres);
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
            scrollPane.setViewportView(table);
            scrollPane.setVisible(false);
        }
    }
}
