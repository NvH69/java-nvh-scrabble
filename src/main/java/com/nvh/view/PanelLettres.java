package com.nvh.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class PanelLettres extends JTextPane implements Observer {

    public PanelLettres() {
        setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
        setBackground(Color.WHITE);
        setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 12));
        setBounds(675, 225, 190, 165);
        setEditable(false);
    }

    @Override
    public void update(Observable arg0, Object obj) {

        if (obj instanceof String) {
            setText("Lettres restantes : " + Main.partie.getLettres().size() + "\n" + Main.partie.getNombreLettres());
            setFont(new Font(FenetrePrincipale.mainFont, Font.PLAIN, 12));
        }
    }
}
