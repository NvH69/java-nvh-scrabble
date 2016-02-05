package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class LettersPanel extends JTextPane implements Observer {

    public LettersPanel() {
        setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
        setBackground(Color.WHITE);
        setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        setBounds(675, 225, 190, 165);
        setEditable(false);
    }

    @Override
    public void update(Observable arg0, Object obj) {

        if (obj instanceof String) {
            setText("Lettres restantes : " + Launcher.game.getLetters().size() + "\n" + Launcher.game.getCountOfRemainingLetters());
            setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        }
    }
}
