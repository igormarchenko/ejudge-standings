package org.ssu.standings.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.Contest;
import org.ssu.standings.entity.Submission;
import org.ssu.standings.file.FileHandler;
import org.ssu.standings.file.HTTPFileHandler;
import org.ssu.standings.file.LocalFileHandler;
import org.ssu.standings.parser.SubmissionsParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class FileWatcher {
    private List<FileHandler> standingsFiles;
    private Boolean isFinal;
    private Contest contest = new Contest();

    public FileWatcher(List<String> links) {
        Function<String, FileHandler> getFileHandler = path -> {
            try {
                return path.startsWith("http://") ? new HTTPFileHandler(path) : new LocalFileHandler(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };
        standingsFiles = links.stream().map(getFileHandler).collect(Collectors.toList());
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

    public void updateChanges() {
        List<Contest> contests = standingsFiles.stream()
                .filter(FileHandler::hasActualChanges)
                .map(this::updateFileHandlerData)
                .collect(Collectors.toList());
        contest = ContestsMerger.merge(contests);
    }

    private Contest updateFileHandlerData(FileHandler fileHandler) {
        SubmissionsParser xmlParser = new SubmissionsParser(fileHandler.getUri());
        return new Contest().setContestId(xmlParser.getContestId())
                .setName(xmlParser.getContestName())
                .setTeams(xmlParser.parseTeamList())
                .setSubmissions(xmlParser.parseSubmissionList())
                .setTasks(xmlParser.parseTaskList())
                .setCurrentTime(xmlParser.getCurrentTime())
                .setBeginTime(xmlParser.getStartTime())
                .setEndTime(xmlParser.getEndTime())
                .setFrozenTime(xmlParser.getFrozenTime())
                .setUnfrozenTime(xmlParser.getUnFrozenTime())
                .setIsFinalResults(isFinal);
    }

    private void frozeSubmissions() {
        if (!contest.getIsFinalResults() || contest.isFrozen()) {
            contest.getSubmissions().stream()
                    .filter(item -> contest.inFrozenTime(contest.getSubmissionTime(item.getTime())))
                    .forEach(item -> item.setStatus("UNKNOWN"));
        }
    }

    public Contest getContest() {
        return contest;
    }

    public Contest getContestData() {
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
        this.isFinal = isFinalResults;
        return this;
    }
}
