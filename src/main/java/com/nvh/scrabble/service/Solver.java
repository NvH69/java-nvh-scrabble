package com.nvh.scrabble.service;

import com.nvh.scrabble.controller.ScrabbleController;
import com.nvh.scrabble.model.Grid;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import java.util.*;
import java.util.stream.Collectors;

public class Solver extends Observable {

    public static int dictionaryIndex;
    public static List<Scrabble.Solution> solutions;

    public void solve(Scrabble game) {

        List<Scrabble.Solution> allSolutions = new ArrayList<>();

        Grid grid = game.getGrid();
        String drawing = game.getDrawing();
        addObserver((Observer) MainWindow.progressionFrame);
        addObserver((Observer) ScrabbleController.gameFrame);
        addObserver((Observer) MainWindow.solutionsFrame);

        MainWindow.progressionFrame.setVisible(true);

        game.getMainTimer().reset();
        dictionaryIndex = 0; //pour barre de progression
        solutions = new ArrayList<>();

        for (String word : com.nvh.scrabble.model.Dictionary.dictionary) {
            setChanged();
            notifyObservers(dictionaryIndex /
                    com.nvh.scrabble.model.Dictionary.dictionary.size() * 100); //notifie à la barre de progression

            Scrabble.Word horizontalWordToTest, verticalWordToTest;
            String[] information;

            for (int x = 0; x < 15; x++)
                for (int y = 0; y < 15; y++) {
                    horizontalWordToTest = game.new Word(word, x, y, true);
                    verticalWordToTest = game.new Word(word, x, y, false);

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
        Collections.sort(solutions);
        setChanged();
        notifyObservers(solutions);
        game.phaseUp();
    }
}