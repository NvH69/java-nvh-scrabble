package com.nvh.controller;

import com.nvh.controller.Scrabble.Mot;
import com.nvh.controller.Scrabble.Solution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class Grille extends Observable implements Serializable {
    private static final long serialVersionUID = 6143259766902247495L;
    private char[][] coor;
    private int[][] bonus;
    private char[][] undo;
    private int[] optiRaccords = new int[]{7, 7, 7, 7};
    private String sequencePresente;
    private String listeMots = new String("");

    public static final String coorLettres = "ABCDEFGHIJKLMNO";
    public static final char[][] resetGrille = new char[][]{
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


    public Grille(char[][] coor, int[][] bonus) {
        if (coor == null)
            coor = resetGrille;

        this.coor = coor;

        if (bonus == null)
            this.bonus = resetBonus;
        else
            this.bonus = bonus;


    }

    public char[][] getCoor() {
        return this.coor;
    }


    public char get(int x, int y) {
        return this.coor[x][y];
    }

    public void set(int x, int y, char c) {
        this.coor[x][y] = c;
    }

    public int[][] getBonus() {
        return this.bonus;
    }

    public void setBonus(int x, int y, int pts) {
        this.bonus[x][y] = pts;
    }

    public String getListeMots() {
        return this.listeMots;
    }

    public void setListeMot(String m) {
        this.listeMots += "_" + m + "_";
    }

    public String getSequencePresente() {
        return sequencePresente;
    }

    public void setSequencePresente(String sequencePresente) {
        this.sequencePresente = sequencePresente;
    }

    public int[] getOptiRaccords() {
        return optiRaccords;
    }

    public void setOptiRaccords(int[] optiRaccords) {
        this.optiRaccords = optiRaccords;
    }

    public Grille clone() {
        Grille rep = new Grille(null, null);
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++)
                rep.set(x, y, this.get(x, y));

        return rep;
    }

    public void setSolution(Scrabble partie, Solution s) //place le mot sur la grille
    {
        Mot m = s.getM();
        String[] info = s.getRetour();
        int x = m.getX();
        int y = m.getY();
        if (m.isHorizontal()) {
            for (int i = 0; i < m.longueur(); i++) {
                this.set(x + i, y, m.charAt(i));    //remplissage
                //si un joker est utilis�, les points de la case sont annul�s, la case est identifi�e joker
                if (info[0].charAt(i) == '*') this.setBonus(x + i, y, 0);
                else this.setBonus(x + i, y, 1);        //mat�rialise les cases bonus occup�es
            }
        } else {
            for (int i = 0; i < m.longueur(); i++) {
                this.set(x, y + i, m.charAt(i));    //remplissage
                //si un joker est utilis�, les points de la case sont annul�s, la case est identifi�e joker
                if (info[0].charAt(i) == '*') this.setBonus(x, y + i, 0);
                else this.setBonus(x, y + i, 1);        //mat�rialise les cases bonus occup�es
            }
        }
        // mise � jour des raccords
        //mise � jour des mots form�s

        List<String> tousMots = this.listeMotsPlaces(); // liste de tous les mots  + nouveaux
        //String motsPresents = this.motsPlaces();

        for (String tm : tousMots) // parcourir tous les nouveaux mots
        {
            if (!this.getListeMots().contains("_" + tm + "_") && tm.length() > 1)
                this.setListeMot(tm);

            // ajouter le nouveau mot � la liste des mots de la partie
        }
        this.setChanged();
        this.notifyObservers(s); //notification nouvelle solution � partie
        this.raccords(); //mise � jour de la grille avec les nouveaux raccords

    }

    public void setMot(Mot m) {// place un mot "test" : pour placer v�ritablement un mot : setSolution
        int x = m.getX();
        int y = m.getY();
        undo = new char[15][15];
        //�criture du mot dans la grille (H et V)
        if (m.isHorizontal()) {
            for (int i = 0; i < m.longueur(); i++) {
                undo[x + i][y] = this.get(x + i, y);    //pour retour en arri�re (utile pour le test)
                this.set(x + i, y, m.charAt(i));    //remplissage
            }
        } else {
            for (int i = 0; i < m.longueur(); i++) {
                undo[x][y + i] = this.get(x, y + i);    //pour retour en arri�re (utile pour le test)
                this.set(x, y + i, m.charAt(i));    //remplissage
            }
        }
    }


    public void deleteMot(Mot m) {
        int x = m.getX();
        int y = m.getY();
        if (m.isHorizontal()) {
            for (int i = 0; i < m.longueur(); i++) {
                this.coor[x + i][y] = undo[x + i][y];
            }
        } else {
            for (int i = 0; i < m.longueur(); i++) {
                this.coor[x][y + i] = undo[x][y + i];
            }
        }

    }

    public String motsPlaces()  //renvoie les mots plac�s s�par�s par _
    {
        String reponseH = new String("");
        String reponseV = new String("");

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (this.coor[j][i] == ' ' || this.coor[j][i] == '#') reponseH += '_';
                else reponseH += this.coor[j][i];
                if (this.coor[i][j] == ' ' || this.coor[i][j] == '#') reponseV += '_';
                else reponseV += this.coor[i][j];
            }
            reponseH += " ";
            reponseV += " ";
        }

        return reponseH + " " + reponseV;
    }

    public void raccords() //place # sur toutes les cases de raccords
    {
        setOptiRaccords(new int[]{14, 0, 14, 0});
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++)
                for (int deltaX = -1; deltaX < 2; deltaX++)
                    for (int deltaY = -1; deltaY < 2; deltaY++)
                        if (x + deltaX >= 0 && y + deltaY >= 0 && x + deltaX <= 14 && y + deltaY <= 14)
                            if (Character.isLetter((coor[x + deltaX][y + deltaY]))
                                    && (deltaX == 0 || deltaY == 0) && (coor[x][y] == ' '))
                                this.coor[x][y] = '#';

        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++)
                if (this.coor[x][y] == '#') {
                    if (x < getOptiRaccords()[0]) getOptiRaccords()[0] = x;
                    if (x > getOptiRaccords()[1]) getOptiRaccords()[1] = x;
                    if (y < getOptiRaccords()[2]) getOptiRaccords()[2] = y;
                    if (y > getOptiRaccords()[3]) getOptiRaccords()[3] = y;
                }
    }


    public static String toCoor(int x, int y, boolean h) {//renvoie les coordonn�es de type Scrabble
        String reponse = "";
        if (h) {
            reponse += coorLettres.charAt(y);
            reponse += x + 1;
        } else {
            reponse += x + 1;
            reponse += coorLettres.charAt(y);
        }

        return reponse;
    }

    public List<String> listeMotsPlaces() {//renvoie la liste de tous les mots pr�sents dans une grille
        List<String> reponse = new ArrayList<String>();

        String reponseH = new String("");
        String reponseV = new String("");

        for (int i = 0; i < 15; i++) {
            if (reponseH.length() > 1) reponse.add(reponseH);
            if (reponseV.length() > 1) reponse.add(reponseV);
            reponseH = "";
            reponseV = "";
            for (int j = 0; j < 15; j++) {
                Character h = this.coor[j][i];
                Character v = this.coor[i][j];
                if (Character.isLetter(h)) reponseH += h;
                else if (reponseH.length() > 1 && !reponse.contains(reponseH)) {
                    reponse.add(reponseH);
                    reponseH = "";
                } else reponseH = "";

                if (Character.isLetter(v)) reponseV += v;
                else if (reponseV.length() > 1 && !reponse.contains(reponseV)) {
                    reponse.add(reponseV);
                    reponseV = "";
                } else reponseV = "";
            }
        }
        if (reponseH.length() > 1) reponse.add(reponseH);
        if (reponseV.length() > 1) reponse.add(reponseV);
        return reponse;
    }

}	
