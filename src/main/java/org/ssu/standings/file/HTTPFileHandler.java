package org.ssu.standings.file;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class HTTPFileHandler implements FileHandler {
    private URL url;
    private Long lastChanges = 0L;

    public HTTPFileHandler(String path) throws IOException {
        url = new URL(path);
    }

    @Override
    public Long lastModified() {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection != null ? connection.getLastModified() : 0;
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
        return null;
    }

    @Override
    public String getUri() {
        try {
            return url.toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }
}
