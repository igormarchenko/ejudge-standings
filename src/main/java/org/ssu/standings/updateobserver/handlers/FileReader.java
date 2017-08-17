package org.ssu.standings.updateobserver.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class FileReader {
    private static Optional<FileReader> reader = Optional.empty();

    private FileReader() {
    }

    public static FileReader getIntstance() {
        if (!reader.isPresent())
            reader = Optional.of(new FileReader());

        return reader.get();
    }

    public String readContent(BufferedReader bufferedReader) throws IOException {
        String nextLine;
        StringBuilder content = new StringBuilder();
        while ((nextLine = bufferedReader.readLine()) != null) {
            content.append(nextLine);
        }
        return content.toString();
    }
}
