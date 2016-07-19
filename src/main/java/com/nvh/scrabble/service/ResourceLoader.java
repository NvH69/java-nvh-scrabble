package com.nvh.scrabble.service;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

public class ResourceLoader implements Serializable {
    private static final long serialVersionUID = 5417648932725664389L;
    private File file = null;

    public File getFileFromResource(String resource) {

        URL res = getClass().getResource(resource);
        if (res.toString().startsWith("jar:")) {
            // for JAR
            try {
                InputStream input = res.openStream();

                file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //for IDE
            file = new File(res.getFile());
        }

        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }
        return file;
    }

    public Path getPathFromResource(String resource) {
        URL res = getClass().getResource(resource);
        if (res.toString().startsWith("jar:")) {
            // for JAR
            try {
                InputStream input = res.openStream();

                file = File.createTempFile("tempfile", ".tmp");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //for IDE
            file = new File(res.getFile());
        }

        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }
        return file.toPath();
    }
}
