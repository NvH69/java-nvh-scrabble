package com.nvh.scrabble.model;

import com.nvh.scrabble.controller.ScrabbleController;
import com.nvh.scrabble.model.Scrabble.Solution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class Player extends Observable implements Serializable {
    private static final long serialVersionUID = 408209690201416162L;
    private String name;
    private int points;
    private ArrayList<Solution> words;

    public Player(String name, int points, ArrayList<Solution> words) {
        this.name = name;
        this.points = points;
        this.words = words;
        this.addObserver(ScrabbleController.game);
    }


    public String getName() {
        return this.name;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints(int p) {
        this.points += p;
        setChanged();
        notifyObservers();
    }

    public Solution getWord(int i) {
        if (i <= this.getWordsCount())
            return this.words.get(i);
        else return null;
    }


    public void setWord(Solution s) {
        this.words.add(s);
        this.points += s.getPoints();
        setChanged();
        notifyObservers();
    }

    public int getWordsCount() {
        return this.words.size();
    }
}