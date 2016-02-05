package com.nvh.scrabble.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {

    public static final String path = new File("Scrabble.jar").getAbsoluteFile().getParent() + "/src/main/resources/";
    public static final String finalPath = path + "dictionaries/ODS6.txt";

    public static final String[] dictionary = new String[400000];


    public Dictionary() {
        int i = 0;

        for (byte j = 2; j <= 15; j++) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(finalPath));

                String line;
                while (!(line = in.readLine()).equals("*")) {

                    if (line.length() == j) {
                        dictionary[i] = line;
                        i++;
                    }
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean isCorrectlySpelled(String s) {

        int i = 0;
        while (Dictionary.dictionary[i] != null) {
            if (s.equals(Dictionary.dictionary[i])) return true;
            i++;
        }
        return false;
    }
}
