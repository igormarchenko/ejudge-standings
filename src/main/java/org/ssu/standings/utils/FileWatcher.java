package org.ssu.standings.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.Contest;
import org.ssu.standings.entity.Submission;
import org.ssu.standings.entity.TeamEntity;
import org.ssu.standings.file.FileHandler;
import org.ssu.standings.file.HTTPFileHandler;
import org.ssu.standings.file.LocalFileHandler;
import org.ssu.standings.parser.SubmissionsParser;
import org.ssu.standings.repository.TeamRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class FileWatcher {
    @Autowired
    private TeamRepository teamsRepository;

    private Long lastModified = 0L;
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
        updateChanges();
    }

    public List<Submission> getFrozenSubmissions() {
        updateChanges();
        if (!contest.isFrozen()) {
            return contest.getSubmissions()
                    .stream()
                    .filter(item -> contest.inFrozenTime(contest.getSubmissionTime(item.getTime())))
                    .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }

    public Boolean isChanged() {
        Long actualLastModified = standingsFile.lastModified();
        Boolean modified = lastModified != actualLastModified;
        if (modified) {
            updateChanges();
            frozeSubmissions();
        }

        lastModified = actualLastModified;
        return modified;
    }

    private void updateChanges() {
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

    public Long getContestId() {
        return contest.getId();
    }

    public FileWatcher setContestId(Long id) {
        contest.setId(id);
        return this;
    }

    public FileWatcher setIsFinalResults(Boolean isFinalResults) {
        contest.setIsFinalResults(isFinalResults);
        return this;
    }
}
