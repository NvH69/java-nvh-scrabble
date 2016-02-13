package com.nvh.scrabble.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Scrabble extends Observable implements Serializable, Observer {
    private static final long serialVersionUID = 1L;
    private ArrayList<Player> players;
    private String drawing;

    private ArrayList<Solution> solutions;
    private ArrayList<String> drawingHistory = new ArrayList<>();
    private Grid grid;
    private int turn;
    private boolean autoDrawing, autoTop, running;
    private long timer;
    private com.nvh.scrabble.service.Timer mainTimer;


    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ*";
    public final static int[] letterValues = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10, 10, 0};

    public ArrayList<Character> letters = new ArrayList<>(102);
    String allLetters = "AAAAAAAAABBCCDDDEEEEEEEEEEEEEEEFF"
            + "GGHHIIIIIIIIJKLLLLLMMMNNNNNNOOOOOOPPQRRRRRR"
            + "SSSSSSTTTTTTUUUUUUVVWXYZ**";
    // * : joker    # : raccord possible

    public Scrabble(Grid grid, ArrayList<Player> players, boolean autoDrawing, boolean autoTop, long timer,
                    boolean running) {
        this.grid = grid;
        this.players = players;
        this.autoDrawing = autoDrawing;
        this.timer = timer;
        this.turn = 1;
        this.drawing = "";
        this.drawingHistory = new ArrayList<>();
        this.solutions = new ArrayList<>();
        this.running = running;
        this.autoTop = autoTop;
        this.mainTimer = new com.nvh.scrabble.service.Timer(timer);

        grid.addObserver(this);

        //init letters
        for (int i = 0; i < allLetters.length(); i++)
            this.letters.add(allLetters.charAt(i));
    }

    public void copyOf(Scrabble toCopy) {
        this.running = true;
        this.setGrid(toCopy.grid);
        this.setPlayers(toCopy.players);
        this.autoDrawing = toCopy.autoDrawing;
        this.autoTop = toCopy.autoTop;
        this.timer = toCopy.timer;
        this.letters = (ArrayList<Character>) toCopy.getLetters().clone();
        this.drawingHistory = (ArrayList<String>) toCopy.getDrawingHistory().clone();
        this.solutions = toCopy.getSolutions();
        if (toCopy.turn > 1)
            this.drawing = this.solutions.get(toCopy.turn - 2).getRemainingDrawing();
        else
            this.drawing = "";
        this.turn = toCopy.turn;
        this.grid.fittings();
        setChanged();
        notifyObservers();
    }

    public Grid getGrid() {
        return this.grid;
    }

    public void setGrid(Grid toBeCopied) {
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++) {
                this.grid.set(x, y, toBeCopied.get(x, y));
                this.grid.setBonus(x, y, toBeCopied.getBonus(x, y));
            }
    }

    public Player getPlayer(int i) {
        return players.get(i);
    }


    public void addPlayer(String name) {
        this.players.add(new Player(name.toUpperCase(), 0, new ArrayList<>()));
        setChanged();
        notifyObservers();
    }

    public void setPlayers(ArrayList<Player> j) {
        this.players = new ArrayList<>();
        this.players.addAll(j.stream().collect(Collectors.toList()));
        setChanged();
        notifyObservers();
    }

    public boolean isAutoDrawing() {
        return autoDrawing;
    }

    public void setAutoDrawing(boolean autoDrawing) {
        this.autoDrawing = autoDrawing;
    }

    public boolean isAutoTop() {
        return autoTop;
    }

    public void setAutoTop(boolean autoTop) {
        this.autoTop = autoTop;
    }

    public ArrayList<Character> getLetters() {
        return letters;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }


    public ArrayList<Solution> getSolutions() {
        return solutions;
    }

    public void addSolution(Solution solution) {
        solutions.add(solution);
        setChanged();
        notifyObservers();
    }

    public int getNumberOfPlayers() {
        return this.players.size();
    }

    public ArrayList<String> getDrawingHistory() {
        return drawingHistory;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public com.nvh.scrabble.service.Timer getMainTimer() {
        return mainTimer;
    }

    public String getCountOfRemainingLetters() {
        String letter = "";
        for (int i = 0; i < alphabet.length(); i++) {
            int count = 0;
            for (Character c : this.letters) {
                if (c == alphabet.charAt(i)) count++;
            }
            letter += alphabet.charAt(i) + " : " + count;
            if ((i + 1) % 3 == 0) letter += "\n";
            else letter += "\t";
        }
        return letter;
    }

    public String manualDrawing(String remainingDrawing)
    //place le drawing et renvoie "" si possible, sinon renvoie msg d'erreur et ne place rien
    {
        String drawing = "", toBeReplaced;
        ArrayList<Character> backupLetters;
        remainingDrawing = remainingDrawing.toUpperCase();

        if (!remainingDrawing.startsWith("+") && !remainingDrawing.startsWith("-")) {
            drawing = "Le nouveau tirage doit débuter par + ou -";
        } else
            for (int i = 1; i < remainingDrawing.length(); i++)
                if (!Character.isLetter(remainingDrawing.charAt(i)) && remainingDrawing.charAt(i) != '*')
                    drawing = "Le nouveau drawing ne doit contenir que des letters ou des jokers";

        if (!Objects.equals(drawing, "")) return drawing;

        backupLetters = (ArrayList<Character>) this.letters.clone();
        if (remainingDrawing.startsWith("-") && this.turn > 1)    //remise des letters rejetées si rejet
        {
            toBeReplaced = this.solutions.get(this.turn - 2).getRemainingDrawing();
            for (int i = 0; i < toBeReplaced.length(); i++)
                this.letters.add(toBeReplaced.charAt(i));
        }
        //controle des letters : sont-elles encore disponibles ?
        for (int i = 1; i < remainingDrawing.length(); i++) {
            int index = 0;
            if (this.letters.contains(remainingDrawing.charAt(i)))
                for (Character c : this.letters) {
                    if (remainingDrawing.charAt(i) == c) {
                        this.letters.remove(index);
                        break;
                    }
                    index++;
                }
            else drawing = "Les letters demandées ne sont plus disponibles";
        }
        if ((this.turn > 1 && remainingDrawing.startsWith("+") &&
                this.getSolutions().get(this.turn - 2).getRemainingDrawing().length() + remainingDrawing.length() != 8) ||
                (this.turn == 1 && remainingDrawing.length() != 8) || (remainingDrawing.startsWith("-") && remainingDrawing.length() != 8))
            drawing = "Le tirage doit contenir 7 letters";
        if (Objects.equals(drawing, "")) this.setDrawing(remainingDrawing);
        else this.letters = (ArrayList<Character>) backupLetters.clone();

        return drawing;
    }

    public void autoDrawing(String remainingDrawing) {
        String drawing = remainingDrawing;
        //si des letters sont restantes : on ajoute juste de quoi compléter (avec +)
        if (Objects.equals(remainingDrawing, "-")) remainingDrawing = "";
        else if (this.letters.size() > 0) drawing += "+";

        int length = remainingDrawing.length();
        int max = 7;

        Random random = new java.util.Random(System.currentTimeMillis());

        if (length + this.letters.size() < 7) max = length + this.letters.size();

        for (int i = length; i < max; i++) {
            int nouvelleL = random.nextInt(this.letters.size());
            drawing += this.letters.get(nouvelleL);
            this.letters.remove(nouvelleL);
        }
        //contrôle de la validité du drawing (au moins 2 V et 2 C pour les 15 premiers tours, 1 ensuite)
        int voyelCount = 0, consonantCount = 0;
        for (int i = 0; i < drawing.length(); i++) {
            char letter = drawing.charAt(i);
            if (letter == '*') {
                voyelCount++;
                consonantCount++;
            } else if (letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U' || letter == 'Y')
                voyelCount++;
            else if (Character.isLetter(letter)) consonantCount++;
        }

        if ((this.turn < 16 && voyelCount > 1 && consonantCount > 1) || (this.turn > 15 && voyelCount > 0 && consonantCount > 0))
            this.setDrawing(drawing); // <--- si le drawing convient
        else {//sinon remise des letters dans le sac et nouveau drawing si possible
            for (int i = 0; i < drawing.length(); i++) {
                if (Character.isLetter(drawing.charAt(i)) || drawing.charAt(i) == '*')
                    this.letters.add(drawing.charAt(i));
            }
            if (isStillDrawable()) autoDrawing("-");
            else {
                this.running = false;
            }
        }
    }

    public String getDrawing() {
        if (this.drawing == null) return "";
        else
            return this.drawing;
    }

    public void setDrawing(String drawing) {
        if (Objects.equals(drawing, "")) {
            this.drawing = "";
            return;
        }
        drawing = drawing.toUpperCase();
        if (drawing.startsWith("+") && (this.turn > 1)) {
            //si on joue au moins le 2ème turn et que le drawing commence par
            // + on ajoute juste les nouvelles letters au reste précèdent
            drawing = this.solutions.get(this.turn - 2).getRemainingDrawing() + drawing;
        }

        String newDrawing = "";
        for (int i = 0; i < drawing.length(); i++) {
            if (Character.isLetter(drawing.charAt(i)) || drawing.charAt(i) == '*' ||
                    drawing.charAt(i) == '+' || drawing.charAt(i) == '-')
                newDrawing += drawing.charAt(i);
        }
        this.drawingHistory.add(newDrawing);
        setChanged();
        notifyObservers(newDrawing);
        newDrawing = newDrawing.replace("+", "");
        newDrawing = newDrawing.replace("-", "");
        this.drawing = newDrawing;
    }

    public boolean isStillDrawable() { //if (this.letters.size()<1) return false;
        int voyelCount = 0, consonantCount = 0;
        for (Character letter : this.letters) {

            if (letter == '*') {
                voyelCount++;
                consonantCount++;
            } else if (letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U' || letter == 'Y')
                voyelCount++;
            else
                consonantCount++;
        }
        if (this.turn > 1) {
            String tr = this.getSolutions().get(this.turn - 2).getRemainingDrawing();
            for (int i = 0; i < tr.length(); i++) {
                if (tr.charAt(i) == '*') {
                    voyelCount++;
                    consonantCount++;
                } else if (tr.charAt(i) == 'A' || tr.charAt(i) == 'E' || tr.charAt(i) == 'I'
                        || tr.charAt(i) == 'O' || tr.charAt(i) == 'U' || tr.charAt(i) == 'Y') voyelCount++;
                else
                    consonantCount++;
            }
        }
        return consonantCount > 0 && voyelCount > 0;
    }

    public class Word implements Serializable {

        private static final long serialVersionUID = 6044242150909210877L;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getWord() {
            return word;
        }

        private String word;
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

        public int letterValue(char c) {
            return letterValues[alphabet.indexOf(c)];
        }


        public boolean isHorizontal() {
            return this.h;
        }

        public String[] isMatchingWord(String drawing, Grid grid)
        // Teste un Word placé : renvoie les letters utilisées (index 0) et le drawing restant (index1)
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
                        //si le joker est placé sur case bonus ET le drawing
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
                    // vérif de l'isCorrectlySpelled
                    if (!Dictionary.isCorrectlySpelled(s)) return null;
            //si tous les mots sont corrects
            information[0] = wordToTest.toString();
            information[1] = drawingArray.toString();
            return information;
        }

        public int lenght() {
            if (word != null) return word.length();
            return 0;
        }

        public char charAt(int i) {
            return word.charAt(i);
        }

        public String onlyDigits(String s) {
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

        public int getScore(Grid grid, String[] wordSequence) {
            int points = 0, alternativePoints = 0, bonus = 1, alternativeBonus = 1;
            boolean isTouchingUp = false, isTouchingDown = false, isTouchingLeft = false, isTouchingRight = false, direction;
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
                                    alternativePoints += letterValue(wordSequence[0].charAt(i)) * grid.getBonus(x + i, y);
                                for (int j = 1; j <= this.getY(); j++) {
                                    if (Character.isLetter(grid.getCoordinates(x + i, y - j)))
                                        alternativePoints += letterValue(grid.getCoordinates(x + i, y - j)) * grid.getBonus(x + i, y - j);
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
                                    alternativePoints += letterValue(wordSequence[0].charAt(i)) * grid.getBonus(x + i, y);
                                for (int j = 1; j <= 14 - this.getY(); j++) {
                                    if (Character.isLetter(grid.getCoordinates(x + i, y + j)))
                                        alternativePoints += letterValue(grid.getCoordinates(x + i, y + j)) * grid.getBonus(x + i, y + j);
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
                                    alternativePoints += letterValue(wordSequence[0].charAt(i)) * grid.getBonus(x, y + i);
                                for (int j = 1; j <= this.getX(); j++) {
                                    if (Character.isLetter(grid.getCoordinates(x - j, y + i)))
                                        alternativePoints += letterValue(grid.getCoordinates(x - j, y + i)) * grid.getBonus(x - j, y + i);//ajout
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
                                    alternativePoints += letterValue(wordSequence[0].charAt(i)) * grid.getBonus(x, y + i);
                                for (int j = 1; j <= 14 - this.getX(); j++) {
                                    if (Character.isLetter(grid.getCoordinates(x + j, y + i)))
                                        alternativePoints += letterValue(grid.getCoordinates(x + j, y + i)) * grid.getBonus(x + j, y + i);//ajout
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
            if (Objects.equals(wordSequence[1], "[]") && getDrawing().length() > 6) points += 50;
            return points;
        }
    }

    public class Solution implements Comparable<Solution>, Serializable {

        private static final long serialVersionUID = 1943115288116173821L;
        private Word word;
        private int points;
        private String[] information = new String[2];


        public Solution(int points, Word word, String[] information) {
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

            if (Objects.equals(information[1], "[]") && getDrawing().length() > 6) scr2 = "Scrabble !";
            else scr2 = "";
            if (word.lenght() > 6)
                return word.word + " \t| " + "\t" + Grid.toCoordinates(word.getX(), word.getY(), word.isHorizontal()) + "\t | \t" + points + "\t | " + scr1;
            return word.word + "\t \t| " + "\t" + Grid.toCoordinates(word.getX(), word.getY(), word.isHorizontal()) + "\t | \t" + points + "\t | " + scr1 + "\t | " + scr2;
        }

        public String getRemainingDrawing() {
            String remainingDrawing = "";
            for (int i = 0; i < this.information[1].length(); i++) {
                if (Character.isLetter(this.information[1].charAt(i)) || this.information[1].charAt(i) == '*')
                    remainingDrawing += this.information[1].charAt(i);
            }
            return remainingDrawing;
        }

        public String getSequence() {
            return this.information[0];
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

        @Override
        public int compareTo(Solution o) {
            return points == o.points ?
                    Integer.compare(points, o.points) :
                    Integer.compare(o.points, points);
        }
    }

    @Override
    public void update(Observable obs, Object obj) {

        //si l'évènement est la mise en place d'une solution :
        if (obj instanceof Solution) {

            this.addSolution((Solution) obj); //ajout de la solution à la partie
            this.getPlayer(0).addPoints(((Solution) obj).getPoints()); //ajout des points du TOP
            this.setTurn(this.getTurn() + 1); //avancée d'un turn

            setChanged();
            notifyObservers(obj); //
            setChanged();
            notifyObservers(((Solution) obj).getRemainingDrawing()); //notification pour la fenêtre tirage
        } else //pour tout autre évènement :
        {
            setChanged();
            notifyObservers();
        }
    }
}
