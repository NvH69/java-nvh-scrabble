package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManualDrawingPane extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JLabel lblEntrerNouvellesLettres = new JLabel("Entrer nouvelles lettres (+) ou nouveau tirage (-)");
    JTextField tf;
    JButton btnNewButton = new JButton("OK");

    public ManualDrawingPane(Scrabble partie) {
        setModal(true);
        setBounds(600, 460, 410, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        tf = new JTextField("Tirage suivant");
        tf.setToolTipText("Entrer tirage ici");

        tf.setText("");
        tf.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 28));
        tf.setBounds(123, 61, 168, 41);
        contentPane.add(tf);
        tf.setVisible(true);

        lblEntrerNouvellesLettres.setHorizontalAlignment(SwingConstants.CENTER);
        lblEntrerNouvellesLettres.setRequestFocusEnabled(false);
        lblEntrerNouvellesLettres.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        lblEntrerNouvellesLettres.setBounds(10, 11, 384, 31);

        contentPane.add(lblEntrerNouvellesLettres);

        JToggleButton toggleType = new JToggleButton("+");
        toggleType.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 24));
        toggleType.setBounds(65, 61, 48, 41);
        contentPane.add(toggleType);
        toggleType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (toggleType.isSelected()) toggleType.setText("-");
                else toggleType.setText("+");
                tf.grabFocus();
            }
        });

        btnNewButton.setBounds(155, 113, 100, 47);
        contentPane.add(btnNewButton);
        btnNewButton.setVisible(true);
        btnNewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String toggle;
                if (toggleType.isSelected()) toggle = "-";
                else toggle = "+";

                if (partie.tirageManu(toggle + tf.getText()) == "")
                    dispose();

                else lblEntrerNouvellesLettres.setText(partie.tirageManu(tf.getText()));
            }
        });
        this.setVisible(true);
    }
}
