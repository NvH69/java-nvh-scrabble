package com.nvh.scrabble.service;

import com.nvh.scrabble.model.Scrabble;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Serializer {
private static URL path;

    public static void write(Scrabble game, String name) throws IOException {

        ObjectOutputStream oos;
        try {
           path = new File(name).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path.getFile())));
        oos.writeObject(game);
        oos.close();
    }

    @SuppressWarnings("resource")
    public static Scrabble read(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream ois;
        try {
            path = new File(name).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path.getFile())));

        return (Scrabble) ois.readObject();
    }
}
