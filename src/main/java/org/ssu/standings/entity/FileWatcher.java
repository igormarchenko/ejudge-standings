package org.ssu.standings.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssu.standings.file.FileHandler;
import org.ssu.standings.file.HTTPFileHandler;
import org.ssu.standings.file.LocalFileHandler;
import org.ssu.standings.parser.SubmissionsParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class FileWatcher {
    private Long lastModified;
    private FileHandler standingsFile;
    private Contest contest = new Contest();

    public FileWatcher(String path) {
        if (path.startsWith("http://")) {
            try {
                standingsFile = new HTTPFileHandler(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            standingsFile = new LocalFileHandler(path);
        }

        lastModified = 0L;
        updateChanges();
    }

    public List<Submission> getFrozenSubmissions() {
        updateChanges();
        if (!contest.isFrozen())
            return contest.getSubmissions().stream().filter(item -> contest.inFrozenTime(contest.getSubmissionTime(item.getTime()))).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }

    public Boolean isChanged() {
        Boolean modified = lastModified != standingsFile.lastModified();
        if (modified) {
            updateChanges();
            frozeSubmissions();
        }

        lastModified = standingsFile.lastModified();
        return modified;
    }

    private void updateChanges() {
        try {
            SubmissionsParser xmlParser = new SubmissionsParser(standingsFile.getUri());
            contest.setContestId(xmlParser.getContestId())
                    .setName(xmlParser.getContestName())
                    .setTeams(xmlParser.parseTeamList())
                    .setSubmissions(xmlParser.parseSubmissionList())
                    .setTasks(xmlParser.parseTaskList())
                    .setBeginTime(xmlParser.getStartTime())
                    .setEndTime(xmlParser.getEndTime())
                    .setFrozenTime(xmlParser.getFrozenTime())
                    .setUnfrozenTime(xmlParser.getUnFrozenTime());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void frozeSubmissions() {
        if (!contest.getIsFinalResults() || contest.isFrozen()) {
            contest.getSubmissions().stream()
                    .filter(item -> contest.inFrozenTime(contest.getSubmissionTime(item.getTime())))
                    .forEach(item -> item.setStatus("UNKNOWN"));
        }
    }

    public Contest getContest() {
        frozeSubmissions();
        return contest;
    }

    public List<Submission> getLastChanged(Long fromId) {
        return contest.getSubmissions().stream().filter(item -> item.getRunId() > fromId).collect(Collectors.toList());
    }

    public void setRegion(String region) {
        contest.setRegion(region);
    }

    public FileWatcher setIsFinalResults(Boolean isFinalResults) {
        contest.setIsFinalResults(isFinalResults);
        return this;
    }
}
