package com.nvh.scrabble;

import com.nvh.scrabble.model.*;
import com.nvh.scrabble.view.MainWindow;
import com.nvh.scrabble.view.internalwindows.ScoreWindow;
import com.nvh.scrabble.service.Solve;
import com.nvh.scrabble.view.internaldialpanes.ConfirmationPane;
import com.nvh.scrabble.view.internaldialpanes.ManualDrawingPane;
import com.nvh.scrabble.view.internalwindows.GameWindow;
import com.nvh.scrabble.view.internalwindows.SolutionWindow;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;


public class Launcher {
    static ArrayList<Player> players = new ArrayList<>();
    public static int phase = 0;
    public static int currentTurn = 1;
    public static Scrabble game;
    public static JFrame gameFrame;

    public static void main(String[] args) {

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        game = new Scrabble(new Grid(null, null, ""), players, false, false, 180000,
                false);

        Player top = new Player("TOP", 0, new ArrayList<>());
        players.add(top);

        new Dictionary();
        new Definitions();
        gameFrame = new MainWindow(game);
        gameFrame.setVisible(true);

        game.addObserver((Observer) MainWindow.scoreFrame);
        game.addObserver((Observer) MainWindow.messageLabel);
        game.addObserver((Observer) MainWindow.solutionsFrame);
        game.addObserver((Observer) MainWindow.gridFrame);
        game.addObserver((Observer) MainWindow.lettersPane);
        game.addObserver((Observer) MainWindow.progressionFrame);


        while (!game.isRunning())
            game.notifyObservers();

        while (game.isRunning()) {

            switch (phase) {
                case 0: //attente de l'utlisateur pour suite jeu

                    MainWindow.mainButton.setText("Tour suivant >>>");
                    MainWindow.saveMenuItem.setEnabled(true);
                    MainWindow.loadMenuItem.setEnabled(true);
                    if (!game.convenientRemainingLetters()) game.setRunning(false);
                    if (game.isAutoDrawing() && game.isAutoTop() && game.getNumberOfPlayers() == 1) phase++;
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
                    new Solve(game);
                    phase++;
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
                                MainWindow.mainButton.setText("CHOISIR TOP"); //top é choisir
                        //sinon :
                        if (!controleTousJoueurs)
                            if (ScoreWindow.scoreTable.getSelectedRow() > -1 && ScoreWindow.scoreTable.getSelectedRow() < game.getNumberOfPlayers())
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
                    phase = 0;
                    break;

            }
        }
        //si game n'est plus en cours (par manque de letters)
        MainWindow.mainButton.setText("Partie terminée");
    }
}
