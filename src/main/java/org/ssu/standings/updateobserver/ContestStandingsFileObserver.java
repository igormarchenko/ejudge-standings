package org.ssu.standings.updateobserver;

import org.ssu.standings.dao.entity.StandingsFileDAO;
import org.ssu.standings.updateobserver.handlers.FileHandler;

import java.io.IOException;


public class ContestStandingsFileObserver {
    private FileHandler watcher;
    private Long lastModified = -1L;
    private String content;

    public ContestStandingsFileObserver(StandingsFileDAO standingsFileDAO) {
        this(standingsFileDAO.getLink());
    }

    public ContestStandingsFileObserver(String path) {
        try {
            watcher = FileHandlerFactory.getInstance().createFileHandler(path);
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() throws IOException {
        Response response = watcher.getUpdates();
        if (response.getLastModified() > lastModified) {
            lastModified = response.getLastModified();
            content = response.getContent();
        }
    }

    public String getContent() {
        return content;
    }
}
