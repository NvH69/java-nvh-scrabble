package com.nvh.scrabble.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Dictionary {

    private static String line;

    public static final String chemin = new File("Scrabble.jar").getAbsoluteFile().getParent() + "/src/main/resources/";
    public static final String cheminFinal = new String(chemin + "dictionaries/ODS6.txt");

    public static final String[] dico = new String[400000];


    public Dictionary() {
        int i = 0;

        for (byte j = 2; j <= 15; j++) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(cheminFinal));

                while ((line = in.readLine()).equals("*") != true) {

                    if (line.length() == j) {
                        dico[i] = line;
                        i++;
                    }
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean orthographe(String s) {

        int i = 0;
        while (Dictionary.dico[i] != null) {
            if (s.equals(Dictionary.dico[i]) == true) return true;
            i++;
        }

        return false;
    }
}
