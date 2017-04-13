package org.ssu.standings.updateobserver;

import org.ssu.standings.updateobserver.handlers.FileHandler;
import org.ssu.standings.updateobserver.handlers.HTTPFileHandler;
import org.ssu.standings.updateobserver.handlers.LocalFileHandler;

import java.io.IOException;
import java.util.Optional;

public class FileHandlerFactory {
    private static Optional<FileHandlerFactory> chooser = Optional.empty();

    private FileHandlerFactory() {
    }

    public static FileHandlerFactory getInstance() {
        if (!chooser.isPresent())
            chooser = Optional.of(new FileHandlerFactory());

        return chooser.get();
    }

    public FileHandler createFileHandler(String path) throws IOException {
        return path.startsWith("http://") ? new HTTPFileHandler(path) : new LocalFileHandler(path);
    }
}
