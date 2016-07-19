package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class ConfirmationPane extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton okButton = new JButton("Confirmer");
    private JTextField textField = new JTextField();

    public ConfirmationPane(Scrabble partie) {
        setTitle("TIRAGE");
        setModal(true);
        setBounds(600, 460, 469, 193);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        okButton.setBounds(175, 102, 100, 47);
        contentPane.add(okButton);

        textField.setSelectionColor(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLUE);
        textField.setDisabledTextColor(UIManager.getColor("ToggleButton.light"));
        textField.setFont(new Font(MainWindow.mainFont, Font.BOLD, 48));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setText(partie.getDrawingHistory().get(partie.getTurn() - 1));
        textField.setBounds(10, 11, 443, 80);
        textField.setEditable(false);
        contentPane.add(textField);

        okButton.addActionListener(e -> {// tirage validé
            dispose();
        });

        this.setVisible(true);
    }

    public ConfirmationPane(Scrabble game, int player, Scrabble.Solution solution) {
        setTitle("Confirmation");
        setModal(true);
        setBounds(600, 460, 469, 193);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        okButton.setBounds(322, 102, 100, 47);
        contentPane.add(okButton);
        textField.setSelectionColor(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLUE);
        textField.setDisabledTextColor(UIManager.getColor("ToggleButton.light"));
        textField.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 16));
        textField.setHorizontalAlignment(SwingConstants.CENTER);

        if (Objects.equals(solution.getWord().getWord(), "-------"))
            textField.setText(game.getPlayer(player).getName() + " joue un coup invalide");
        else

            textField.setText(game.getPlayer(player).getName() + " joue : " + solution.getWord().getWord() + " en " + solution.getWord().toCoordinates());

        textField.setBounds(10, 11, 443, 80);
        textField.setEditable(false);
        contentPane.add(textField);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBounds(34, 102, 100, 47);
        if (player > 0) contentPane.add(cancelButton);
        okButton.setVisible(true);
        okButton.addActionListener(e -> {    //pour tout joueur (sauf TOP) on ajoute le coup joué
            //le coup joué par TOP est validé par setSolutions
            if (player > 0)
                game.getPlayer(player).setWord(solution);
            if (game.getMainTimer().isOn()) game.getMainTimer().arret();
            dispose();
        });

        cancelButton.addActionListener(e -> {// si annulé, pas d'action
            dispose();
        });

        this.setVisible(true);
    }
}
