package com.nvh.view;

import com.nvh.controller.*;
import com.nvh.controller.Scrabble.Solution;
import com.nvh.controller.Timer;
import com.nvh.model.Definitions;
import com.nvh.model.Dictionnaire;
import com.nvh.model.SampledSon;
import com.nvh.model.Serializer;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;


public class Main {
    static ArrayList<Joueur> j = new ArrayList<Joueur>();
    public static int phase = 0;
    public static int currentTurn = 1;
    public static Scrabble partie;
    public static JFrame f;
    public static ArrayList<SampledSon> sons = new ArrayList<SampledSon>();

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
        partie = new Scrabble(new Grille(null, null), j, false, false, 180000,
                new ArrayList<Character>(), currentTurn, "", null, null, false, new Timer(0));

        Joueur top = new Joueur("TOP", 0, new ArrayList<Solution>());
        j.add(top);

        new Dictionnaire();
        new Definitions();
        f = new FenetrePrincipale(partie);
        f.setVisible(true);

        partie.addObserver((Observer) FenetrePrincipale.frameScores);
        partie.addObserver((Observer) FenetrePrincipale.lblComm);
        partie.addObserver((Observer) FenetrePrincipale.frameSolutions);
        partie.addObserver((Observer) FenetrePrincipale.frameGrille);
        partie.addObserver((Observer) FenetrePrincipale.lettersPane);
        partie.addObserver((Observer) FenetrePrincipale.frameProgress);


        while (!partie.isPartieEncours())
            partie.notifyObservers();

        while (partie.isPartieEncours()) {

            switch (phase) {
                case 0: //attente de l'utlisateur pour suite jeu

                    FenetrePrincipale.mainBtn.setText("Tour suivant >>>");
                    FenetrePrincipale.mntmSauvegarder.setEnabled(true);
                    FenetrePrincipale.mntmReprendre.setEnabled(true);
                    if (!partie.lettresSuffisantes()) partie.setPartieEncours(false);
                    if (partie.isAuto() && partie.isAutoTop() && partie.getNbJoueurs() == 1) phase++;
                    break;

                case 1: //tirage
                    //d�sactivation sauvegarde et ajout nvx joueurs
                    FenetrePrincipale.mntmSauvegarder.setEnabled(false);
                    FenetrePrincipale.mntmReprendre.setEnabled(false);

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
                        new DialTirageManu(partie);

                    if (!partie.isAuto() || !partie.isAutoTop() || partie.getNbJoueurs() > 1)
                        new DialConfirm(partie);
                    FenetrePrincipale.mainBtn.setText("");
                    FenetrePrincipale.mainBtn.setEnabled(false);
                    new Solve(partie);
                    phase++;
                    break;

                case 2: //recherche termin�e
                    FenetrePrincipale.mainBtn.setText("Choisir joueur");
                    FenetrePrincipale.mainBtn.setEnabled(true);
                    FenetreSolutions.btnVoir.setText("Voir toutes les solutions");
                    FenetrePrincipale.frameProgress.dispose();
                    if (partie.isAutoTop() && partie.getNbJoueurs() == 1) {
                        FenetrePrincipale.mainBtn.doClick();
                        FenetreSolutions.btnVoir.doClick();
                    }


                    currentTurn++;

                    while (currentTurn != partie.getTour()) //controle de validation des coups par utilisateurs
                    {
                        FenetrePrincipale.lblChrono.setText(partie.getMainTimer().getLcd());
                        boolean controleTousJoueurs = true;
                        //controle si tous les joueurs ont eu un score :
                        for (int i = 1; i < partie.getNbJoueurs(); i++) {
                            if (partie.getJoueur(i).getNbCoupsJoues() < partie.getTour()) controleTousJoueurs = false;
                        }
                        if (controleTousJoueurs) //si tous les joueurs ont valid� leur choix
                            if (partie.isAutoTop()) FenetrePrincipale.mainBtn.doClick(); //top jou� auto
                            else if (!partie.isAutoTop())
                                FenetrePrincipale.mainBtn.setText("CHOISIR TOP"); //top � choisir
                        //sinon :
                        if (!controleTousJoueurs)
                            if (FenetreScores.tableS.getSelectedRow() > -1 && FenetreScores.tableS.getSelectedRow() < partie.getNbJoueurs())
                                if (FenetreSolutions.table.getSelectedRow() > -1)
                                    FenetrePrincipale.mainBtn.setText("VALIDER");
                                else
                                    FenetrePrincipale.mainBtn.setText("Choisir mot");
                    }
                    //sauvegarde auto en fin de tour
                    try {
                        Serializer.ecrire(partie, "autosave.dat");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FenetrePartie.update();
                    phase = 0;
                    break;

            }
        }
        //si partie n'est plus en cours (par manque de lettres)
        FenetrePrincipale.mainBtn.setText("Partie termin�e");
    }
}
