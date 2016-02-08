package com.nvh.scrabble.view.internaldialpanes;

import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.model.Scrabble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.Calendar;

public class FilePane extends JFrame implements TreeSelectionListener {
    private static final long serialVersionUID = -4105494649356150949L;
    private static final Logger logger = LoggerFactory.getLogger(FilePane.class);
    String path = Dictionary.path + "/savedgames/";
    Calendar calendar = Calendar.getInstance();

    public FilePane(Scrabble game) {
        setBounds(600, 460, 410, 200);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    public void displayDial() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (File file : File.listRoots()) {
            String absolutePath = file.getAbsolutePath();
            DefaultMutableTreeNode lecteur = new DefaultMutableTreeNode(absolutePath);
            logger.info("Chemin du fichier : " + absolutePath);
            if (absolutePath.equals(path)) {

                try {
                    for (File nom : file.listFiles()) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName() + "\\");
                        lecteur.add(this.listFile(nom, node));
                    }
                } catch (NullPointerException ignored) {
                }
                root.add(lecteur);
            }
        }
    }

    public String actualDate() {
        int jour = calendar.get(Calendar.DAY_OF_MONTH);
        int mois = calendar.get(Calendar.MONTH);
        int annee = calendar.get(Calendar.YEAR);
        return String.valueOf(jour) + "_" + String.valueOf(mois) + "_" + String.valueOf(annee);
    }

    private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node) {

        if (file.isFile())
            return new DefaultMutableTreeNode(file.getName());
        else {
            for (File nom : file.listFiles()) {
                DefaultMutableTreeNode subNode;
                if (nom.isDirectory()) {
                    subNode = new DefaultMutableTreeNode(nom.getName() + "\\");
                    node.add(this.listFile(nom, subNode));
                } else {
                    subNode = new DefaultMutableTreeNode(nom.getName());
                }

                node.add(subNode);

            }
            return node;
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
    }
}
