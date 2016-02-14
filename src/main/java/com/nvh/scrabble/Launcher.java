package com.nvh.scrabble;

import com.nvh.scrabble.controller.ScrabbleController;

import javax.swing.*;

public class Launcher {


    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ScrabbleController();
    }
}
