package com.nvh.scrabble.view;

import com.nvh.scrabble.Launcher;
import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.model.Scrabble.Solution;
import com.nvh.scrabble.service.Solver;
import com.nvh.scrabble.view.internaldialpanes.ConfirmationPane;
import com.nvh.scrabble.view.internaldialpanes.FileFrame;
import com.nvh.scrabble.view.internaldialpanes.LettersPanel;
import com.nvh.scrabble.view.internaldialpanes.PlayerDialPane;
import com.nvh.scrabble.view.internalwindows.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Observer {
    public static JInternalFrame messageLabel = new DrawingWindow();
    public static JInternalFrame gameFrame = new GameWindow();
    public static JInternalFrame gridFrame = new BoardWindow();
    public static JInternalFrame solutionsFrame = new SolutionWindow();
    public static JInternalFrame scoreFrame = new ScoreWindow();
    public static JFrame progressionFrame = new ProgressWindow();
    public static JTextPane lettersPane = new LettersPanel();
    public static JLabel timerLabel = new JLabel("");
    public static JButton mainButton = new JButton();

    public static JMenuItem addPlayerMenuItem = new JMenuItem("Ajouter joueur...");
    public static JCheckBoxMenuItem autoTopCheckBox = new JCheckBoxMenuItem("Top automatique");
    public static JMenuItem LauncherMenuItem = new JMenuItem("Lancer !");
    public static JMenuItem saveMenuItem = new JMenuItem("Sauvegarder...");
    public static JMenuItem loadMenuItem = new JMenuItem("Reprendre...");
    public static JCheckBoxMenuItem soundCheckBox = new JCheckBoxMenuItem("Annonce des tirages");
    public static JRadioButtonMenuItem manualDrawingButton = new JRadioButtonMenuItem("Tirage manuel");
    public static JRadioButtonMenuItem autoDrawingButton = new JRadioButtonMenuItem("Tirage automatique");

    public static String mainFont = "DejaVu Sans Mono";

    public MainWindow(Scrabble game) {

        getContentPane().setBackground(Color.BLACK);
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(new Font(mainFont, Font.PLAIN, 12));
        menuBar.setForeground(Color.WHITE);
        menuBar.setBackground(Color.LIGHT_GRAY);
        menuBar.setSize(new Dimension(800, 20));
        menuBar.setPreferredSize(new Dimension(800, 20));
        menuBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("Partie");
        menu.setFont(new Font(mainFont, Font.PLAIN, 12));
        menuBar.add(menu);
        autoTopCheckBox.setFont(new Font(mainFont, Font.PLAIN, 12));
        soundCheckBox.setSelected(true);
        menu.add(autoTopCheckBox);
        autoTopCheckBox.setSelected(true);
        game.setAutoTop(true);
        addPlayerMenuItem.setFont(new Font(mainFont, Font.PLAIN, 12));
        menu.add(addPlayerMenuItem);
        LauncherMenuItem.setFont(new Font(mainFont, Font.PLAIN, 12));

        menu.add(LauncherMenuItem);

        JSeparator separator_1 = new JSeparator();
        menu.add(separator_1);
        saveMenuItem.setFont(new Font(mainFont, Font.PLAIN, 12));

        menu.add(saveMenuItem);
        loadMenuItem.setFont(new Font(mainFont, Font.PLAIN, 12));

        menu.add(loadMenuItem);
        JMenu optionMenu = new JMenu("Options");
        optionMenu.setFont(new Font(mainFont, Font.PLAIN, 12));

        menuBar.add(optionMenu);
        soundCheckBox.setFont(new Font(mainFont, Font.PLAIN, 12));

        optionMenu.add(soundCheckBox);
        JMenuItem mntmRglerTemps = new JMenuItem("Régler temps...");
        mntmRglerTemps.setFont(new Font(mainFont, Font.PLAIN, 12));

        optionMenu.add(mntmRglerTemps);

        JSeparator separator = new JSeparator();
        optionMenu.add(separator);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(manualDrawingButton);
        manualDrawingButton.setFont(new Font(mainFont, Font.PLAIN, 12));
        optionMenu.add(manualDrawingButton);
        manualDrawingButton.setSelected(true);

        buttonGroup.add(autoDrawingButton);
        autoDrawingButton.setFont(new Font(mainFont, Font.PLAIN, 12));
        optionMenu.add(autoDrawingButton);
        autoDrawingButton.setSelected(false);
        game.setAutoDrawing(false);

        initialize(game);
    }

    private void initialize(Scrabble game) {

        setBounds(150, 50, 1200, 830);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().add(gridFrame);
        getContentPane().add(solutionsFrame);
        getContentPane().add(scoreFrame);
        getContentPane().add(mainButton);
        getContentPane().add(messageLabel);
        getContentPane().add(gameFrame);
        getContentPane().add(lettersPane);
        getContentPane().add(timerLabel);

        mainButton.setFont(new Font(mainFont, Font.PLAIN, 16));
        mainButton.setBounds(478, 689, 187, 71);

        timerLabel.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        timerLabel.setFont(new Font(mainFont, Font.PLAIN, 24));
        timerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setBackground(Color.LIGHT_GRAY);
        timerLabel.setForeground(Color.LIGHT_GRAY);
        timerLabel.setBounds(10, 695, 187, 52);

        mainButton.addActionListener(arg0 -> {
            if (Launcher.phase == 0) Launcher.phase++;

            if (Launcher.phase == 2 && Solver.solutions.size() > 0) {
                boolean areAllPlayersDone = true; //VRAI si tous les joueurs ont validé leur coup
                Solution solution = null;
                //controle que tous les joueurs ont eu un score :
                for (int player = 1; player < game.getNumberOfPlayers(); player++) {
                    if (game.getPlayer(player).getWordsCount() < game.getTurn()) areAllPlayersDone = false;
                }
                //si non :
                if (!areAllPlayersDone && SolutionWindow.table.getSelectedRow() != -1) {//place le coup d'un joueur autre que TOP si possible :
                    if (SolutionWindow.table.getSelectedRow() <= Solver.solutions.size())
                        if (SolutionWindow.table.getSelectedRow() == 0)
                            solution = game.new Solution(0, game.new Word("-------", "A1"), null);
                        else
                            solution = Solver.solutions.get(SolutionWindow.table.getSelectedRow() - 1);

                    if (ScoreWindow.scoreTable.getSelectedRow() > 0 && ScoreWindow.scoreTable.getSelectedRow() < game.getNumberOfPlayers())

                        if (game.getPlayer(ScoreWindow.scoreTable.getSelectedRow()).getWordsCount() < game.getTurn())

                            //si je joueur n'a pas encore eu de coup validé à ce tour :
                            new ConfirmationPane(game, ScoreWindow.scoreTable.getSelectedRow(), solution);
                }

                if (areAllPlayersDone) //si tous les joueurs ont validé leur choix
                {// place le TOP :

                    if (game.isAutoTop()) solution = Solver.solutions.get(0);
                    else {// vérifie que le top choisi est bien au score max
                        if (SolutionWindow.table.getSelectedRow() > 0
                                && Solver.solutions.get(SolutionWindow.table.getSelectedRow() - 1).getPoints()
                                == Solver.solutions.get(0).getPoints())

                            solution = Solver.solutions.get(SolutionWindow.table.getSelectedRow() - 1);
                    }
                    if (solution != null) {
                        if (!game.isAutoTop() || !game.isAutoDrawing() ||
                                game.getNumberOfPlayers() > 1)
                            new ConfirmationPane(game, 0, solution);

                        game.getGrid().setSolution(solution);
                    }
                }
            }
        });

        LauncherMenuItem.addActionListener(e -> {
            game.setRunning(true);
            LauncherMenuItem.setEnabled(false);
            addPlayerMenuItem.setEnabled(false);
        });
        autoTopCheckBox.addActionListener(e -> game.setAutoTop(!game.isAutoTop()));

        autoDrawingButton.addActionListener(e -> {
            if (autoDrawingButton.isSelected()) game.setAutoDrawing(true);
            else game.setAutoDrawing(false);

        });

        manualDrawingButton.addActionListener(e -> {
            if (manualDrawingButton.isSelected()) game.setAutoDrawing(false);
            else game.setAutoDrawing(true);

        });

        addPlayerMenuItem.addActionListener(e -> new PlayerDialPane(game));
        saveMenuItem.addActionListener(e -> new FileFrame().writeDialogBox());

        loadMenuItem.addActionListener(e -> new FileFrame().readDialogBox(game));
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof Solver)
            timerLabel.setText(Launcher.game.getMainTimer().getDisplay());
    }
}
