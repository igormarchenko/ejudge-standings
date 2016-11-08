package org.ssu.standings.file;

import java.io.*;

public class LocalFileHandler  implements FileHandler{
    private File file;
    private Long lastChanges = 0L;

    public LocalFileHandler(String path) {
        this.file = new File(path);
    }

    @Override
    public Long lastModified() {
        return file.lastModified();
    }

    @Override
    public Long lastChanges() {
        return lastChanges;
    }

    @Override
    public Long updateLastChanges(Long timestamp) {
        lastChanges = timestamp;
        return lastChanges;
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
