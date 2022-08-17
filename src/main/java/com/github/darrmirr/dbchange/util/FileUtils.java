package com.github.darrmirr.dbchange.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class dedicated to work with files.
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Read file from path.
     *
     * @param filePath file path to read.
     * @return file content as string.
     */
    public static String readFile(String filePath) {
        try (InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)) {
            if (resource == null) {
                throw new IllegalStateException("could not find file resource : path=" + filePath);
            }
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder
                        .append(line)
                        .append(System.lineSeparator());
            }
            // cut last line separator
            stringBuilder.setLength(stringBuilder.length() - 2);
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
