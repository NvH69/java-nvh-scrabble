package com.nvh.scrabble.service;

import com.nvh.scrabble.model.Scrabble;

import java.io.*;
import java.net.URL;

public class Serializer {


    public static void write(Scrabble game, String name) throws IOException {

        ObjectOutputStream oos;
        final URL savingPath = Serializer.class.getResource("/savedgames/" + name);
        oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(savingPath.getFile())));
        oos.writeObject(game);
        oos.close();
    }

    @SuppressWarnings("resource")
    public static Scrabble read(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream ois;
        final URL savingPath = Serializer.class.getResource("/savedgames/");
        ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(savingPath.getPath() + name)));

        return (Scrabble) ois.readObject();
    }
}
