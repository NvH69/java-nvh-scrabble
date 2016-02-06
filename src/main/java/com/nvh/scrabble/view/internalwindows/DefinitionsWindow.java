package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Definitions;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import java.awt.*;

public class DefinitionsWindow extends JFrame {
    private static final long serialVersionUID = -3495912767328932401L;
    static JTextPane definitionText = new JTextPane();

    public DefinitionsWindow() {
        this.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 11));
        this.setBounds(0, 0, 300, 500);
        this.setTitle("Défintion");
        getContentPane().setLayout(null);

        definitionText.setBounds(0, 0, 300, 500);
        getContentPane().add(definitionText);
        definitionText.setEditable(false);
        this.setVisible(true);
    }

    public static void display(String s) {
        definitionText.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        definitionText.setText(Definitions.getDefinition(s));
    }
}
