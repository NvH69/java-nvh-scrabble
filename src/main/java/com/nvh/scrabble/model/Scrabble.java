package com.nvh.scrabble.model;

import com.nvh.scrabble.controller.ScrabbleController;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Scrabble extends Observable implements Serializable, Observer {
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ*";

    private static final long serialVersionUID = 1L;
    final static int[] letterValues =
            {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10, 10, 0};
    private ArrayList<Character> letters = new ArrayList<>(102);
    private ArrayList<Player> players;
    private String drawing;
    private ArrayList<Solution> solutions;
    private ArrayList<String> drawingHistory = new ArrayList<>();
    private Grid grid;
    private int turn;
    private boolean autoDrawing, autoTop, running;
    private long timer;
    private com.nvh.scrabble.service.Timer mainTimer;
    private int phase;

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
        this.phase = 0;
        grid.addObserver(this);

        //init letters
        String allLetters = "AAAAAAAAABBCCDDDEEEEEEEEEEEEEEEFF"
                + "GGHHIIIIIIIIJKLLLLLMMMNNNNNNOOOOOOPPQRRRRRR"
                + "SSSSSSTTTTTTUUUUUUVVWXYZ**";
        for (int i = 0; i < allLetters.length(); i++)
            this.letters.add(allLetters.charAt(i));
    }

    public void copyOf(Scrabble toCopy) {
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
        ScrabbleController.currentTurn = this.turn;
        this.grid.fittings();
        this.running = this.isStillDrawable();
        setChanged();
        notifyObservers();
    }

    public Grid getGrid() {
        return this.grid;
    }

    private void setGrid(Grid toBeCopied) {
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


    private void setPlayers(ArrayList<Player> j) {
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

    private void setTurn(int turn) {
        this.turn = turn;
    }

    public ArrayList<Solution> getSolutions() {
        return solutions;
    }


    private void addSolution(Solution solution) {
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

    public int getPhase() {
        return phase;
    }

    private void setPhase(int phase) {
        this.phase = phase;
    }

    public void phaseUp() {
        this.phase++;
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
                this.getSolutions().
                        get(this.turn - 2).getRemainingDrawing().length() + remainingDrawing.length() != 8) ||
                (this.turn == 1 && remainingDrawing.length() != 8) ||
                (remainingDrawing.startsWith("-") && remainingDrawing.length() != 8))
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
            } else if
                    (letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U' || letter == 'Y')
                voyelCount++;
            else if (Character.isLetter(letter)) consonantCount++;
        }

        if ((this.turn < 16 && voyelCount > 1 && consonantCount > 1) ||
                (this.turn > 15 && voyelCount > 0 && consonantCount > 0))
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

    private void setDrawing(String drawing) {
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
            } else if
                    (letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U' || letter == 'Y')
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

    @Override
    public void update(Observable obs, Object obj) {

        //si l'évènement est la mise en place d'une solution :
        if (obj instanceof Solution) {

            this.addSolution((Solution) obj); //ajout de la solution à la partie
            this.getPlayer(0).addPoints(((Solution) obj).getPoints()); //ajout des points du TOP
            this.setTurn(this.getTurn() + 1); //avancée d'un tour

            setChanged();
            notifyObservers(obj);
            setChanged();
            notifyObservers(((Solution) obj).getRemainingDrawing()); //notification pour la fenêtre tirage
            this.setPhase(0);
        } else //pour tout autre évènement :
        {
            setChanged();
            notifyObservers();
        }
    }




}
