package org.ssu.standings.file;

import java.io.*;

public class LocalFileHandler  implements FileHandler{
    private File file;

    public LocalFileHandler(String path) {
        this.file = new File(path);
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public String getUri() {
        return file.getPath();
    }
}
