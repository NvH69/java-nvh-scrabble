package com.nvh.scrabble.model;

import com.nvh.scrabble.model.Scrabble.Solution;
import com.nvh.scrabble.model.Scrabble.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class Grid extends Observable implements Serializable {
    private static final long serialVersionUID = 6143259766902247495L;
    private char[][] coordinates;
    private int[][] bonus;
    private char[][] undo;
    private int[] filledCoordinates = new int[]{7, 7, 7, 7};
    private String presentSequence;
    private String listOfWords = "";

    public static final String xAxisLetters = "ABCDEFGHIJKLMNO";
    public static final char[][] resetGrid = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}};

    private static final int[][] resetBonus = new int[][]{
            {30, 1, 1, 2, 1, 1, 1, 30, 1, 1, 1, 2, 1, 1, 30},
            {1, 20, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 20, 1},
            {1, 1, 20, 1, 1, 1, 2, 1, 2, 1, 1, 1, 20, 1, 1},
            {2, 1, 1, 20, 1, 1, 1, 2, 1, 1, 1, 20, 1, 1, 2},
            {1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1},
            {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
            {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
            {30, 1, 1, 2, 1, 1, 1, 20, 1, 1, 1, 2, 1, 1, 30},
            {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
            {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
            {1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1},
            {2, 1, 1, 20, 1, 1, 1, 2, 1, 1, 1, 20, 1, 1, 2},
            {1, 1, 20, 1, 1, 1, 2, 1, 2, 1, 1, 1, 20, 1, 1},
            {1, 20, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 20, 1},
            {30, 1, 1, 2, 1, 1, 1, 30, 1, 1, 1, 2, 1, 1, 30}};


    public Grid(char[][] coordinates, int[][] bonus, String presentSequence) {
        this.presentSequence = presentSequence;
        if (coordinates == null)
            coordinates = resetGrid;

        this.coordinates = coordinates;

        if (bonus == null)
            this.bonus = resetBonus;
        else
            this.bonus = bonus;


    }

    public char[][] getCoordinates() {
        return this.coordinates;
    }


    public char get(int x, int y) {
        return this.coordinates[x][y];
    }

    public void set(int x, int y, char c) {
        this.coordinates[x][y] = c;
    }

    public int[][] getBonus() {
        return this.bonus;
    }

    public void setBonus(int x, int y, int pts) {
        this.bonus[x][y] = pts;
    }

    public String getListOfWords() {
        return this.listOfWords;
    }

    public void setWordList(String m) {
        this.listOfWords += "_" + m + "_";
    }

    public String getPresentSequence() {
        return presentSequence;
    }

    public int[] getFilledCoordinates() {
        return filledCoordinates;
    }

    public void setFilledCoordinates(int[] filledCoordinates) {
        this.filledCoordinates = filledCoordinates;
    }

    public void setSolution(Solution solution) //place le mot sur la grille
    {
        Word word = solution.getWord();
        String[] information = solution.getInformation();
        int x = word.getX();
        int y = word.getY();
        if (word.isHorizontal()) {
            for (int i = 0; i < word.lenght(); i++) {
                this.set(x + i, y, word.charAt(i));    //remplissage
                //si un joker est utilisé, les points de la case sont annulés, la case est identifiée joker
                if (information[0].charAt(i) == '*') this.setBonus(x + i, y, 0);
                else this.setBonus(x + i, y, 1);        //matérialise les cases bonus occupées
            }
        } else {
            for (int i = 0; i < word.lenght(); i++) {
                this.set(x, y + i, word.charAt(i));    //remplissage
                //si un joker est utilisé, les points de la case sont annulés, la case est identifiée joker
                if (information[0].charAt(i) == '*') this.setBonus(x, y + i, 0);
                else this.setBonus(x, y + i, 1);        //matérialise les cases bonus occupées
            }
        }
        // mise à jour des fittings
        //mise à jour des mots formés

        List<String> placedWords = this.placedWords(); // liste de tous les mots  + nouveaux
        //String motsPresents = this.allCells();

        // ajouter le nouveau mot à la liste des mots de la game
        placedWords.stream().filter(tm -> !this.getListOfWords().contains("_" + tm + "_") && tm.length() > 1).forEach(this::setWordList);
        this.setChanged();
        this.notifyObservers(solution); //notification nouvelle solution à game
        this.fittings(); //mise à jour de la grille avec les nouveaux fittings

    }

    public void setWord(Word word) {// place un word "test" : pour placer véritablement un word : setSolution
        int x = word.getX();
        int y = word.getY();
        undo = new char[15][15];
        //écriture du word dans la grille (H et V)
        if (word.isHorizontal()) {
            for (int i = 0; i < word.lenght(); i++) {
                undo[x + i][y] = this.get(x + i, y);    //pour retour en arrière (utile pour le test)
                this.set(x + i, y, word.charAt(i));    //remplissage
            }
        } else {
            for (int i = 0; i < word.lenght(); i++) {
                undo[x][y + i] = this.get(x, y + i);    //pour retour en arrière (utile pour le test)
                this.set(x, y + i, word.charAt(i));    //remplissage
            }
        }
    }


    public void deleteWord(Word word) {
        int x = word.getX();
        int y = word.getY();
        if (word.isHorizontal()) {
            for (int i = 0; i < word.lenght(); i++) {
                this.coordinates[x + i][y] = undo[x + i][y];
            }
        } else {
            System.arraycopy(undo[x], y, this.coordinates[x], y, word.lenght());
        }
    }

    public void fittings() //place # sur toutes les cases de fittings
    {
        setFilledCoordinates(new int[]{14, 0, 14, 0});
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++)
                for (int deltaX = -1; deltaX < 2; deltaX++)
                    for (int deltaY = -1; deltaY < 2; deltaY++)
                        if (x + deltaX >= 0 && y + deltaY >= 0 && x + deltaX <= 14 && y + deltaY <= 14)
                            if (Character.isLetter((coordinates[x + deltaX][y + deltaY]))
                                    && (deltaX == 0 || deltaY == 0) && (coordinates[x][y] == ' '))
                                this.coordinates[x][y] = '#';

        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++)
                if (this.coordinates[x][y] == '#') {
                    if (x < getFilledCoordinates()[0]) getFilledCoordinates()[0] = x;
                    if (x > getFilledCoordinates()[1]) getFilledCoordinates()[1] = x;
                    if (y < getFilledCoordinates()[2]) getFilledCoordinates()[2] = y;
                    if (y > getFilledCoordinates()[3]) getFilledCoordinates()[3] = y;
                }
    }


    public static String toCoordinates(int x, int y, boolean h) {//renvoie les coordonnées de type Scrabble
        String coordinates = "";
        if (h) {
            coordinates += xAxisLetters.charAt(y);
            coordinates += x + 1;
        } else {
            coordinates += x + 1;
            coordinates += xAxisLetters.charAt(y);
        }

        return coordinates;
    }

    public List<String> placedWords() {//renvoie la liste de tous les mots présents dans une grille
        List<String> placedWords = new ArrayList<>();

        String horizontalAnswers = "";
        String verticalAnswers = "";

        for (int i = 0; i < 15; i++) {
            if (horizontalAnswers.length() > 1) placedWords.add(horizontalAnswers);
            if (verticalAnswers.length() > 1) placedWords.add(verticalAnswers);
            horizontalAnswers = "";
            verticalAnswers = "";
            for (int j = 0; j < 15; j++) {
                Character h = this.coordinates[j][i];
                Character v = this.coordinates[i][j];
                if (Character.isLetter(h)) horizontalAnswers += h;
                else if (horizontalAnswers.length() > 1 && !placedWords.contains(horizontalAnswers)) {
                    placedWords.add(horizontalAnswers);
                    horizontalAnswers = "";
                } else horizontalAnswers = "";

                if (Character.isLetter(v)) verticalAnswers += v;
                else if (verticalAnswers.length() > 1 && !placedWords.contains(verticalAnswers)) {
                    placedWords.add(verticalAnswers);
                    verticalAnswers = "";
                } else verticalAnswers = "";
            }
        }
        if (horizontalAnswers.length() > 1) placedWords.add(horizontalAnswers);
        if (verticalAnswers.length() > 1) placedWords.add(verticalAnswers);
        return placedWords;
    }

}
