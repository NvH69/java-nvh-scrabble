package com.nvh.scrabble.model;

import com.nvh.scrabble.model.Scrabble.Solution;
import com.nvh.scrabble.Launcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Player extends Observable implements Serializable {
    private static final long serialVersionUID = 408209690201416162L;
    private String nom;
    private int points;
    private ArrayList<Solution> coupJoues;


    public Player(String nom, int points, ArrayList<Solution> coupJoues) {
        this.nom = nom;
        this.points = points;
        this.coupJoues = coupJoues;
        this.addObserver((Observer) Launcher.partie);
    }


    public String getNom() {
        return this.nom;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }


    public int getPoints() {
        return this.points;
    }


    public void addPoints(int p) {
        this.points += p;
        setChanged();
        notifyObservers();
    }


    public Solution getCoupJoue(int i) {
        if (i <= this.getNbCoupsJoues())
            return this.coupJoues.get(i);
        else return null;
    }


    public void setCoupJoue(Solution s) {
        this.coupJoues.add(s);
        this.points += s.getPoints();
        setChanged();
        notifyObservers();
    }

    public int getNbCoupsJoues() {
        return this.coupJoues.size();
    }

}