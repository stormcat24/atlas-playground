package io.stormcat.sandbox.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class FileLoader {
    
    public static String load(String path) {
        try (InputStream stream = FileLoader.class.getResourceAsStream(path);
                BufferedInputStream bis = new BufferedInputStream(stream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            if (stream == null) {
                throw new FileNotFoundException(path + " not found");
            }

            int data;
            while ((data = bis.read()) != -1) {
                baos.write(data);
            }

            return baos.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
