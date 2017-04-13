package org.ssu.standings.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.ContestDEPRECATED;
import org.ssu.standings.entity.Submission;
import org.ssu.standings.updateobserver.handlers.FileHandler;
import org.ssu.standings.updateobserver.handlers.HTTPFileHandler;
import org.ssu.standings.updateobserver.handlers.LocalFileHandler;

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
    private ContestDEPRECATED contestDEPRECATED = new ContestDEPRECATED();

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
        if (!contestDEPRECATED.isFrozen()) {
            return contestDEPRECATED.getSubmissions()
                    .stream()
                    .filter(item -> contestDEPRECATED.inFrozenTime(contestDEPRECATED.getSubmissionTime(item.getTime())))
                    .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }

    public void updateChanges() {
        List<ContestDEPRECATED> contestDEPRECATEDS = standingsFiles.stream()
                //.filter(FileHandler::hasActualChanges)
                .map(this::updateFileHandlerData)
                .collect(Collectors.toList());
        contestDEPRECATED = ContestsMerger.merge(contestDEPRECATEDS);
    }

    private ContestDEPRECATED updateFileHandlerData(FileHandler fileHandler) {
        return null;
        /*
        SubmissionsParser xmlParser = new SubmissionsParser(fileHandler.getUri());
        return new ContestDEPRECATED().setContestId(xmlParser.getContestId())
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
                */
    }

    private void frozeSubmissions() {
        if (!contestDEPRECATED.getIsFinalResults() || contestDEPRECATED.isFrozen()) {
            contestDEPRECATED.getSubmissions().stream()
                    .filter(item -> contestDEPRECATED.inFrozenTime(contestDEPRECATED.getSubmissionTime(item.getTime())))
                    .forEach(item -> item.setStatus("UNKNOWN"));
        }
    }

    public ContestDEPRECATED getContestDEPRECATED() {
        return contestDEPRECATED;
    }

    public ContestDEPRECATED getContestData() {
        frozeSubmissions();
        return contestDEPRECATED;
    }

    public List<Submission> getLastChanged(Long fromId) {
        return contestDEPRECATED.getSubmissions().stream().filter(item -> item.getRunId() > fromId).collect(Collectors.toList());
    }

    public Long getContestId() {
        return contestDEPRECATED.getId();
    }

    public FileWatcher setContestId(Long id) {
        contestDEPRECATED.setId(id);
        return this;
    }

    public FileWatcher setIsFinalResults(Boolean isFinalResults) {
        this.isFinal = isFinalResults;
        return this;
    }
}
