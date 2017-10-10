package org.ssu.standings.updateobserver.handlers;

import org.ssu.standings.updateobserver.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class HTTPFileHandler implements FileHandler {
    private static final int CONNECTION_TIMEOUT = 100;
    private HttpURLConnection response;

    public HTTPFileHandler(String path) throws IOException {
        reconnect(new URL(path));
    }

    private void reconnect(URL url) throws IOException {
        Optional<HttpURLConnection> connection;
        connection = Optional.of(((HttpURLConnection) url.openConnection()));
        connection.get().setConnectTimeout(CONNECTION_TIMEOUT);
        response = connection.orElseThrow(IOException::new);
    }

    @Override
    public Response getUpdates() throws IOException {
        reconnect(response.getURL());
        if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {
            reconnect(response.getURL());
        }

        return new Response.Builder()
                .withLastModified(response.getLastModified())
                .withContent(FileReader.getInstance().readContent(new BufferedReader(new InputStreamReader(response.getInputStream()))))
                .build();
    }

}
