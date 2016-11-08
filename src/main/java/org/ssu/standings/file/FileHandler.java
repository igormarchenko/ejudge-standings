package org.ssu.standings.file;

import java.io.File;

public interface FileHandler {
    default Boolean hasActualChanges() {
        return !lastChanges().equals(lastModified());
    }

    Long lastModified();
    Long lastChanges();
    Long updateLastChanges(Long timestamp);
    File getFile();
    String getUri();
}
