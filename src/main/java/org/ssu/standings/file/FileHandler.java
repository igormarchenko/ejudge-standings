package org.ssu.standings.file;

import java.io.File;

public interface FileHandler {
    long lastModified();
    File getFile();
    String getUri();
}
