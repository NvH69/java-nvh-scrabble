package com.nvh.scrabble.controller;

import com.nvh.scrabble.model.*;
import com.nvh.scrabble.service.Serializer;
import com.nvh.scrabble.service.Solver;
import com.nvh.scrabble.view.MainWindow;
import com.nvh.scrabble.view.internaldialpanes.ConfirmationPane;
import com.nvh.scrabble.view.internaldialpanes.ManualDrawingPane;
import com.nvh.scrabble.view.internalwindows.GameWindow;
import com.nvh.scrabble.view.internalwindows.ScoreWindow;
import com.nvh.scrabble.view.internalwindows.SolutionWindow;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class ScrabbleController {

    public static int currentTurn = 1;
    public static Scrabble game;
    public static JFrame gameFrame;
    public static Solver solver = new Solver();
    static ArrayList<Player> players = new ArrayList<>();

    public ScrabbleController() {

        game = new Scrabble(new Grid(null, null, ""), players, false, false, 180000,
                false);

        Player top = new Player("TOP", 0, new ArrayList<>());
        players.add(top);

        new Dictionary();
        new Definitions();
        gameFrame = new MainWindow(game);
        gameFrame.setVisible(true);

        ObserversController.swingObserversImplementer(game);

        while (!game.isRunning()) {
            game.notifyObservers();
            if (currentTurn == 1)
                MainWindow.mainButton.setText("En attente...");
            else
                MainWindow.mainButton.setText("Partie terminée");
        }

        while (game.isRunning()) {

            switch (game.getPhase()) {
                case 0: //attente de l'utlisateur pour suite jeu

                    MainWindow.mainButton.setText("Tour suivant >>>");
                    MainWindow.saveMenuItem.setEnabled(true);
                    MainWindow.loadMenuItem.setEnabled(true);
                    if (!game.isStillDrawable()) game.setRunning(false);
                    if (game.isAutoDrawing() && game.isAutoTop() && game.getNumberOfPlayers() == 1) game.phaseUp();
                    break;

                case 1: //tirage
                    //désactivation sauvegarde et ajout nvx joueurs
                    MainWindow.saveMenuItem.setEnabled(false);
                    MainWindow.loadMenuItem.setEnabled(false);

                    //passage à auto si nb letters insuffisant pour continuer manuel
                    if (game.getTurn() > 1)
                        if (!game.isAutoDrawing() &&
                                game.getLetters().size()
                                        + game.getSolutions().get(game.getTurn() - 2).getRemainingDrawing().length() < 8)
                            game.setAutoDrawing(true);
                    //TIRAGE AUTO
                    if (game.isAutoDrawing()) {
                        if (game.getTurn() > 1)
                            game.autoDrawing(game.getSolutions().get(game.getTurn() - 2).getRemainingDrawing());
                        else game.autoDrawing("");
                    } else
                        //TIRAGE MANU
                        new ManualDrawingPane(game);

                    if (!game.isAutoDrawing() || !game.isAutoTop() || game.getNumberOfPlayers() > 1)
                        new ConfirmationPane(game);
                    MainWindow.mainButton.setText("");
                    MainWindow.mainButton.setEnabled(false);
                    solver.solve(game);
                    break;

                case 2: //recherche terminée
                    MainWindow.mainButton.setText("Choisir joueur");
                    MainWindow.mainButton.setEnabled(true);
                    SolutionWindow.showButton.setText("Voir toutes les solutions");
                    MainWindow.progressionFrame.dispose();
                    if (game.isAutoTop() && game.getNumberOfPlayers() == 1) {
                        MainWindow.mainButton.doClick();
                        SolutionWindow.showButton.doClick();
                    }
                    currentTurn++;

                    while (currentTurn != game.getTurn()) //controle de validation des coups par utilisateurs
                    {
                        MainWindow.timerLabel.setText(game.getMainTimer().getDisplay());
                        boolean controleTousJoueurs = true;
                        //controle si tous les joueurs ont eu un score :
                        for (int i = 1; i < game.getNumberOfPlayers(); i++) {
                            if (game.getPlayer(i).getWordsCount() < game.getTurn()) controleTousJoueurs = false;
                        }
                        if (controleTousJoueurs) //si tous les joueurs ont validé leur choix
                            if (game.isAutoTop()) MainWindow.mainButton.doClick(); //top joué auto
                            else if (!game.isAutoTop())
                                MainWindow.mainButton.setText("CHOISIR TOP"); //top à choisir
                        //sinon :
                        if (!controleTousJoueurs)
                            if (ScoreWindow.scoreTable.getSelectedRow() > -1 && ScoreWindow.scoreTable.getSelectedRow()
                                    < game.getNumberOfPlayers())
                                if (SolutionWindow.table.getSelectedRow() > -1)
                                    MainWindow.mainButton.setText("VALIDER");
                                else
                                    MainWindow.mainButton.setText("Choisir mot");
                    }
                    //sauvegarde auto en fin de tour
                    try {
                        Serializer.write(game, "autosave.dat");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GameWindow.update();
                    break;
            }
        }
        //si la partie n'est plus en cours (par manque de lettres)
        MainWindow.mainButton.setText("Partie terminée");
    }
}
