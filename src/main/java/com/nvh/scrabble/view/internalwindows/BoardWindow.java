package com.nvh.scrabble.view.internalwindows;

import com.nvh.scrabble.model.Grid;
import com.nvh.scrabble.model.Solution;
import com.nvh.scrabble.service.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


@SuppressWarnings("serial")
public class BoardWindow extends JInternalFrame implements Observer {
    static final String woodFiles = "/letters/wood/";
    static final String boardPath = "/boards/boardr0.jpg";
    static GridBagConstraints gridBagConstraints = new GridBagConstraints();
    static String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0";
    static ImageIcon[] letterImages = new ImageIcon[27];
    static ImageIcon[] jokerImages = new ImageIcon[27];
    ResourceLoader resourceLoader = new ResourceLoader();

    public BoardWindow() {

        getContentPane().setMaximumSize(new Dimension(700, 700));
        getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        getContentPane().setFocusable(false);
        setVisible(true);
        setBounds(10, 11, 655, 673);
        ImageIcon boardIcon = new ImageIcon(resourceLoader.getFileFromResource(boardPath).getPath());
        ImagePanel boardPanel = new ImagePanel(boardIcon.getImage(), 0, 0);
        this.getContentPane().add(boardPanel);
        this.setFocusable(false);
        setContentPane(boardPanel);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagLayout.rowWeights =
                new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        gridBagLayout.columnWidths =
                new int[]{40, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 0};
        gridBagLayout.rowHeights =
                new int[]{15, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 0};
        getContentPane().setLayout(gridBagLayout);

        for (int i = 0; i < letterImages.length; i++) {
            letterImages[i] =
                    new ImageIcon(resourceLoader.getFileFromResource(
                            woodFiles + letters.charAt(i) + ".jpg").getAbsolutePath());
            if (letters.charAt(i) != '0')
                jokerImages[i] =
                        new ImageIcon(resourceLoader.getFileFromResource(
                                woodFiles + letters.charAt(i) + "0.jpg").getAbsolutePath());
        }
    }

    public static void drawLetter(char c, int x, int y, boolean joker, Object o) {
        //dessine une lettre dans un objet graphique donné (o)
        if (Character.isAlphabetic(c) || c == '0') {
            JLabel jLabel;
            if (!joker) jLabel = new JLabel(letterImages[letters.indexOf(c)]);
            else jLabel = new JLabel(jokerImages[letters.indexOf(c)]);

            gridBagConstraints.gridx = x;
            gridBagConstraints.gridy = y;
            ((RootPaneContainer) o).getContentPane().add(jLabel, gridBagConstraints);
        }
        ((RootPaneContainer) o).getContentPane().repaint();
    }

    public static void displayGrid(Grid grid, Object o) {//dessine une grille entière
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (Character.isLetter(grid.get(x, y)))
                    if (grid.getBonus(x, y) != 0) // si lettre normale
                        drawLetter(grid.get(x, y), x + 1, y + 1, false, o);
                    else // si joker
                        drawLetter(grid.get(x, y), x + 1, y + 1, true, o);
            }
        }
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obj instanceof Solution) {
            Solution s = (Solution) obj;
            String mot = s.getWord().getWord();
            for (int i = 0; i < mot.length(); i++) {
                if (s.getWord().isHorizontal()) {
                    if (s.getInformation()[0].charAt(i) == '*')
                        drawLetter(mot.charAt(i), s.getWord().getX() + i + 1, s.getWord().getY() + 1, true, this);
                    else
                        drawLetter(mot.charAt(i), s.getWord().getX() + i + 1, s.getWord().getY() + 1, false, this);
                } else {
                    if (s.getInformation()[0].charAt(i) == '*')
                        drawLetter(mot.charAt(i), s.getWord().getX() + 1, s.getWord().getY() + i + 1, true, this);
                    else
                        drawLetter(mot.charAt(i), s.getWord().getX() + 1, s.getWord().getY() + i + 1, false, this);
                }

            }
        }
        this.repaint();
    }

    public class ImagePanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private Image img;

        public ImagePanel(Image img, int x, int y) {
            this.img = img;
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, (img1, infoflags, x, y, width, height) -> false);
        }
    }
}
