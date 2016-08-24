package com.nvh.scrabble.model;

import java.io.Serializable;
import java.util.Objects;

import static com.nvh.scrabble.controller.ScrabbleController.game;

public class Solution implements Comparable<Solution>, Serializable {

    private static final long serialVersionUID = 1943115288116173821L;
    private Word word;
    private int points;
    private String[] information = new String[2];


    public Solution(Scrabble game, int points, Word word, String[] information) {
        this.word = word;
        this.points = points;
        this.information = information;
    }

    public Word getWord() {
        return word;
    }

    public int getPoints() {
        return points;
    }

    public String[] getInformation() {
        return information;
    }

    @Override
    public String toString() {
        String scr1, scr2;
        scr1 = information[0];

        if (Objects.equals(information[1], "[]") && game.getDrawing().length() > 6) scr2 = "Scrabble !";
        else scr2 = "";
        if (word.lenght() > 6)
            return word.word + " \t| " + "\t" + Grid.toCoordinates(word.getX(), word.getY(),
                    word.isHorizontal()) + "\t | \t" + points + "\t | " + scr1;
        return word.word + "\t \t| " + "\t" + Grid.toCoordinates(word.getX(),
                word.getY(), word.isHorizontal()) + "\t | \t" + points + "\t | " + scr1 + "\t | " + scr2;
    }

    public String getRemainingDrawing() {
        String remainingDrawing = "";
        for (int i = 0; i < this.information[1].length(); i++) {
            if (Character.isLetter(this.information[1].charAt(i)) || this.information[1].charAt(i) == '*')
                remainingDrawing += this.information[1].charAt(i);
        }
        return remainingDrawing;
    }

    public String getWordWithJokers() //renvoie le mot solution avec la lettre remplacée par un joker entre ( )
    {
        String reponse = "";
        if (Objects.equals(this.getWord().getWord(), "-------")) return "-------";
        for (int c = 0; c < this.getSequence().length(); c++) {
            if (this.getSequence().charAt(c) != '*') reponse += this.getSequence().charAt(c);
            else reponse += "(" + this.word.getWord().charAt(c) + ")";
        }
        return reponse;
    }

    String getSequence() {
        return this.information[0];
    }

    @Override
    public int compareTo(Solution o) {
        return points == o.points ?
                Integer.compare(points, o.points) :
                Integer.compare(o.points, points);
    }
}
