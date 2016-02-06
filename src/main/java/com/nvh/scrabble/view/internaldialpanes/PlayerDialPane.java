package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PlayerDialPane extends JDialog {
    private static final long serialVersionUID = 1L;
    JLabel titleLabel = new JLabel("Entrer le pseudo du nouveau joueur");
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Annuler");
    JFormattedTextField playerTextField = new JFormattedTextField();

    public PlayerDialPane(Scrabble game) {
        setModal(true);
        setBounds(600, 460, 410, 200);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setRequestFocusEnabled(false);
        titleLabel.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 16));
        titleLabel.setBounds(10, 11, 384, 31);

        contentPane.add(titleLabel);

        okButton.setBounds(246, 113, 100, 47);
        contentPane.add(okButton);

        playerTextField.setBounds(104, 53, 193, 31);
        contentPane.add(playerTextField);


        cancelButton.addActionListener(arg0 -> {
        });
        cancelButton.setBounds(51, 113, 100, 47);
        contentPane.add(cancelButton);
        okButton.setVisible(true);
        okButton.addActionListener(e -> {
            if (playerTextField.getText().length() > 1) {
                game.addPlayer(playerTextField.getText());
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());
        this.setVisible(true);
    }
}
