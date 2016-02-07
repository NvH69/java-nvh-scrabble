package com.nvh.scrabble.service;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.model.Grid;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import java.util.*;
import java.util.stream.Collectors;

public class Solver extends Observable {

    public static int dictionaryIndex;
    public static List<Scrabble.Solution> solutions;

    public void solve(Scrabble game) {

        ArrayList<Character> drawingArray = new ArrayList<>();
        List<Scrabble.Solution> allSolutions = new ArrayList<>();

        Grid grid = game.getGrid();
        String drawing = game.getDrawing();
        addObserver((Observer) MainWindow.progressionFrame);
        addObserver((Observer) Launcher.gameFrame);

        MainWindow.progressionFrame.setVisible(true);

        for (int i = 0; i < drawing.length(); i++) {
            drawingArray.add(drawing.charAt(i));
        }
        game.getMainTimer().reset();
        dictionaryIndex = 0; //pour barre de progression
        solutions = new ArrayList<>();

        for (String word : com.nvh.scrabble.model.Dictionary.dictionary) {
            setChanged();
            notifyObservers(dictionaryIndex /
                    com.nvh.scrabble.model.Dictionary.dictionary.size()*100); //notifie à la barre de progression

            Scrabble.Word horizontalWordToTest, verticalWordToTest;
            StringBuffer testWord = new StringBuffer(word);
            String[] information;

            for (int x = 0; x < 15; x++)
                for (int y = 0; y < 15; y++) {
                    horizontalWordToTest = Launcher.game.new Word(word, x, y, true);
                    verticalWordToTest = Launcher.game.new Word(word, x, y, false);

                    if (y >= grid.getFilledCoordinates()[2] && y <= grid.getFilledCoordinates()[3]) {
                        information = horizontalWordToTest.isMatchingWord(drawing, grid);
                        if (information != null) {
                            allSolutions.add(game.
                                    new Solution(horizontalWordToTest.getScore(grid, information),
                                    horizontalWordToTest, information));
                        }

                    }
                    if (x >= grid.getFilledCoordinates()[0] && x <= grid.getFilledCoordinates()[1]) {
                        information = verticalWordToTest.isMatchingWord(drawing, grid);
                        if (information != null) {
                            allSolutions.add(game.
                                    new Solution(verticalWordToTest.getScore(grid, information),
                                    verticalWordToTest, information));
                        }
                    }
                }
            solutions.addAll(allSolutions.stream().collect(Collectors.toList()));
            allSolutions.clear();
            dictionaryIndex++;
        }
        notifyDone();
    }

    private void notifyDone() {
        MainWindow.progressionFrame.setVisible(true);

        Collections.sort(solutions);
        setChanged();
        notifyObservers(solutions);
        Launcher.phase++;
    }
}