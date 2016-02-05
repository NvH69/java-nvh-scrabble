package com.nvh.scrabble.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyboardListener {
    public static String stringReader()   // lecture d'une chaine
    {
        String readLine = null;
        try {
            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader input = new BufferedReader(reader);
            readLine = input.readLine();
        } catch (IOException err) {
            System.exit(0);
        }
        return readLine;
    }

    public static float floatReader()   // lecture d'un float
    {
        float x = 0;   // valeur a lire
        try {
            String readLine = stringReader();
            x = Float.parseFloat(readLine);
        } catch (NumberFormatException err) {
            System.out.println("*** Erreur de donnee ***");
            System.exit(0);
        }
        return x;
    }

    public static int lireInt()         // lecture d'un int
    {
        int n = 0;
        try {
            String readLine = stringReader();
            n = Integer.parseInt(readLine);
        } catch (NumberFormatException err) {
            System.out.println("*** Erreur de donnee ***");
            System.exit(0);
        }
        return n;
    }
}
