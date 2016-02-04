package com.nvh.scrabble.model;

import java.io.Serializable;
import java.util.*;

public class Scrabble extends Observable implements Serializable, Observer {
    private static final long serialVersionUID = 1L;
    private ArrayList<Player> players;
    private String tirage;

    private ArrayList<Solution> Solutions;
    private ArrayList<String> histoTirage = new ArrayList<String>();
    private Grid g;
    private int tour;
    private boolean auto, autoTop, partieEncours;
    private long temps;
    private com.nvh.scrabble.service.Timer mainTimer;


    public static final String posLettres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ*";
    public final static int[] pointsLettres = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10, 10, 0};

    public ArrayList<Character> lettres = new ArrayList<Character>(102);
    String tabLettres = "AAAAAAAAABBCCDDDEEEEEEEEEEEEEEEFF"
            + "GGHHIIIIIIIIJKLLLLLMMMNNNNNNOOOOOOPPQRRRRRR"
            + "SSSSSSTTTTTTUUUUUUVVWXYZ**";
    // * : joker    # : raccord possible

    public Scrabble(Grid g, ArrayList<Player> players, boolean auto, boolean autoTop, long temps, ArrayList<Character> lettres,
                    int tour, String tirage, ArrayList<String> histoTirage, ArrayList<Solution> Solutions, boolean partieEncours, com.nvh.scrabble.service.Timer mainTimer) {
        this.g = g;
        this.players = players;
        this.auto = auto;
        this.temps = temps;
        this.tour = 1;
        this.tirage = "";
        this.histoTirage = new ArrayList<String>();
        this.Solutions = new ArrayList<Solution>();
        this.partieEncours = partieEncours;
        this.autoTop = autoTop;
        this.mainTimer = new com.nvh.scrabble.service.Timer(temps);

        g.addObserver(this);

        //init lettres
        for (int i = 0; i < tabLettres.length(); i++)
            this.lettres.add(tabLettres.charAt(i));
    }

    @SuppressWarnings("unchecked")
    public void copyOf(Scrabble aCopier) {
        this.partieEncours = true;
        this.setGrille(aCopier.g);
        this.setPlayers(aCopier.players);
        this.auto = aCopier.auto;
        this.autoTop = aCopier.autoTop;
        this.temps = aCopier.temps;
        this.lettres = (ArrayList<Character>) aCopier.getLettres().clone();
        this.histoTirage = (ArrayList<String>) aCopier.getHistoTirage().clone();
        this.Solutions = aCopier.getSolutions();
        if (aCopier.tour > 1)
            this.tirage = this.Solutions.get(aCopier.tour - 2).getTirageRestant();
        else
            this.tirage = "";
        this.tour = aCopier.tour;
        this.g.raccords();

        setChanged();
        notifyObservers();
    }

    public Grid getGrille() {
        return this.g;
    }

    public void setGrille(Grid aCopier) {
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 15; y++) {
                this.g.set(x, y, aCopier.get(x, y));
                this.g.setBonus(x, y, aCopier.getBonus()[x][y]);
            }
    }

    public Player getJoueur(int i) {
        return players.get(i);
    }

    public ArrayList<Player> getAllJoueurs() {
        return this.players;
    }

    public void addJoueur(String name) {
        this.players.add(new Player(name.toUpperCase(), 0, new ArrayList<Solution>()));
        setChanged();
        notifyObservers();
    }

    public void setPlayers(ArrayList<Player> j) {
        this.players = new ArrayList<Player>();
        for (Player nj : j)
            this.players.add(nj);
        setChanged();
        notifyObservers();
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isAutoTop() {
        return autoTop;
    }

    public void setAutoTop(boolean autoTop) {
        this.autoTop = autoTop;
    }

    public long getTemps() {
        return temps;
    }

    public void setTemps(long temps) {
        this.temps = temps;
        this.mainTimer = new com.nvh.scrabble.service.Timer(temps);
    }

    public ArrayList<Character> getLettres() {
        return lettres;
    }

    public int getTour() {
        return tour;
    }

    public void setTour(int tour) {
        this.tour = tour;
    }


    public ArrayList<Solution> getSolutions() {
        return Solutions;
    }

    public void addSolution(Solution solution) {
        Solutions.add(solution);
        setChanged();
        notifyObservers();
    }

    public void setAllSolutions(ArrayList<Solution> s) {
        this.Solutions = new ArrayList<Solution>();
        for (Solution ls : s)
            this.Solutions.add(ls);
        setChanged();
        notifyObservers();
    }

    public int getNbJoueurs() {
        return this.players.size();
    }


    public ArrayList<String> getHistoTirage() {
        return histoTirage;
    }

    public void setHistoTirage(ArrayList<String> t) {
        this.histoTirage = new ArrayList<String>();
        for (String lt : t)
            this.histoTirage.add(lt);
        setChanged();
        notifyObservers();
    }

    public boolean isPartieEncours() {
        return partieEncours;
    }

    public void setPartieEncours(boolean partieEncours) {
        this.partieEncours = partieEncours;

    }

    public com.nvh.scrabble.service.Timer getMainTimer() {
        return mainTimer;
    }

    public void setMainTimer(com.nvh.scrabble.service.Timer mainTimer) {
        this.mainTimer = mainTimer;
    }

    public String getNombreLettres() {
        String reponse = "";
        for (int i = 0; i < posLettres.length(); i++) {
            int count = 0;
            for (Character c : this.lettres) {
                if (c == posLettres.charAt(i)) count++;
            }
            reponse += posLettres.charAt(i) + " : " + count;
            if ((i + 1) % 3 == 0) reponse += "\n";
            else reponse += "\t";
        }
        return reponse;
    }

    @SuppressWarnings("unchecked")
    public String tirageManu(String u)
    //place le tirage et renvoie "" si possible, sinon renvoie msg d'erreur et ne place rien
    {
        String reponse = "", aReplacer;
        ArrayList<Character> backupLettres;
        u = u.toUpperCase();

        if (!u.startsWith("+") && !u.startsWith("-")) reponse = "Le nouveau tirage doit d�buter par + ou -";
        else
            for (int i = 1; i < u.length(); i++)
                if (!Character.isLetter(u.charAt(i)) && u.charAt(i) != '*')
                    reponse = "Le nouveau tirage ne doit contenir que des lettres ou des jokers";

        if (reponse != "") return reponse;

        backupLettres = (ArrayList<Character>) this.lettres.clone();
        if (u.startsWith("-") && this.tour > 1)    //remise des lettres rejet�es si rejet
        {
            aReplacer = new String(this.Solutions.get(this.tour - 2).getTirageRestant());
            for (int i = 0; i < aReplacer.length(); i++)
                this.lettres.add(aReplacer.charAt(i));
        }
        //controle des lettres : sont-elles encore disponibles ?
        for (int i = 1; i < u.length(); i++) {
            int index = 0;
            if (this.lettres.contains(u.charAt(i)))
                for (Character c : this.lettres) {
                    if (u.charAt(i) == c) {
                        this.lettres.remove(index);
                        break;
                    }
                    index++;
                }
            else reponse = "Les lettres demand�es ne sont plus disponibles";
        }
        if ((this.tour > 1 && u.startsWith("+") &&
                this.getSolutions().get(this.tour - 2).getTirageRestant().length() + u.length() != 8) ||
                (this.tour == 1 && u.length() != 8) || (u.startsWith("-") && u.length() != 8))
            reponse = "Le tirage doit contenir 7 lettres";
        if (reponse == "") this.setTirage(u);
        else this.lettres = (ArrayList<Character>) backupLettres.clone();

        return reponse;
    }

    public void tirageAuto(String depart) {
        String reponse = depart;
        //si des lettres sont restantes : on ajoute juste de quoi compl�ter (avec +)
        if (depart == "-") depart = "";
        else if (this.lettres.size() > 0) reponse += "+";

        int d = depart.length();
        int max = 7;

        Random random = new java.util.Random(System.currentTimeMillis());

        if (d + this.lettres.size() < 7) max = d + this.lettres.size();

        for (int i = d; i < max; i++) {
            int nouvelleL = random.nextInt(this.lettres.size());
            reponse += this.lettres.get(nouvelleL);
            this.lettres.remove(nouvelleL);
        }
        //contr�le de la validit� du tirage (au moins 2 V et 2 C pour les 15 premiers tours, 1 ensuite)
        int countV = 0, countC = 0;
        for (int i = 0; i < reponse.length(); i++) {
            char c = reponse.charAt(i);
            if (c == '*') {
                countV++;
                countC++;
            } else if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y') countV++;
            else if (Character.isLetter(c)) countC++;
        }

        if ((this.tour < 16 && countV > 1 && countC > 1) || (this.tour > 15 && countV > 0 && countC > 0))
            this.setTirage(reponse); // <--- si le tirage convient
        else {//sinon remise des lettres dans le sac et nouveau tirage si possible
            for (int i = 0; i < reponse.length(); i++) {
                if (Character.isLetter(reponse.charAt(i)) || reponse.charAt(i) == '*')
                    this.lettres.add(reponse.charAt(i));
            }
            if (lettresSuffisantes()) tirageAuto("-");
            else {
                this.partieEncours = false;
                return;
            }
        }
    }

    public String getTirage() {
        if (this.tirage == null) return "";
        else
            return this.tirage;
    }

    public void setTirage(String t) {
        if (t == "") {
            this.tirage = "";
            return;
        }
        t = t.toUpperCase();
        if (t.startsWith("+") && this.tour > 1) {
            t = this.Solutions.get(this.tour - 2).getTirageRestant() + t;
            //si on joue au moins le 2�me tour et que le tirage commence par + on ajoute juste les nouvelles lettres au reste pr�c�dent
        }

        String rep = "";
        for (int i = 0; i < t.length(); i++) {
            if (Character.isLetter(t.charAt(i)) || t.charAt(i) == '*' || t.charAt(i) == '+' || t.charAt(i) == '-')
                rep += t.charAt(i);
        }
        this.histoTirage.add(rep);
        setChanged();
        notifyObservers(rep);
        rep = rep.replace("+", "");
        rep = rep.replace("-", "");
        this.tirage = rep;


    }

    public boolean lettresSuffisantes() { //if (this.lettres.size()<1) return false;
        int countV = 0, countC = 0;
        for (Character c : this.lettres) {

            if (c == '*') {
                countV++;
                countC++;
            } else if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y') countV++;
            else
                countC++;
        }
        if (this.tour > 1) {
            String tr = this.getSolutions().get(this.tour - 2).getTirageRestant();
            for (int i = 0; i < tr.length(); i++) {
                if (tr.charAt(i) == '*') {
                    countV++;
                    countC++;
                } else if (tr.charAt(i) == 'A' || tr.charAt(i) == 'E' || tr.charAt(i) == 'I'
                        || tr.charAt(i) == 'O' || tr.charAt(i) == 'U' || tr.charAt(i) == 'Y') countV++;
                else
                    countC++;
            }
        }
        if (countC > 0 && countV > 0) return true;
        else return false;
    }

    public class Mot implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 6044242150909210877L;


        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getMot() {
            return nom;
        }


        private String nom;
        private int x, y;
        private boolean h;


        public Mot(String nom, int x, int y, boolean h) {
            this.nom = nom;
            this.x = x;
            this.y = y;
            this.h = h;
        }

        public Mot(String nom, String coord)                //construteur avec "vraies" coordonn�es type Scrabble
        {
            this.nom = nom;
            Character X = coord.charAt(0);
            Character Y = coord.charAt(coord.length() - 1);

            if (Character.isLetter(X)) {
                this.h = true;
                this.y = posLettres.indexOf(X);
                this.x = Integer.parseInt(onlyDigits(coord)) - 1;
            } else {
                this.h = false;
                this.x = Integer.parseInt(onlyDigits(coord)) - 1;
                this.y = posLettres.indexOf(Y);
            }
        }

        public int lettrePoints(char c) {
            return pointsLettres[posLettres.indexOf(c)];
        }


        public boolean isHorizontal() {
            return this.h;
        }

        public String[] motPossible(String t, Grid g)
        // Teste un Mot plac� : renvoie les lettres utilis�es (index 0) et le tirage restant (index1)
        // OU null si impossible

        {
            String m = this.nom;

            int x = this.x;
            int y = this.y;
            int l = this.longueur();
            int countL = 0;
            boolean raccord = false;
            String[] retour = new String[2];
            StringBuffer temp = new StringBuffer(m);
            ArrayList<Character> tirageBase = new ArrayList<Character>();
            for (int i = 0; i < t.length(); i++) {
                tirageBase.add(t.charAt(i));
            }

            if (this.h) {//si le mot d�passe, s'il est suivi ou pr�c�d� d'une lettre d�j� plac�e, on renvoie FAUX
                if ((x + l) > 15) return null;
                if ((x + l) < 15) if (g.getCoor()[x + l][y] != ' ' && g.getCoor()[x + l][y] != '#') return null;
                if (x > 0) if (g.getCoor()[x - 1][y] != ' ' && g.getCoor()[x - 1][y] != '#') return null;

                for (int i = 0; i < l; i++) {
                    if (g.getCoor()[x + i][y] == '#') {
                        raccord = true;
                        break;
                    }
                }
                if (!raccord) return null; //s'il n'existe pas de point d'ancrage, on renvoie FAUX

                for (int i = 0; i < l; i++) {
                    if (g.getCoor()[x + i][y] == m.charAt(i)) countL++;
                    else if (g.getCoor()[x + i][y] != ' ' && g.getCoor()[x + i][y] != '#') return null;
                    else if (tirageBase.contains(m.charAt(i))) {
                        countL++;
                        int index = 0;
                        for (Character c : tirageBase) {
                            if (c == m.charAt(i)) {
                                tirageBase.remove(index);
                                break;
                            }
                            index++;
                        }
                    } else if (tirageBase.contains('*')) {
                        countL++;
                        int index = 0;
                        temp.setCharAt(i, '*');
                        //insertion choix de la position du joker
                        //si le joker est plac� sur case bonus ET le tirage
                        //contient d�j� la lettre cherch�e
                        if (g.getBonus()[x + i][y] > 1 && t.contains(Character.toString(m.charAt(i)))) {
                            //�change de position entre le joker et la lettre d�j� plac�e
                            //si le bonus est inf�rieur
                            for (int j = 0; j < i; j++)
                                if (m.charAt(j) == m.charAt(i) && g.getBonus()[x + j][y] != 3) {
                                    temp.setCharAt(i, m.charAt(i));
                                    temp.setCharAt(j, '*');
                                    break;
                                }

                        }
                        for (Character c : tirageBase) {
                            if (c == '*') {
                                tirageBase.remove(index);
                                break;
                            }
                            index++;
                        }
                    }
                }
                if (countL < l) return null;
            }

            if (!this.h) {//si le mot d�passe, s'il est suivi et pr�c�d� d'une lettre d�j� plac�e, renvoie null
                if ((y + l) > 15) return null;
                if ((y + l) < 15) if (g.getCoor()[x][y + l] != ' ' && g.getCoor()[x][y + l] != '#') return null;
                if (y > 0) if (g.getCoor()[x][y - 1] != ' ' && g.getCoor()[x][y - 1] != '#') return null;
                for (int i = 0; i < l; i++) {
                    if (g.getCoor()[x][y + i] == '#') {
                        raccord = true;
                        break;
                    }
                }
                if (!raccord) return null; //s'il n'existe pas de point d'ancrage, on renvoie FAUX

                for (int i = 0; i < l; i++) {
                    if (g.getCoor()[x][y + i] == m.charAt(i)) countL++;
                    else if (g.getCoor()[x][y + i] != ' ' && g.getCoor()[x][y + i] != '#') return null;
                    else if (tirageBase.contains(m.charAt(i))) {
                        countL++;
                        int index = 0;
                        for (Character c : tirageBase) {
                            if (c == m.charAt(i)) {
                                tirageBase.remove(index);
                                break;
                            }
                            index++;
                        }
                    } else if (tirageBase.contains('*')) {
                        countL++;
                        int index = 0;
                        temp.setCharAt(i, '*');
                        //insertion choix de la position du joker
                        //si le joker est plac� sur case bonus ET le tirage
                        //contient d�j� la lettre cherch�e
                        if (g.getBonus()[x][y + i] > 1 && t.contains(Character.toString(m.charAt(i)))) {
                            //�change de position entre le joker et la lettre d�j� plac�e
                            //si le bonus est inf�rieur
                            for (int j = 0; j < i; j++)
                                if (m.charAt(j) == m.charAt(i) && g.getBonus()[x][y + j] != 3) {
                                    temp.setCharAt(i, m.charAt(i));
                                    temp.setCharAt(j, '*');
                                    break;
                                }

                        }
                        for (Character c : tirageBase) {
                            if (c == '*') {
                                tirageBase.remove(index);
                                break;
                            }
                            index++;
                        }
                    }
                }
                if (countL < l) return null;

            }

            g.setMot(this); //essai du  mot test�
            List<String> tousMots = g.listeMotsPlaces(); // liste de tous les mots  + nouveaux mots form�s
            g.deleteMot(this);    //effacement du nouvau mot

            for (String s : tousMots) // parcourir tous les nouveaux mots
                if (!g.getListeMots().contains("_" + s + "_") && s.length() > 1) //si  un nouveau mot est form�
                    // v�rif de l'orthographe
                    if (!Dictionary.orthographe(s)) return null;
            //si tous les mots sont corrects
            retour[0] = temp.toString();
            retour[1] = tirageBase.toString();
            return retour;
        }

        public int longueur() {
            if (nom != null) return nom.length();
            return 0;
        }

        public char charAt(int i) {
            return nom.charAt(i);
        }

        public String onlyDigits(String s) {
            String reponse = "";
            for (int i = 0; i < s.length(); i++) {
                Character ctest = s.charAt(i);
                if (Character.isDigit(ctest)) reponse += ctest;
            }
            return reponse;
        }

        public String toCoor() {
            if (this.nom == "-------") return "---";
            int x = this.getX();
            int y = this.getY();
            boolean h = this.isHorizontal();
            String reponse = "";
            if (h) {
                reponse += Grid.coorLettres.charAt(y);
                reponse += x + 1;
            } else {
                reponse += x + 1;
                reponse += Grid.coorLettres.charAt(y);
            }

            return reponse;
        }

        public int getScore(Grid g, String[] sequence) {
            int pts = 0, pts2 = 0, multi = 1, multi2 = 1;
            int[][] bonusT = g.getBonus();
            boolean bordSup = false, bordInf = false, bordG = false, bordD = false, sensUnique;
            //1 : calcul des points du mot plac�
            for (int i = 0; i < this.longueur(); i++) {
                if (this.h) {
                    if (bonusT[x + i][y] > 9) {
                        multi *= (bonusT[x + i][y]) / 10;
                        pts += lettrePoints(sequence[0].charAt(i));
                    } else
                        pts += lettrePoints(sequence[0].charAt(i)) * bonusT[x + i][y];
                } else {
                    if (bonusT[x][y + i] > 9) {
                        multi *= (bonusT[x][y + i]) / 10;
                        pts += lettrePoints(sequence[0].charAt(i));
                    } else
                        pts += lettrePoints(sequence[0].charAt(i)) * bonusT[x][y + i];
                }
            }
            pts *= multi;
            //2 : calcul des points des autres mots form�s
            if (this.getY() == 0) bordSup = true;
            if (this.getY() == 14) bordInf = true;
            if (this.getX() == 0) bordG = true;
            if (this.getX() == 14) bordD = true;
            for (int i = 0; i < this.longueur(); i++) {
                sensUnique = true;
                if (this.h) {
                    if (g.getCoor()[x + i][y] == '#') {
                        if (!bordSup) {
                            if (Character.isLetter(g.getCoor()[x + i][y - 1])) {
                                sensUnique = false;
                                if (bonusT[x + i][y] > 9) {
                                    multi2 *= (bonusT[x + i][y]) / 10;
                                    pts2 += lettrePoints(sequence[0].charAt(i));
                                } else
                                    pts2 += lettrePoints(sequence[0].charAt(i)) * bonusT[x + i][y];
                                for (int j = 1; j <= this.getY(); j++) {
                                    if (Character.isLetter(g.getCoor()[x + i][y - j]))
                                        pts2 += lettrePoints(g.getCoor()[x + i][y - j]) * bonusT[x + i][y - j];
                                    else break;
                                }
                            }
                            pts += (pts2 * multi2);
                            pts2 = 0;
                            multi2 = 1;
                        }
                        if (!bordInf) {
                            if (Character.isLetter(g.getCoor()[x + i][y + 1])) {
                                if (bonusT[x + i][y] > 9 && sensUnique) {
                                    multi2 *= (bonusT[x + i][y]) / 10;
                                    pts2 += lettrePoints(sequence[0].charAt(i));
                                }
                                if (bonusT[x + i][y] < 9 && sensUnique)
                                    pts2 += lettrePoints(sequence[0].charAt(i)) * bonusT[x + i][y];
                                for (int j = 1; j <= 14 - this.getY(); j++) {
                                    if (Character.isLetter(g.getCoor()[x + i][y + j]))
                                        pts2 += lettrePoints(g.getCoor()[x + i][y + j]) * bonusT[x + i][y + j];
                                    else break;
                                }
                            }
                            pts += (pts2 * multi2);
                            pts2 = 0;
                            multi2 = 1;
                        }
                    }
                }
                if (!this.h) {
                    if (g.getCoor()[x][y + i] == '#') {
                        if (!bordG) {
                            if (Character.isLetter(g.getCoor()[x - 1][y + i])) {
                                sensUnique = false;
                                if (bonusT[x][y + i] > 9) {
                                    multi2 *= (bonusT[x][y + i]) / 10;
                                    pts2 += lettrePoints(sequence[0].charAt(i));
                                } else
                                    pts2 += lettrePoints(sequence[0].charAt(i)) * bonusT[x][y + i];
                                for (int j = 1; j <= this.getX(); j++) {
                                    if (Character.isLetter(g.getCoor()[x - j][y + i]))
                                        pts2 += lettrePoints(g.getCoor()[x - j][y + i]) * bonusT[x - j][y + i];//ajout
                                    else break;
                                }
                            }
                            pts += (pts2 * multi2);
                            pts2 = 0;
                            multi2 = 1;
                        }

                        if (!bordD) {
                            if (Character.isLetter(g.getCoor()[x + 1][y + i])) {
                                if (bonusT[x][y + i] > 9 && sensUnique) {
                                    multi2 *= (bonusT[x][y + i]) / 10;
                                    pts2 += lettrePoints(sequence[0].charAt(i));
                                }
                                if (bonusT[x][y + i] < 9 && sensUnique)
                                    pts2 += lettrePoints(sequence[0].charAt(i)) * bonusT[x][y + i];
                                for (int j = 1; j <= 14 - this.getX(); j++) {
                                    if (Character.isLetter(g.getCoor()[x + j][y + i]))
                                        pts2 += lettrePoints(g.getCoor()[x + j][y + i]) * bonusT[x + j][y + i];//ajout
                                    else break;
                                }
                            }
                            pts += (pts2 * multi2);
                            pts2 = 0;
                            multi2 = 1;
                        }

                    }
                }

            }
            if (sequence[1] == "[]" && getTirage().length() > 6) pts += 50;
            return pts;
        }
    }//Mot

    public class Solution implements Comparable<Solution>, Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 1943115288116173821L;
        private Mot m;
        private int points;
        private String[] retour = new String[2];


        public Solution(int points, Mot m, String[] retour) {
            this.m = m;
            this.points = points;
            this.retour = retour;
        }

        public Mot getM() {
            return m;
        }

        public void setM(Mot m) {
            this.m = m;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String[] getRetour() {
            return retour;
        }

        public void setRetour(String[] retour) {
            this.retour = retour;
        }

        public String toString() {
            String scr1, scr2;
            scr1 = retour[0];

            if (retour[1] == "[]" && getTirage().length() > 6) scr2 = "Scrabble !";
            else scr2 = "";
            if (m.longueur() > 6)
                return m.nom + " \t| " + "\t" + Grid.toCoor(m.getX(), m.getY(), m.isHorizontal()) + "\t | \t" + points + "\t | " + scr1;
            return m.nom + "\t \t| " + "\t" + Grid.toCoor(m.getX(), m.getY(), m.isHorizontal()) + "\t | \t" + points + "\t | " + scr1 + "\t | " + scr2;
        }

        public String getTirageRestant() {
            String reponse = new String("");
            for (int i = 0; i < this.retour[1].length(); i++) {
                if (Character.isLetter(this.retour[1].charAt(i)) || this.retour[1].charAt(i) == '*')
                    reponse += this.retour[1].charAt(i);
            }
            return reponse;
        }

        public String getSequence() {
            return this.retour[0];
        }

        public String getMotJokers() //renvoie le mot solution avec la lettre remplac�e par un joker entre ( )
        {
            String reponse = "";
            if (this.getM().getMot() == "-------") return "-------";
            for (int c = 0; c < this.getSequence().length(); c++) {
                if (this.getSequence().charAt(c) != '*') reponse += this.getSequence().charAt(c);
                else reponse += "(" + this.m.getMot().charAt(c) + ")";
            }
            return reponse;
        }


        @Override
        public int compareTo(Solution o) {
            return points == o.points ?
                    Integer.compare(points, o.points) :
                    Integer.compare(o.points, points);
        }
    }//solution

    @Override
    public void update(Observable obs, Object obj) {

        //si l'�v�nement est la mise en place d'une solution :
        if (obj instanceof Solution) {

            this.addSolution((Solution) obj); //ajout de la solution � la partie
            this.getJoueur(0).addPoints(((Solution) obj).getPoints()); //ajout des points du TOP
            this.setTour(this.getTour() + 1); //avanc�e d'un tour

            setChanged();
            notifyObservers(((Solution) obj)); //
            setChanged();
            notifyObservers(((Solution) obj).getTirageRestant()); //notification pour la fen�tre tirage
        } else //pour tout autre �v�nement :
        {
            setChanged();
            notifyObservers();
        }
    }

}
