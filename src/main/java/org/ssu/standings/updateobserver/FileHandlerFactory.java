package org.ssu.standings.updateobserver;

import org.ssu.standings.updateobserver.handlers.FileHandler;
import org.ssu.standings.updateobserver.handlers.HTTPFileHandler;
import org.ssu.standings.updateobserver.handlers.LocalFileHandler;

import java.io.IOException;

public class FileHandlerFactory {
    private FileHandlerFactory() {
    }

    private static final class SingletonHolder {
        private static final FileHandlerFactory chooser = new FileHandlerFactory();
    }

    public static FileHandlerFactory getInstance() {
        return SingletonHolder.chooser;
    }

    public FileHandler createFileHandler(String path) throws IOException {
        return path.startsWith("http://") ? new HTTPFileHandler(path) : new LocalFileHandler(path);
    }
}
