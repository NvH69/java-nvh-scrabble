package com.nvh.view;

import com.nvh.Launcher;
import com.nvh.controller.Scrabble;
import com.nvh.controller.Scrabble.Solution;
import com.nvh.controller.Solve;
import com.nvh.view.internaldialpanes.ConfirmationPane;
import com.nvh.view.internaldialpanes.PlayerDialPane;
import com.nvh.view.internaldialpanes.FileFrame;
import com.nvh.view.internaldialpanes.PanelLettres;
import com.nvh.view.internalwindows.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import static com.nvh.view.internalwindows.ScoreWindow.*;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Observer {
    public static JInternalFrame lblComm = new DrawingWindow();
    public static JInternalFrame framePartie = new GameWindow();
    public static JInternalFrame frameGrille = new BoardWindow();
    public static JInternalFrame frameSolutions = new SolutionWindow();
    public static JInternalFrame frameScores = new ScoreWindow();
    public static JFrame frameProgress = new ProgressWindow();
    public static JTextPane lettersPane = new PanelLettres();
    public static JLabel lblChrono = new JLabel("");
    public static JButton mainBtn = new JButton();

    private final ButtonGroup buttonTirageGroup = new ButtonGroup();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Partie");
    public static JMenuItem mntmAjouterJoueur = new JMenuItem("Ajouter joueur...");
    public static JCheckBoxMenuItem chckbxmntmTopAutomatique = new JCheckBoxMenuItem("Top automatique");
    public static JMenuItem mntmLancer = new JMenuItem("Lancer !");
    public static JMenuItem mntmSauvegarder = new JMenuItem("Sauvegarder...");
    public static JMenuItem mntmReprendre = new JMenuItem("Reprendre...");
    private JMenu mnOptions = new JMenu("Options");
    public static JCheckBoxMenuItem boxSon = new JCheckBoxMenuItem("Annonce des tirages");
    private JMenuItem mntmRglerTemps = new JMenuItem("R�gler temps...");
    public static JRadioButtonMenuItem rdbtnmntmTirageManuel = new JRadioButtonMenuItem("Tirage manuel");
    public static JRadioButtonMenuItem rdbtnmntmTirageAutomatique = new JRadioButtonMenuItem("Tirage automatique");

    public static String mainFont = "Consolas";

    public MainWindow(Scrabble partie) {

        getContentPane().setBackground(Color.BLACK);
        menuBar.setFont(new Font(mainFont, Font.PLAIN, 12));
        menuBar.setForeground(Color.WHITE);
        menuBar.setBackground(Color.LIGHT_GRAY);
        menuBar.setSize(new Dimension(800, 20));
        menuBar.setPreferredSize(new Dimension(800, 20));
        menuBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        setJMenuBar(menuBar);
        menu.setFont(new Font(mainFont, Font.PLAIN, 12));
        menuBar.add(menu);
        chckbxmntmTopAutomatique.setFont(new Font(mainFont, Font.PLAIN, 12));
        boxSon.setSelected(true);
        menu.add(chckbxmntmTopAutomatique);
        chckbxmntmTopAutomatique.setSelected(true);
        partie.setAutoTop(true);
        mntmAjouterJoueur.setFont(new Font(mainFont, Font.PLAIN, 12));
        menu.add(mntmAjouterJoueur);
        mntmLancer.setFont(new Font(mainFont, Font.PLAIN, 12));

        menu.add(mntmLancer);

        JSeparator separator_1 = new JSeparator();
        menu.add(separator_1);
        mntmSauvegarder.setFont(new Font(mainFont, Font.PLAIN, 12));

        menu.add(mntmSauvegarder);
        mntmReprendre.setFont(new Font(mainFont, Font.PLAIN, 12));

        menu.add(mntmReprendre);
        mnOptions.setFont(new Font(mainFont, Font.PLAIN, 12));

        menuBar.add(mnOptions);
        boxSon.setFont(new Font(mainFont, Font.PLAIN, 12));

        mnOptions.add(boxSon);
        mntmRglerTemps.setFont(new Font(mainFont, Font.PLAIN, 12));

        mnOptions.add(mntmRglerTemps);

        JSeparator separator = new JSeparator();
        mnOptions.add(separator);

        buttonTirageGroup.add(rdbtnmntmTirageManuel);
        rdbtnmntmTirageManuel.setFont(new Font(mainFont, Font.PLAIN, 12));
        mnOptions.add(rdbtnmntmTirageManuel);
        rdbtnmntmTirageManuel.setSelected(true);

        buttonTirageGroup.add(rdbtnmntmTirageAutomatique);
        rdbtnmntmTirageAutomatique.setFont(new Font(mainFont, Font.PLAIN, 12));
        mnOptions.add(rdbtnmntmTirageAutomatique);
        rdbtnmntmTirageAutomatique.setSelected(false);
        partie.setAuto(false);

        initialize(partie);
    }

    private void initialize(Scrabble partie) {

        setBounds(150, 50, 1200, 830);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().add(frameGrille);
        getContentPane().add(frameSolutions);
        getContentPane().add(frameScores);
        getContentPane().add(mainBtn);
        getContentPane().add(lblComm);
        getContentPane().add(framePartie);
        getContentPane().add(lettersPane);
        getContentPane().add(lblChrono);

        mainBtn.setFont(new Font(mainFont, Font.PLAIN, 16));
        mainBtn.setBounds(478, 689, 187, 71);

        lblChrono.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.LIGHT_GRAY));
        lblChrono.setFont(new Font(mainFont, Font.PLAIN, 24));
        lblChrono.setHorizontalTextPosition(SwingConstants.CENTER);
        lblChrono.setHorizontalAlignment(SwingConstants.CENTER);
        lblChrono.setBackground(Color.LIGHT_GRAY);
        lblChrono.setForeground(Color.LIGHT_GRAY);
        lblChrono.setBounds(10, 695, 187, 52);

        mainBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (Launcher.phase == 0) Launcher.phase++;

                if (Launcher.phase == 2 && Solve.toutesSolutions.size() > 0) {
                    boolean controleTousJoueurs = true; //VRAI si tous les joueurs ont valid� leur coup
                    Solution temp = null;
                    //controle que tous les joueurs ont eu un score :
                    for (int i = 1; i < partie.getNbJoueurs(); i++) {
                        if (partie.getJoueur(i).getNbCoupsJoues() < partie.getTour()) controleTousJoueurs = false;
                    }
                    //si non :
                    if (!controleTousJoueurs && SolutionWindow.table.getSelectedRow() != -1) {//place le coup d'un joueur autre que TOP si possible :
                        if (SolutionWindow.table.getSelectedRow() <= Solve.toutesSolutions.size())
                            if (SolutionWindow.table.getSelectedRow() == 0)
                                temp = partie.new Solution(0, partie.new Mot("-------", "A1"), null);
                            else
                                temp = Solve.toutesSolutions.get(SolutionWindow.table.getSelectedRow() - 1);

                        if (tableS.getSelectedRow() > 0 && tableS.getSelectedRow() < partie.getNbJoueurs())

                            if (partie.getJoueur(tableS.getSelectedRow()).getNbCoupsJoues() < partie.getTour())

                                //si je joueur n'a pas encore eu de coup valid� � ce tour :
                                new ConfirmationPane(partie, tableS.getSelectedRow(), temp);
                    }

                    if (controleTousJoueurs) //si tous les joueurs ont valid� leur choix
                    {// place le TOP :

                        if (partie.isAutoTop()) temp = Solve.toutesSolutions.get(0);
                        else {// v�rifie que le top choisi est bien au score max
                            if (SolutionWindow.table.getSelectedRow() > 0
                                    && Solve.toutesSolutions.get(SolutionWindow.table.getSelectedRow() - 1).getPoints()
                                    == Solve.toutesSolutions.get(0).getPoints())

                                temp = Solve.toutesSolutions.get(SolutionWindow.table.getSelectedRow() - 1);
                        }
                        if (temp != null) {
                            if (!partie.isAutoTop() || !partie.isAuto() ||
                                    partie.getNbJoueurs() > 1)
                                new ConfirmationPane(partie, 0, temp);

                            partie.getGrille().setSolution(partie, temp);
                        }
                    }
                }
            }
        });

        mntmLancer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                partie.setPartieEncours(true);
                mntmLancer.setEnabled(false);
                mntmAjouterJoueur.setEnabled(false);
            }
        });
        chckbxmntmTopAutomatique.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                partie.setAutoTop(!partie.isAutoTop());
            }
        });

        rdbtnmntmTirageAutomatique.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdbtnmntmTirageAutomatique.isSelected()) partie.setAuto(true);
                else partie.setAuto(false);

            }
        });

        rdbtnmntmTirageManuel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdbtnmntmTirageManuel.isSelected()) partie.setAuto(false);
                else partie.setAuto(true);

            }
        });

        mntmAjouterJoueur.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new PlayerDialPane(partie);

            }
        });
        mntmSauvegarder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new FileFrame().dialEcrire();
            }
        });

        mntmReprendre.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new FileFrame().dialLire(partie);

            }
        });
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof Solve)
            lblChrono.setText(Launcher.partie.getMainTimer().getLcd());
    }
}
