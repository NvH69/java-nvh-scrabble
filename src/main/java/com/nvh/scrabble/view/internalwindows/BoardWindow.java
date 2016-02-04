package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Grid;
import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.model.Scrabble;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;


@SuppressWarnings("serial")
public class BoardWindow extends JInternalFrame implements Observer {
    static GridBagConstraints gbc_lbl = new GridBagConstraints();
    static String lettres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0";
    static ImageIcon[] lettre = new ImageIcon[27];
    static ImageIcon[] lettre_jok = new ImageIcon[27];
    String chemin = Dictionary.chemin + "/letters/wood/";

    public BoardWindow() {

        getContentPane().setMaximumSize(new Dimension(700, 700));
        getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        getContentPane().setFocusable(false);
        setVisible(true);
        setBounds(10, 11, 655, 673);

        ImagePanel plateau = new ImagePanel(new ImageIcon(Dictionary.chemin+"/boards/boardr0.jpg").getImage(), 0, 0);
        this.getContentPane().add(plateau);
        setContentPane(plateau);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gbc_lbl.anchor = GridBagConstraints.NORTH;
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        gridBagLayout.columnWidths = new int[]{40, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 0};
        gridBagLayout.rowHeights = new int[]{15, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 0};
        getContentPane().setLayout(gridBagLayout);

        for (int i = 0; i < lettre.length; i++) {
            lettre[i] = new ImageIcon(chemin + lettres.charAt(i) + ".jpg");
            lettre_jok[i] = new ImageIcon(chemin + lettres.charAt(i) + "0.jpg");
        }
    }

    public static void drawLettre(char c, int x, int y, boolean joker, Object o) {//dessine une lettre dans un objet graphique donn� (o)
        if (Character.isAlphabetic(c) || c == '0') {
            JLabel lbl;
            if (!joker) lbl = new JLabel(lettre[lettres.indexOf(c)]);
            else lbl = new JLabel(lettre_jok[lettres.indexOf(c)]);

            gbc_lbl.gridx = x;
            gbc_lbl.gridy = y;
            ((RootPaneContainer) o).getContentPane().add(lbl, gbc_lbl);
        }
        ((RootPaneContainer) o).getContentPane().repaint();
    }

    public static void displayGrille(Grid gd, Object o) {//dessine une grille enti�re
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (Character.isLetter(gd.get(x, y)))
                    if (gd.getBonus()[x][y] != 0) // si lettre normale
                        drawLettre(gd.get(x, y), x + 1, y + 1, false, o);
                    else // si joker
                        drawLettre(gd.get(x, y), x + 1, y + 1, true, o);
            }
        }
    }

    public class ImagePanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private Image img;

        public ImagePanel(Image img, int x, int y) {
            this.img = img;


        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, new ImageObserver() {

                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {

                    return false;
                }
            });
        }
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obj instanceof Scrabble.Solution) {
            Scrabble.Solution s = (Scrabble.Solution) obj;
            String mot = s.getM().getMot();
            for (int i = 0; i < mot.length(); i++) {
                if (s.getM().isHorizontal()) {
                    if (s.getRetour()[0].charAt(i) == '*')
                        drawLettre(mot.charAt(i), s.getM().getX() + i + 1, s.getM().getY() + 1, true, this);
                    else
                        drawLettre(mot.charAt(i), s.getM().getX() + i + 1, s.getM().getY() + 1, false, this);
                } else {
                    if (s.getRetour()[0].charAt(i) == '*')
                        drawLettre(mot.charAt(i), s.getM().getX() + 1, s.getM().getY() + i + 1, true, this);
                    else
                        drawLettre(mot.charAt(i), s.getM().getX() + 1, s.getM().getY() + i + 1, false, this);
                }

            }
        }
        this.repaint();
    }
}
