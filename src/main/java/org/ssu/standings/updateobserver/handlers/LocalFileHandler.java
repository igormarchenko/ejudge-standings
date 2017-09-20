package org.ssu.standings.updateobserver.handlers;

import org.ssu.standings.updateobserver.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LocalFileHandler implements FileHandler {
    private File file;

    public LocalFileHandler(String path) throws FileNotFoundException {
        this.file = new File(path);
    }

    @Override
    public Response getUpdates() throws IOException {
        if (!file.exists())
            throw new FileNotFoundException();

        if (!file.canRead())
            throw new IOException();

        return new Response.Builder()
                .withLastModified(file.lastModified())
                .withContent(FileReader.getInstance().readContent(new BufferedReader(new java.io.FileReader(file.getPath()))))
                .build();
    }
}
