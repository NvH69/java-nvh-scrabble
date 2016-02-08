package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.service.Solver;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ProgressWindow extends JFrame implements Observer {
    private static final long serialVersionUID = -596843077674913400L;
    static public JProgressBar progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, Dictionary.dictionary.size());

    public ProgressWindow() {
        this.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 11));

        this.setBounds(168, 150, 45, 620);
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        progressBar.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        progressBar.setBounds(0, 0, 45, 620);
        progressBar.setBackground(Color.black);
        contentPane.add(progressBar, BorderLayout.CENTER);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        contentPane.setVisible(true);
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof Solver)
            progressBar.setValue(Solver.dictionaryIndex);

    }
}
