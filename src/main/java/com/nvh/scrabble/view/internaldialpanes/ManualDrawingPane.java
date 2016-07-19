package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class ManualDrawingPane extends JDialog {
    private static final long serialVersionUID = 1L;
    private JLabel newLettersLabel = new JLabel("Entrer nouvelles letters (+) ou nouveau tirage (-)");
    private JTextField textField;

    public ManualDrawingPane(Scrabble game) {
        setModal(true);
        setBounds(600, 460, 410, 200);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        textField = new JTextField("Tirage suivant");
        textField.setToolTipText("Entrer tirage ici");

        textField.setText("");
        textField.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 28));
        textField.setBounds(123, 61, 168, 41);
        contentPane.add(textField);
        textField.setVisible(true);

        newLettersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newLettersLabel.setRequestFocusEnabled(false);
        newLettersLabel.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        newLettersLabel.setBounds(10, 11, 384, 31);

        contentPane.add(newLettersLabel);

        JToggleButton toggleType = new JToggleButton("+");
        toggleType.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 24));
        toggleType.setBounds(65, 61, 48, 41);
        contentPane.add(toggleType);
        toggleType.addActionListener(e -> {
            if (toggleType.isSelected()) toggleType.setText("-");
            else toggleType.setText("+");
            textField.grabFocus();
        });

        JButton okButton = new JButton("OK");
        okButton.setBounds(155, 113, 100, 47);
        contentPane.add(okButton);
        okButton.setVisible(true);
        okButton.addActionListener(e -> {
            String toggle;
            if (toggleType.isSelected()) toggle = "-";
            else toggle = "+";

            if (Objects.equals(game.manualDrawing(toggle + textField.getText()), ""))
                dispose();

            else newLettersLabel.setText(game.manualDrawing(textField.getText()));
        });
        this.setVisible(true);
    }
}
