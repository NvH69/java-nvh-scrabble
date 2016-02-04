package com.nvh.view.internaldialpanes;

import com.nvh.controller.Scrabble;
import com.nvh.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerDialPane extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JLabel lblTitre = new JLabel("Entrer le pseudo du nouveau joueur");
    JButton btnOk = new JButton("OK");
    JButton btnAnnul = new JButton("Annuler");
    JFormattedTextField joueurTextField = new JFormattedTextField();

    public PlayerDialPane(Scrabble partie) {
        setModal(true);
        setBounds(600, 460, 410, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitre.setRequestFocusEnabled(false);
        lblTitre.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 16));
        lblTitre.setBounds(10, 11, 384, 31);

        contentPane.add(lblTitre);

        btnOk.setBounds(246, 113, 100, 47);
        contentPane.add(btnOk);

        joueurTextField.setBounds(104, 53, 193, 31);
        contentPane.add(joueurTextField);


        btnAnnul.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        btnAnnul.setBounds(51, 113, 100, 47);
        contentPane.add(btnAnnul);
        btnOk.setVisible(true);
        btnOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (joueurTextField.getText().length() > 1) {
                    partie.addJoueur(joueurTextField.getText());
                    dispose();
                }
            }
        });

        btnAnnul.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.setVisible(true);
    }
}
