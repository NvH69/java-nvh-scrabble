package com.nvh.scrabble.service;

import com.nvh.scrabble.model.Scrabble;

import java.io.*;

public class Serializer {

    private static ResourceLoader resourceLoader = new ResourceLoader();

    public static void write(Scrabble game, String name) throws IOException {

        ObjectOutputStream oos;
        final String savingName = "/savedgames/" + name;
        oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(resourceLoader.getFileFromResource(savingName))));
        oos.writeObject(game);
        oos.close();
    }

    public static Scrabble read(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream ois;
        final String saveName = "/savedgames/" + name;
        FileInputStream fileInputStream = new FileInputStream(resourceLoader.getFileFromResource(saveName));
        ois = new ObjectInputStream(new BufferedInputStream(fileInputStream));

        return (Scrabble) ois.readObject();
    }
}
