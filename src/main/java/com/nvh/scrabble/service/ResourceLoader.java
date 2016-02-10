package com.nvh.scrabble.service;

import java.io.*;
import java.net.URL;

public class ResourceLoader {
    File file = null;
//    Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public File getFileFromResource(String resource) {

        URL res = getClass().getResource(resource);
        if (res.toString().startsWith("jar:")) {
            // for JAR
            try {
//                JarFile jarFile = new JarFile(res.getFile());
                InputStream input = res.openStream();
                file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                file.deleteOnExit();
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
}
