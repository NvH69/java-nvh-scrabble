package com.nvh.view;

import com.nvh.model.Definitions;

import javax.swing.*;
import java.awt.*;

public class FenetreDefinitions extends JFrame {
    private static final long serialVersionUID = -3495912767328932401L;
    static JTextPane textDef = new JTextPane();

    public FenetreDefinitions() {
        this.setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 11));
        this.setBounds(0, 0, 300, 500);
        this.setTitle("D�fintion");
        getContentPane().setLayout(null);

        textDef.setBounds(0, 0, 300, 500);
        getContentPane().add(textDef);
        textDef.setEditable(false);
        this.setVisible(true);
    }

    public static void affiche(String s) {
        textDef.setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 12));
        textDef.setText(Definitions.getDefinition(s));
    }
}
