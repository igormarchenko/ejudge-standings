package org.ssu.standings.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class FileWatcher {

    //    private XmlParser xmlParser;
    private Long lastModified;
    private File standingsFile;
    private Contest contest = new Contest();


    public FileWatcher(String path) {
        standingsFile = new File(path);
        lastModified = 0L;
        updateChanges();
    }

    public Boolean isChanged() {
        Boolean modified = lastModified != standingsFile.lastModified();
//        List<Submission> temp = new ArrayList<>();
        if (modified) {
            updateChanges();
//            if (contest.isFrozen())
            {
                contest.getSubmissions().stream().filter(item -> contest.inFrozenTime(contest.getSubmissionTime(item.getTime()))).forEach(item -> item.setStatus("UNKNOWN"));
            }
        }

        lastModified = standingsFile.lastModified();
        return modified;
    }

    private void updateChanges() {
        try {
            XmlParser xmlParser = new XmlParser(standingsFile);
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

    public Contest getContest() {
        return contest;
    }

    public String getFileName() {
        return standingsFile.getName();
    }

    public List<Submission> getLastChanged(Long fromId) {
        return contest.getSubmissions().stream().filter(item -> item.getRunId() > fromId).collect(Collectors.toList());
    }

    public Long getContestId() {
        return contest.getContestId();
    }

    public void setRegion(String region) {
        contest.setRegion(region);
    }
}
