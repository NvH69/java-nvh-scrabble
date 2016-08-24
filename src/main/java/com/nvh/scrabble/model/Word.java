package com.nvh.scrabble.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nvh.scrabble.model.Scrabble.alphabet;
import static com.nvh.scrabble.model.Scrabble.letterValues;

public class Word implements Serializable {

    private static final long serialVersionUID = 6044242150909210877L;
    String word;
    private int x, y;
    private boolean h;

    public Word(String word, int x, int y, boolean h) {
        this.word = word;
        this.x = x;
        this.y = y;
        this.h = h;
    }

    public Word(String word, String coordinates)   //construteur avec "vraies" coordonnées type Scrabble
    {
        this.word = word;
        Character X = coordinates.charAt(0);
        Character Y = coordinates.charAt(coordinates.length() - 1);

        if (Character.isLetter(X)) {
            this.h = true;
            this.y = alphabet.indexOf(X);
            this.x = Integer.parseInt(onlyDigits(coordinates)) - 1;
        } else {
            this.h = false;
            this.x = Integer.parseInt(onlyDigits(coordinates)) - 1;
            this.y = alphabet.indexOf(Y);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getWord() {
        return word;
    }

    private int letterValue(char c) {
        return letterValues[alphabet.indexOf(c)];
    }


    public boolean isHorizontal() {
        return this.h;
    }

    public String[] isMatchingWord(String drawing, Grid grid)
    // Teste un mot placé : renvoie les lettres utilisées (index 0) et le tirage restant (index1)
    // OU null si impossible
    {
        String word = this.word;

        int x = this.x;
        int y = this.y;
        int lenght = this.lenght();
        int lettersCount = 0;
        boolean isFitting = false;
        String[] information = new String[2];
        StringBuilder wordToTest = new StringBuilder(word);
        ArrayList<Character> drawingArray = new ArrayList<>();
        for (int i = 0; i < drawing.length(); i++) {
            drawingArray.add(drawing.charAt(i));
        }

        if (this.h) {//si le mot dépasse, s'il est suivi ou précédé d'une lettre déjà placée, on renvoie FAUX
            if ((x + lenght) > 15) return null;
            if ((x + lenght) < 15)
                if (grid.getCoordinates(x + lenght, y) != ' ' && grid.getCoordinates(x + lenght, y) != '#')
                    return null;
            if (x > 0)
                if (grid.getCoordinates(x - 1, y) != ' ' && grid.getCoordinates(x - 1, y) != '#') return null;

            for (int i = 0; i < lenght; i++) {
                if (grid.getCoordinates(x + i, y) == '#') {
                    isFitting = true;
                    break;
                }
            }
            if (!isFitting) return null; //s'il n'existe pas de point d'ancrage, on renvoie FAUX

            for (int i = 0; i < lenght; i++) {
                if (grid.getCoordinates(x + i, y) == word.charAt(i)) lettersCount++;
                else if (grid.getCoordinates(x + i, y) != ' ' && grid.getCoordinates(x + i, y) != '#')
                    return null;
                else if (drawingArray.contains(word.charAt(i))) {
                    lettersCount++;
                    int index = 0;
                    for (Character c : drawingArray) {
                        if (c == word.charAt(i)) {
                            drawingArray.remove(index);
                            break;
                        }
                        index++;
                    }
                } else if (drawingArray.contains('*')) {
                    lettersCount++;
                    int index = 0;
                    wordToTest.setCharAt(i, '*');
                    //insertion choix de la position du joker
                    //si le joker est placé sur case bonus ET le tirage
                    //contient déjà la lettre cherchée
                    if (grid.getBonus(x + i, y) > 1 && drawing.contains(Character.toString(word.charAt(i)))) {
                        //échange de position entre le joker et la lettre déjà placée
                        //si le bonus est inférieur
                        for (int j = 0; j < i; j++)
                            if (word.charAt(j) == word.charAt(i) && grid.getBonus(x + j, y) != 3) {
                                wordToTest.setCharAt(i, word.charAt(i));
                                wordToTest.setCharAt(j, '*');
                                break;
                            }
                    }
                    for (Character letter : drawingArray) {
                        if (letter == '*') {
                            drawingArray.remove(index);
                            break;
                        }
                        index++;
                    }
                }
            }
            if (lettersCount < lenght) return null;
        }

        if (!this.h) {//si le mot dépasse, s'il est suivi et précédé d'une lettre déjà placée, renvoie null
            if ((y + lenght) > 15) return null;
            if ((y + lenght) < 15)
                if (grid.getCoordinates(x, y + lenght) != ' ' && grid.getCoordinates(x, y + lenght) != '#')
                    return null;
            if (y > 0)
                if (grid.getCoordinates(x, y - 1) != ' ' && grid.getCoordinates(x, y - 1) != '#') return null;
            for (int i = 0; i < lenght; i++) {
                if (grid.getCoordinates(x, y + i) == '#') {
                    isFitting = true;
                    break;
                }
            }
            if (!isFitting) return null; //s'il n'existe pas de point d'ancrage, on renvoie FAUX

            for (int i = 0; i < lenght; i++) {
                if (grid.getCoordinates(x, y + i) == word.charAt(i)) lettersCount++;
                else if (grid.getCoordinates(x, y + i) != ' ' && grid.getCoordinates(x, y + i) != '#')
                    return null;
                else if (drawingArray.contains(word.charAt(i))) {
                    lettersCount++;
                    int index = 0;
                    for (Character c : drawingArray) {
                        if (c == word.charAt(i)) {
                            drawingArray.remove(index);
                            break;
                        }
                        index++;
                    }
                } else if (drawingArray.contains('*')) {
                    lettersCount++;
                    int index = 0;
                    wordToTest.setCharAt(i, '*');
                    //insertion choix de la position du joker
                    //si le joker est placé sur case bonus ET le drawing
                    //contient déjà la lettre cherchée
                    if (grid.getBonus(x, y + i) > 1 && drawing.contains(Character.toString(word.charAt(i)))) {
                        //échange de position entre le joker et la lettre déjà placée
                        //si le bonus est inférieur
                        for (int j = 0; j < i; j++)
                            if (word.charAt(j) == word.charAt(i) && grid.getBonus(x, y + j) != 3) {
                                wordToTest.setCharAt(i, word.charAt(i));
                                wordToTest.setCharAt(j, '*');
                                break;
                            }
                    }
                    for (Character letter : drawingArray) {
                        if (letter == '*') {
                            drawingArray.remove(index);
                            break;
                        }
                        index++;
                    }
                }
            }
            if (lettersCount < lenght) return null;
        }
        Grid tempGrid = grid.cloneGrid();
        tempGrid.setWord(this); //essai du  mot testé
        List<String> allWords = tempGrid.placedWords(); // liste de tous les mots  + nouveaux mots formés

        for (String s : allWords) // parcourir tous les nouveaux mots
            if (!grid.getWordList().contains("_" + s + "_") && s.length() > 1) //si  un nouveau mot est formé
                // vérification de l'orthographe
                if (!Dictionary.isCorrectlySpelled(s)) return null;
        //si tous les mots sont corrects
        information[0] = wordToTest.toString();
        information[1] = drawingArray.toString();
        return information;
    }

    int lenght() {
        if (word != null) return word.length();
        return 0;
    }

    char charAt(int i) {
        return word.charAt(i);
    }

    private String onlyDigits(String s) {
        String digits = "";
        for (int i = 0; i < s.length(); i++) {
            Character toTest = s.charAt(i);
            if (Character.isDigit(toTest)) digits += toTest;
        }
        return digits;
    }

    public String toCoordinates() {
        if (Objects.equals(this.word, "-------")) return "---";
        int x = this.getX();
        int y = this.getY();
        boolean h = this.isHorizontal();
        String reponse = "";
        if (h) {
            reponse += Grid.xAxisLetters.charAt(y);
            reponse += x + 1;
        } else {
            reponse += x + 1;
            reponse += Grid.xAxisLetters.charAt(y);
        }
        return reponse;
    }

    public int getScore(Scrabble scrabble, Grid grid, String[] wordSequence) {
        int points = 0, alternativePoints = 0, bonus = 1, alternativeBonus = 1;
        boolean isTouchingUp = false,
                isTouchingDown = false, isTouchingLeft = false, isTouchingRight = false, direction;
        //1 : calcul des points du mot placé
        for (int i = 0; i < this.lenght(); i++) {
            if (this.h) {
                if (grid.getBonus(x + i, y) > 9) {
                    bonus *= (grid.getBonus(x + i, y)) / 10;
                    points += letterValue(wordSequence[0].charAt(i));
                } else
                    points += letterValue(wordSequence[0].charAt(i)) * grid.getBonus(x + i, y);
            } else {
                if (grid.getBonus(x, y + i) > 9) {
                    bonus *= (grid.getBonus(x, y + i) / 10);
                    points += letterValue(wordSequence[0].charAt(i));
                } else
                    points += letterValue(wordSequence[0].charAt(i)) * grid.getBonus(x, y + i);
            }
        }
        points *= bonus;
        //2 : calcul des points des autres mots formés
        if (this.getY() == 0) isTouchingUp = true;
        if (this.getY() == 14) isTouchingDown = true;
        if (this.getX() == 0) isTouchingLeft = true;
        if (this.getX() == 14) isTouchingRight = true;
        for (int i = 0; i < this.lenght(); i++) {
            direction = true;
            if (this.h) {
                if (grid.getCoordinates(x + i, y) == '#') {
                    if (!isTouchingUp) {
                        if (Character.isLetter(grid.getCoordinates(x + i, y - 1))) {
                            direction = false;
                            if (grid.getBonus(x + i, y) > 9) {
                                alternativeBonus *= (grid.getBonus(x + i, y) / 10);
                                alternativePoints += letterValue(wordSequence[0].charAt(i));
                            } else
                                alternativePoints +=
                                        letterValue(wordSequence[0].charAt(i)) * grid.getBonus(x + i, y);
                            for (int j = 1; j <= this.getY(); j++) {
                                if (Character.isLetter(grid.getCoordinates(x + i, y - j)))
                                    alternativePoints +=
                                            letterValue(grid.getCoordinates(x + i, y - j)) *
                                                    grid.getBonus(x + i, y - j);
                                else break;
                            }
                        }
                        points += (alternativePoints * alternativeBonus);
                        alternativePoints = 0;
                        alternativeBonus = 1;
                    }
                    if (!isTouchingDown) {
                        if (Character.isLetter(grid.getCoordinates(x + i, y + 1))) {
                            if (grid.getBonus(x + i, y) > 9 && direction) {
                                alternativeBonus *= (grid.getBonus(x + i, y)) / 10;
                                alternativePoints += letterValue(wordSequence[0].charAt(i));
                            }
                            if (grid.getBonus(x + i, y) < 9 && direction)
                                alternativePoints += letterValue(wordSequence[0].charAt(i)) *
                                        grid.getBonus(x + i, y);
                            for (int j = 1; j <= 14 - this.getY(); j++) {
                                if (Character.isLetter(grid.getCoordinates(x + i, y + j)))
                                    alternativePoints += letterValue(grid.getCoordinates(x + i, y + j)) *
                                            grid.getBonus(x + i, y + j);
                                else break;
                            }
                        }
                        points += (alternativePoints * alternativeBonus);
                        alternativePoints = 0;
                        alternativeBonus = 1;
                    }
                }
            }
            if (!this.h) {
                if (grid.getCoordinates(x, y + i) == '#') {
                    if (!isTouchingLeft) {
                        if (Character.isLetter(grid.getCoordinates(x - 1, y + i))) {
                            direction = false;
                            if (grid.getBonus(x, y + i) > 9) {
                                alternativeBonus *= (grid.getBonus(x, y + i)) / 10;
                                alternativePoints += letterValue(wordSequence[0].charAt(i));
                            } else
                                alternativePoints += letterValue(wordSequence[0].charAt(i)) *
                                        grid.getBonus(x, y + i);
                            for (int j = 1; j <= this.getX(); j++) {
                                if (Character.isLetter(grid.getCoordinates(x - j, y + i)))
                                    alternativePoints += letterValue(grid.getCoordinates(x - j, y + i)) *
                                            grid.getBonus(x - j, y + i);//ajout
                                else break;
                            }
                        }
                        points += (alternativePoints * alternativeBonus);
                        alternativePoints = 0;
                        alternativeBonus = 1;
                    }

                    if (!isTouchingRight) {
                        if (Character.isLetter(grid.getCoordinates(x + 1, y + i))) {
                            if (grid.getBonus(x, y + i) > 9 && direction) {
                                alternativeBonus *= (grid.getBonus(x, y + i)) / 10;
                                alternativePoints += letterValue(wordSequence[0].charAt(i));
                            }
                            if (grid.getBonus(x, y + i) < 9 && direction)
                                alternativePoints += letterValue(wordSequence[0].charAt(i)) *
                                        grid.getBonus(x, y + i);
                            for (int j = 1; j <= 14 - this.getX(); j++) {
                                if (Character.isLetter(grid.getCoordinates(x + j, y + i)))
                                    alternativePoints += letterValue(grid.getCoordinates(x + j, y + i)) *
                                            grid.getBonus(x + j, y + i);//ajout
                                else break;
                            }
                        }
                        points += (alternativePoints * alternativeBonus);
                        alternativePoints = 0;
                        alternativeBonus = 1;
                    }

                }
            }
        }
        //si un Scrabble est obtenu, on ajoute 50 points
        if (Objects.equals(wordSequence[1], "[]") && scrabble.getDrawing().length() > 6) points += 50;
        return points;
    }
}
