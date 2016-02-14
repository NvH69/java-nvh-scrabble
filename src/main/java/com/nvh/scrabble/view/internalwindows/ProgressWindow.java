package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.service.Solver;
import com.nvh.scrabble.view.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ProgressWindow extends JInternalFrame implements Observer {
    private static final long serialVersionUID = -596843077674913400L;
    static public JProgressBar progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, Dictionary.dictionary.size());

    public ProgressWindow() {
        this.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 11));

        this.setBounds(15, 30, 60, 640);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        progressBar.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 12));
        progressBar.setBounds(0, 0, 45, 640);
        contentPane.add(progressBar, BorderLayout.CENTER);
        setResizable(false);
        contentPane.setVisible(true);
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof Solver)
            progressBar.setValue(Solver.dictionaryIndex);

    }
}
