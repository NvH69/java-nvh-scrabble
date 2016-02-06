package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.service.Solve;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ProgressWindow extends JFrame implements Observer {
    private static final long serialVersionUID = -596843077674913400L;
    static public JProgressBar progressBar = new JProgressBar();

    public ProgressWindow() {
        this.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 11));

        this.setTitle("Recherche...");
        this.setBounds(600, 460, 276, 64);
        this.setAlwaysOnTop(true);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        progressBar.setMinimum(0);
        progressBar.setMaximum(Dictionary.dictionary.length - 30000);

        progressBar.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        progressBar.setBounds(0, 0, 270, 35);
        progressBar.setStringPainted(true);
        contentPane.add(progressBar, BorderLayout.CENTER);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        contentPane.setVisible(true);
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof Solve)
            progressBar.setValue(Solve.dictionaryIndex);

    }
}
