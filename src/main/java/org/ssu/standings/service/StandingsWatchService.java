package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.*;
import org.ssu.standings.parser.Parser;
import org.ssu.standings.parser.TeamParser;
import org.ssu.standings.parser.UniversityParser;
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
    private Map<Long, FileWatcher> watchers = new HashMap<>();
    private Parser parser;

    @PostConstruct
    public void init() throws ParserConfigurationException, IOException, SAXException {
        parser = new Parser(new File(getClass().getClassLoader().getResource("sources.xml").getFile()));
        NodeList files = parser.getNodeList("file");
        parseUniversities();
        for (int pos = 0; pos < files.getLength(); pos++) {
            FileWatcher watcher = new FileWatcher(((Element) files.item(pos)).getAttribute("value"));
            String contestId = ((Element) files.item(pos)).getAttribute("contest");
            watcher.setContestId(Long.parseLong(contestId));
            watcher.setIsFinalResults(Boolean.parseBoolean(((Element) files.item(pos)).getAttribute("final")));
            watchers.put(watcher.getContestId(), watcher);
        }

    }

    private void parseUniversities() {
        String teamToUniversity = parser.getNodeList("team-university-file").item(0).getAttributes().item(0).getNodeValue();
        String universityType = parser.getNodeList("university-type-file").item(0).getAttributes().item(0).getNodeValue();

        try {
            UniversityParser universityParser = new UniversityParser(universityType);
            TeamParser teamsParser = new TeamParser(teamToUniversity);
            Map<String, University> universityList = universityParser.universityInfo();
            Map<String, String> teamList = teamsParser.teamList();
            TeamInUniversityList.setTeamUniversity(teamList.entrySet().stream().collect(Collectors.toMap(item -> item.getKey(), item -> universityList.get(item.getValue()))));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getLogin() {
        return getDataStringFromXML("login");
    }

    public String getPassword() {
        return getDataStringFromXML("password");
    }

    private String getDataStringFromXML(String attribute) {
        return parser.getNodeList(attribute).item(0).getChildNodes().item(0).getNodeValue();
    }

    @Scheduled(fixedDelay = 500)
    public void checkChanges() {
        watchers.values().forEach(FileWatcher::isChanged);
    }

    public Map<Long, List<Submission>> getLastSubmissions(Map<Long, Long> lastSubmits) {
        return lastSubmits.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> watchers.get(item.getKey()).getLastChanged(item.getValue())));
    }

    public Boolean isContestChanged(Map<Long, Long> lastSubmit) {
        return !getLastSubmissions(lastSubmit).isEmpty();
    }
    public List<Contest> getContestData() {
        return watchers.values().stream().map(FileWatcher::getContest).collect(Collectors.toList());
    }

//    public List<String> getRegionList() {
//        return watchers.keySet().stream().collect(Collectors.toList());
//    }

    public Map<Long, List<Submission>> getFrozenResults() {
        return  watchers.keySet().stream().collect(Collectors.toMap(item -> item, item -> watchers.get(item).getFrozenSubmissions()));
    }
}
