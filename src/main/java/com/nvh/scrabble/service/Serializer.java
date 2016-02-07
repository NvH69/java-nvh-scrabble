package com.nvh.scrabble.service;

import com.nvh.scrabble.model.Dictionary;
import com.nvh.scrabble.model.Scrabble;

import java.io.*;

public class Serializer {

    public static void write(Scrabble game, String name) throws IOException {

        ObjectOutputStream oos;
        oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(Dictionary.path + "/savedgames/" + name)));
        oos.writeObject(game);
        oos.close();
    }

    @SuppressWarnings("resource")
    public static Scrabble read(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream ois;
        ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(Dictionary.path + "/savedgames/" + name)));

        return (Scrabble) ois.readObject();
    }
}
