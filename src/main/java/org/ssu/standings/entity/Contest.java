package org.ssu.standings.entity;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Contest {
    private List<ContestStandingsFileObserver> standingsFileList;

    public Contest(List<String> paths) {
        standingsFileList = paths.stream().map(ContestStandingsFileObserver::new).collect(Collectors.toList());
    }

    public List<String> getContent() {
        return standingsFileList.stream().map(ContestStandingsFileObserver::getContent).collect(Collectors.toList());
    }

    public void updateAllFiles() {
        standingsFileList.parallelStream().forEach(file -> {
            try {
                file.update();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
