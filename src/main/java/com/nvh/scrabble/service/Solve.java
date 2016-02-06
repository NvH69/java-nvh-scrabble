package com.nvh.scrabble.service;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.model.Grid;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import java.util.*;
import java.util.stream.Collectors;

public class Solve extends Observable {

    public static List<Scrabble.Solution> solutions = new ArrayList<>();
    static ArrayList<Character> drawingArray = new ArrayList<>();
    static List<Scrabble.Solution> allSolutions = new ArrayList<>();
    public static int dictionaryIndex;

    public Solve(Scrabble partie) {
        Grid grid = partie.getGrid();
        String drawing = partie.getDrawing();
        addObserver((Observer) MainWindow.solutionsFrame);
        addObserver((Observer) MainWindow.progressionFrame);
        addObserver((Observer) Launcher.gameFrame);

        MainWindow.progressionFrame.setVisible(true);

        for (int i = 0; i < drawing.length(); i++) {
            drawingArray.add(drawing.charAt(i));
        }
        partie.getMainTimer().reset();
        dictionaryIndex = 0;
        solutions = new ArrayList<>();

        for (String word : com.nvh.scrabble.model.Dictionary.dictionary) {
            {
                if (word == null || word.length() < 2) break;
                setChanged();
                notifyObservers(dictionaryIndex / 3900);

                Scrabble.Word horizontalWordToTest, verticalWordToTest;
                StringBuffer testWord = new StringBuffer(word);
                String[] information;
                //1er filtre : trouver séquence manquante sur grille : si NON : pas possible
                if (!drawingArray.contains('*') && partie.getGrid().getPresentSequence() != null) {
                    for (int i = 0; i < word.length(); i++) // pour chaque lettre du mot à tester
                    {
                        for (int j = 0; j < drawingArray.size(); j++) {
                            if (word.charAt(i) == drawingArray.get(j)) {
                                testWord.replace(i, i + 1, "_");
                                drawingArray.set(j, '$');
                                break;
                            }  //lettre ok
                        }
                    }
                    if (!partie.getGrid().getPresentSequence().contains(testWord)) break;
                }
                //2nd filtre : exhaustif
                for (int x = 0; x < 15; x++)
                    for (int y = 0; y < 15; y++) {
                        horizontalWordToTest = Launcher.game.new Word(word, x, y, true);
                        verticalWordToTest = Launcher.game.new Word(word, x, y, false);

                        if (y >= grid.getFilledCoordinates()[2] && y <= grid.getFilledCoordinates()[3]) {
                            information = horizontalWordToTest.isMatchingWord(drawing, grid);
                            if (information != null)
                                allSolutions.add(partie.
                                        new Solution(horizontalWordToTest.getScore(grid, information), horizontalWordToTest, information));

                        }
                        if (x >= grid.getFilledCoordinates()[0] && x <= grid.getFilledCoordinates()[1]) {
                            information = verticalWordToTest.isMatchingWord(drawing, grid);
                            if (information != null)
                                allSolutions.add(partie.
                                        new Solution(verticalWordToTest.getScore(grid, information), verticalWordToTest, information));
                        }
                    }
            }
            if (allSolutions != null)
                solutions.addAll(allSolutions.stream().collect(Collectors.toList()));
            allSolutions.clear();
            dictionaryIndex++;
        }

        MainWindow.progressionFrame.setVisible(true);

        Collections.sort(solutions);
        setChanged();
        notifyObservers(solutions);
    }
}