package com.nvh;

import com.nvh.controller.*;
import com.nvh.controller.Scrabble.Solution;
import com.nvh.controller.Timer;
import com.nvh.model.Definitions;
import com.nvh.model.Dictionary;
import com.nvh.model.SampledSound;
import com.nvh.model.Serializer;
import com.nvh.view.MainWindow;
import com.nvh.view.internaldialpanes.ConfirmationPane;
import com.nvh.view.internaldialpanes.ManualDrawingPane;
import com.nvh.view.internalwindows.GameWindow;
import com.nvh.view.internalwindows.ScoreWindow;
import com.nvh.view.internalwindows.SolutionWindow;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

import static com.nvh.view.MainWindow.*;


public class Launcher {
    static ArrayList<Player> j = new ArrayList<Player>();
    public static int phase = 0;
    public static int currentTurn = 1;
    public static Scrabble partie;
    public static JFrame f;
    public static ArrayList<SampledSound> sons = new ArrayList<SampledSound>();

    public static void main(String[] args) {


        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {

        }
        partie = new Scrabble(new Grid(null, null), j, false, false, 180000,
                new ArrayList<Character>(), currentTurn, "", null, null, false, new Timer(0));

        Player top = new Player("TOP", 0, new ArrayList<Solution>());
        j.add(top);

        new Dictionary();
        new Definitions();
        f = new MainWindow(partie);
        f.setVisible(true);

        partie.addObserver((Observer) frameScores);
        partie.addObserver((Observer) lblComm);
        partie.addObserver((Observer) frameSolutions);
        partie.addObserver((Observer) frameGrille);
        partie.addObserver((Observer) lettersPane);
        partie.addObserver((Observer) frameProgress);


        while (!partie.isPartieEncours())
            partie.notifyObservers();

        while (partie.isPartieEncours()) {

            switch (phase) {
                case 0: //attente de l'utlisateur pour suite jeu

                    mainBtn.setText("Tour suivant >>>");
                    mntmSauvegarder.setEnabled(true);
                    mntmReprendre.setEnabled(true);
                    if (!partie.lettresSuffisantes()) partie.setPartieEncours(false);
                    if (partie.isAuto() && partie.isAutoTop() && partie.getNbJoueurs() == 1) phase++;
                    break;

                case 1: //tirage
                    //d�sactivation sauvegarde et ajout nvx joueurs
                    mntmSauvegarder.setEnabled(false);
                    mntmReprendre.setEnabled(false);

                    //passage � auto si nb lettres insuffisant pour continuer manuel
                    if (partie.getTour() > 1)
                        if (!partie.isAuto() &&
                                partie.getLettres().size()
                                        + partie.getSolutions().get(partie.getTour() - 2).getTirageRestant().length() < 8)
                            partie.setAuto(true);
                    //TIRAGE AUTO
                    if (partie.isAuto()) {
                        if (partie.getTour() > 1)
                            partie.tirageAuto(partie.getSolutions().get(partie.getTour() - 2).getTirageRestant());
                        else partie.tirageAuto("");
                    } else
                        //TIRAGE MANU
                        new ManualDrawingPane(partie);

                    if (!partie.isAuto() || !partie.isAutoTop() || partie.getNbJoueurs() > 1)
                        new ConfirmationPane(partie);
                    mainBtn.setText("");
                    mainBtn.setEnabled(false);
                    new Solve(partie);
                    phase++;
                    break;

                case 2: //recherche termin�e
                    mainBtn.setText("Choisir joueur");
                    mainBtn.setEnabled(true);
                    SolutionWindow.btnVoir.setText("Voir toutes les solutions");
                    frameProgress.dispose();
                    if (partie.isAutoTop() && partie.getNbJoueurs() == 1) {
                        mainBtn.doClick();
                        SolutionWindow.btnVoir.doClick();
                    }


                    currentTurn++;

                    while (currentTurn != partie.getTour()) //controle de validation des coups par utilisateurs
                    {
                        lblChrono.setText(partie.getMainTimer().getLcd());
                        boolean controleTousJoueurs = true;
                        //controle si tous les joueurs ont eu un score :
                        for (int i = 1; i < partie.getNbJoueurs(); i++) {
                            if (partie.getJoueur(i).getNbCoupsJoues() < partie.getTour()) controleTousJoueurs = false;
                        }
                        if (controleTousJoueurs) //si tous les joueurs ont valid� leur choix
                            if (partie.isAutoTop()) mainBtn.doClick(); //top jou� auto
                            else if (!partie.isAutoTop())
                                mainBtn.setText("CHOISIR TOP"); //top � choisir
                        //sinon :
                        if (!controleTousJoueurs)
                            if (ScoreWindow.tableS.getSelectedRow() > -1 && ScoreWindow.tableS.getSelectedRow() < partie.getNbJoueurs())
                                if (SolutionWindow.table.getSelectedRow() > -1)
                                    mainBtn.setText("VALIDER");
                                else
                                    mainBtn.setText("Choisir mot");
                    }
                    //sauvegarde auto en fin de tour
                    try {
                        Serializer.ecrire(partie, "autosave.dat");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GameWindow.update();
                    phase = 0;
                    break;

            }
        }
        //si partie n'est plus en cours (par manque de lettres)
        mainBtn.setText("Partie termin�e");
    }
}
