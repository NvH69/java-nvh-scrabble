package com.nvh.scrabble.service;

import com.nvh.scrabble.model.Grid;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.view.MainWindow;

import java.util.*;

public class Solve extends Observable {

    public static List<Scrabble.Solution> toutesSolutions = new ArrayList<Scrabble.Solution>();
    static ArrayList<Character> tirageBase = new ArrayList<Character>();
    static List<Scrabble.Solution> allSolutions = new ArrayList<Scrabble.Solution>();
    private static Grid g;
    private static String t;
    public static int indexDico;

    public Solve(Scrabble partie) {
        g = partie.getGrille();
        t = partie.getTirage();
        addObserver((Observer) MainWindow.frameSolutions);
        addObserver((Observer) MainWindow.frameProgress);
        addObserver((Observer) Launcher.f);

        MainWindow.frameProgress.setVisible(true);

        for (int i = 0; i < t.length(); i++) {
            tirageBase.add(t.charAt(i));
        }
        partie.getMainTimer().reset();
        indexDico = 0;
        toutesSolutions = new ArrayList<Scrabble.Solution>();


        for (String mot : com.nvh.scrabble.model.Dictionary.dico) {
            {
                if (mot == null || mot.length() < 2) break;
                setChanged();
                notifyObservers((int) (indexDico / 3900));

                Scrabble.Mot motHToTest, motVToTest;
                StringBuffer motTest = new StringBuffer(mot);
                String[] retourMot = new String[2];
                //1er filtre : trouver s�quence manquante sur grille : si NON : pas possible
                if (!tirageBase.contains('*') && partie.getGrille().getSequencePresente() != null) {
                    for (int i = 0; i < mot.length(); i++) // pour chaque lettre du mot � tester
                    {
                        for (int j = 0; j < tirageBase.size(); j++) {
                            if (mot.charAt(i) == tirageBase.get(j)) {
                                motTest.replace(i, i + 1, "_");
                                tirageBase.set(j, '$');
                                break;
                            }  //lettre ok
                        }
                    }
                    if (!partie.getGrille().getSequencePresente().contains(motTest)) break;
                }

                //2nd filtre : exhaustif
                for (int x = 0; x < 15; x++)
                    for (int y = 0; y < 15; y++) {
                        motHToTest = Launcher.partie.new Mot(mot, x, y, true);
                        motVToTest = Launcher.partie.new Mot(mot, x, y, false);

                        if (y >= g.getOptiRaccords()[2] && y <= g.getOptiRaccords()[3]) {
                            retourMot = motHToTest.motPossible(t, g);
                            if (retourMot != null)
                                allSolutions.add((Scrabble.Solution) partie.
                                        new Solution(motHToTest.getScore(g, retourMot), motHToTest, retourMot));

                        }
                        if (x >= g.getOptiRaccords()[0] && x <= g.getOptiRaccords()[1]) {
                            retourMot = motVToTest.motPossible(t, g);
                            if (retourMot != null)
                                allSolutions.add((Scrabble.Solution) partie.
                                        new Solution(motVToTest.getScore(g, retourMot), motVToTest, retourMot));

                        }
                    }
            }

            if (allSolutions != null)
                for (Scrabble.Solution sol : allSolutions) toutesSolutions.add(sol);
            allSolutions.clear();
            indexDico++;
        }

        MainWindow.frameProgress.setVisible(true);

        Collections.sort(toutesSolutions);
        //toutesSolutions.add(0,partie.new Solution(0, partie.new Mot("-------",  "A1"), new String[2]));
        setChanged();
        notifyObservers(toutesSolutions);

    }
}