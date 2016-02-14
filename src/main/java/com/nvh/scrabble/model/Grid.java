package com.nvh.scrabble.model;

import com.nvh.scrabble.model.Scrabble.Solution;
import com.nvh.scrabble.model.Scrabble.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;


public class Grid extends Observable implements Serializable {
    public static final String xAxisLetters = "ABCDEFGHIJKLMNO";
    private static final long serialVersionUID = 6143259766902247495L;
    private ArrayList<ArrayList<Character>> coordinates;
    private ArrayList<ArrayList<Integer>> bonus;
    private int[] filledCoordinates = new int[]{7, 7, 7, 7};
    private String listOfWords = "";

    public Grid() {
        this.coordinates = new ArrayList<>(15);
        this.bonus = new ArrayList<>(15);
    }

    public Grid(ArrayList<ArrayList<Character>> coordinates,
                ArrayList<ArrayList<Integer>> bonus, String presentSequence) {
        if (coordinates == null)
            this.coordinates = resetGrid();
        else
            this.coordinates = coordinates;

        if (bonus == null)
            this.bonus = resetBonus();
        else
            this.bonus = bonus;
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

    private ArrayList<ArrayList<Character>> resetGrid() {
        ArrayList<ArrayList<Character>> newGrid = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i != 7)
                newGrid.add(new ArrayList<>
                        (Arrays.asList(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ')));
            else
                newGrid.add(new ArrayList<>
                        (Arrays.asList(' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ')));
        }
        return newGrid;
    }

    private ArrayList<ArrayList<Integer>> resetBonus() {
        ArrayList<ArrayList<Integer>> newBonus = new ArrayList<>();
        newBonus.add(new ArrayList<>
                (Arrays.asList(30, 1, 1, 2, 1, 1, 1, 30, 1, 1, 1, 2, 1, 1, 30)));
        newBonus.add(new ArrayList<>
                (Arrays.asList(1, 20, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 20, 1)));
        newBonus.add(new ArrayList<>
                (Arrays.asList(1, 1, 20, 1, 1, 1, 2, 1, 2, 1, 1, 1, 20, 1, 1)));
        newBonus.add(new ArrayList<>
                (Arrays.asList(2, 1, 1, 20, 1, 1, 1, 2, 1, 1, 1, 20, 1, 1, 2)));
        newBonus.add(new ArrayList<>
                (Arrays.asList(1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1)));
        newBonus.add(new ArrayList<>
                (Arrays.asList(1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1)));
        newBonus.add(new ArrayList<>
                (Arrays.asList(1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1)));
        newBonus.add(new ArrayList<>
                (Arrays.asList(30, 1, 1, 2, 1, 1, 1, 20, 1, 1, 1, 2, 1, 1, 30)));
        for (int i = 6; i >= 0; i--) {
            newBonus.add(newBonus.get(i));
        }
        return newBonus;
    }

    public ArrayList<ArrayList<Character>> getCoordinates() {
        return this.coordinates;
    }

    public Character getCoordinates(int x, int y) {
        return this.coordinates.get(x).get(y);
    }

    public char get(int x, int y) {
        return this.coordinates.get(x).get(y);
    }

    public void set(int x, int y, char c) {
        this.coordinates.get(x).set(y, c);
    }

    public ArrayList<ArrayList<Integer>> getBonus() {
        return this.bonus;
    }

    public Integer getBonus(int x, int y) {
        return this.bonus.get(x).get(y);
    }

    public void setBonus(int x, int y, int pts) {
        this.bonus.get(x).set(y, pts);
    }

    public String getWordList() {
        return this.listOfWords;
    }

    public void setWordList(String m) {
        this.listOfWords += "_" + m + "_";
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
        placedWords.stream().filter
                (tm -> !this.getWordList().contains("_" + tm + "_") && tm.length() > 1).forEach(this::setWordList);
        this.setChanged();
        this.notifyObservers(solution); //notification nouvelle solution à game
        this.fittings(); //mise à jour de la grille avec les nouveaux fittings

    }

    public void setWord(Word word) {// place un word "test" : pour placer véritablement un word : setSolution
        int x = word.getX();
        int y = word.getY();
        //écriture du word dans la grille (H et V)
        if (word.isHorizontal()) {
            for (int i = 0; i < word.lenght(); i++) {
                this.set(x + i, y, word.charAt(i));    //remplissage
            }
        } else {
            for (int i = 0; i < word.lenght(); i++) {
                this.set(x, y + i, word.charAt(i));    //remplissage
            }
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
                            if (Character.isLetter((coordinates.get(x + deltaX).get(y + deltaY)))
                                    && (deltaX == 0 || deltaY == 0) && (coordinates.get(x).get(y) == ' '))
                                this.coordinates.get(x).set(y, '#');

        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++)
                if (this.coordinates.get(x).get(y) == ('#')) {
                    if (x < getFilledCoordinates()[0]) getFilledCoordinates()[0] = x;
                    if (x > getFilledCoordinates()[1]) getFilledCoordinates()[1] = x;
                    if (y < getFilledCoordinates()[2]) getFilledCoordinates()[2] = y;
                    if (y > getFilledCoordinates()[3]) getFilledCoordinates()[3] = y;
                }
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
                Character h = this.coordinates.get(j).get(i);
                Character v = this.coordinates.get(i).get(j);
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

    public Grid cloneGrid() {
        Grid cloned = new Grid();
        cloned.coordinates.addAll
                (this.coordinates.stream().map(x -> (ArrayList<Character>) x.clone()).collect(Collectors.toList()));
        cloned.bonus.addAll
                (this.bonus.stream().map(x -> (ArrayList<Integer>) x.clone()).collect(Collectors.toList()));

        return cloned;
    }
}
