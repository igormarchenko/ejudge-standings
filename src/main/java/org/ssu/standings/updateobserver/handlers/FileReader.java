package org.ssu.standings.updateobserver.handlers;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {
    private FileReader() {
    }

    public static FileReader getInstance() {
        return SingletonHolder.fileReader;
    }

    public String readContent(BufferedReader bufferedReader) throws IOException {
        String nextLine;
        StringBuilder content = new StringBuilder();
        while ((nextLine = bufferedReader.readLine()) != null) {
            content.append(nextLine);
        }
        return content.toString();
    }

    private static final class SingletonHolder {
        private static final FileReader fileReader = new FileReader();
    }
}
