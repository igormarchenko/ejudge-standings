package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.Contest;
import org.ssu.standings.entity.FileWatcher;
import org.ssu.standings.entity.Submission;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class StandingsWatchService {
    private Map<String, FileWatcher> watchers = new HashMap<>();

    @PostConstruct
    public void init() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(getClass().getClassLoader().getResource("sources.xml").getFile()));
        NodeList files = document.getElementsByTagName("file");
        for (int pos = 0; pos < files.getLength(); pos++) {
            FileWatcher watcher = new FileWatcher(((Element) files.item(pos)).getAttribute("value"));
            String region = ((Element) files.item(pos)).getAttribute("region");
            watcher.setRegion(region);
            watchers.put(region, watcher);
        }
    }

    @Scheduled(fixedDelay = 500)
    public void checkChanges() {
        watchers.values().forEach(FileWatcher::isChanged);
    }

    public Map<String, List<Submission>> getLastSubmissions(Map<String, Long> lastSubmits) {
        return lastSubmits.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> watchers.get(item.getKey()).getLastChanged(item.getValue())));
    }

    public Boolean isContestChanged(Map<String, Long> lastSubmit) {
        return !getLastSubmissions(lastSubmit).isEmpty();
    }
    public List<Contest> getContestData() {
        return watchers.values().stream().map(FileWatcher::getContest).collect(Collectors.toList());
    }

    public List<String> getRegionList() {
        return watchers.keySet().stream().collect(Collectors.toList());
    }
}
