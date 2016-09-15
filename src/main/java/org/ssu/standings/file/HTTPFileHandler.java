package org.ssu.standings.file;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class HTTPFileHandler implements FileHandler {
    private URL url;

    public HTTPFileHandler(String path) throws IOException {
        url = new URL(path);
    }

    @Override
    public long lastModified() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection.getLastModified();
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
