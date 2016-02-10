package com.nvh.scrabble.model;

import com.nvh.scrabble.service.ResourceLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {

    public static final String dictionaryFile = "/dictionaries/ODS6.txt";
    private static ResourceLoader resourceLoader = new ResourceLoader();
    public static final List<String> dictionary = new ArrayList<>();
//    private static final Logger logger = LoggerFactory.getLogger(Dictionary.class);

    public Dictionary() {

        for (byte j = 2; j <= 15; j++) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(resourceLoader.getFileFromResource(dictionaryFile)));

                String line;
                while (!(line = in.readLine()).equals("*")) {

                    if (line.length() == j) {
                        dictionary.add(line);
                    }
                }
                in.close();
            } catch (IOException e) {
//                logger.error(e.getMessage());
            }
        }
    }

    public static boolean isCorrectlySpelled(String s) {

        int i = 0;
        for (String word : dictionary) {
            if (s.equals(word)) return true;
            i++;
        }
        return false;
    }
}
