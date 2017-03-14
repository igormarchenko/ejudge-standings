package org.ssu.standings.parser;

import org.ssu.standings.entity.Submission;
import org.ssu.standings.entity.Task;
import org.ssu.standings.entity.Team;
import org.ssu.standings.utils.TeamInUniversityList;
import org.ssu.standings.utils.XmlStream;
import org.w3c.dom.Node;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SubmissionsParser extends Parser {
    static Logger log = Logger.getLogger(SubmissionsParser.class.getName());

    private static final String TASK_ID_ATTRIBUTE = "id";
    private static final String TASK_LONG_NAME_ATTRIBUTE = "long_name";
    private static final String TASK_SHORT_NAME_ATTRIBUTE = "short_name";
    private static final String TEAM_NAME_ATTRIBUTE = "name";
    private static final String TEAM_ID_ATTRIBUTE = "id";
    private static final String DURATION = "duration";
    private static final String RUN_ID_ATTRIBUTE = "run_id";
    private static final String PROBLEM_ID_ATTRIBUTE = "prob_id";
    private static final String LANG_ID_ATTRIBUTE = "lang_id";
    private static final String STATUS_ATTRIBUTE = "status";
    private static final String TIME_ATTRIBUTE = "time";
    private static final String USER_ID_ATTRIBUTE = "user_id";
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final String USERS_TAG = "users";
    private static final String RUN_TAG = "runs";
    private static final String RUNLOG_TAG = "runlog";
    private static final String CONTEST_ID_TAG = "contest_id";
    private static final String STOP_TIME_TAG = "stop_time";
    private static final String START_TIME_TAG = "start_time";
    private static final String FOG_TIME = "fog_time";
    private static final String TASKS_TAG = "problems";
    private static final String UNFOG_TIME = "unfog_time";
    private static final String NAME_TAG = "name";

    public SubmissionsParser(File file) {
        super(file);
    }

    public SubmissionsParser(String uri) {
        super(uri);
    }

    private String getAttributeFromRunLogTag(String attribute) {
        return getAttributeValue(getCurrentNode(RUNLOG_TAG).item(0), attribute);
    }

    public String getContestName() {
        return getCurrentNode(NAME_TAG).item(0).getChildNodes().item(0).getNodeValue();
    }

    public LocalDateTime getFrozenTime() {
        LocalDateTime result = getEndTime();
        try {
            Long fogSeconds = Long.parseLong(getAttributeFromRunLogTag(FOG_TIME));
            result = getEndTime().minusSeconds(fogSeconds);
        } catch (NullPointerException ex) {
            log.warning("Frozen tag is not present!");
        }
        return result;
    }

    public LocalDateTime getUnFrozenTime() {
        LocalDateTime result = getEndTime();
        try {
            Long unfogSeconds = Long.parseLong(getAttributeFromRunLogTag(UNFOG_TIME));
            result = getEndTime().plusSeconds(unfogSeconds);
        } catch (NullPointerException ex) {
            log.warning("Unfrozen tag is not present!");
        }
        return result;
    }

    public List<Task> parseTaskList() {
        return XmlStream.of(getChildNodes(TASKS_TAG))
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(item -> new Task()
                        .setId(Long.parseLong(getAttributeValue(item, TASK_ID_ATTRIBUTE)))
                        .setLongName(getAttributeValue(item, TASK_LONG_NAME_ATTRIBUTE))
                        .setShortName(getAttributeValue(item, TASK_SHORT_NAME_ATTRIBUTE)))
                .collect(Collectors.toList());
    }

    public List<Team> parseTeamList() {
        return XmlStream.of(getChildNodes(USERS_TAG))
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(item -> new Team().setTeamIdInContest(Long.parseLong(getAttributeValue(item, TEAM_ID_ATTRIBUTE)))
                        .setName(getAttributeValue(item, TEAM_NAME_ATTRIBUTE))
                        .setUniversityEntity(TeamInUniversityList.universityForTeam(getAttributeValue(item, TEAM_NAME_ATTRIBUTE)))
                )
                .collect(Collectors.toList());
    }

    public List<Submission> parseSubmissionList() {
        return XmlStream.of(getChildNodes(RUN_TAG))
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(item -> new Submission()
                        .setRunId(Long.parseLong(getAttributeValue(item, RUN_ID_ATTRIBUTE)))
                        .setProblemId(Long.parseLong(getAttributeValue(item, PROBLEM_ID_ATTRIBUTE)))
                        .setLanguageId(Long.parseLong(getAttributeValue(item, LANG_ID_ATTRIBUTE)))
                        .setStatus(getAttributeValue(item, STATUS_ATTRIBUTE))
                        .setTime(Long.parseLong(getAttributeValue(item, TIME_ATTRIBUTE)))
                        .setUserId(Long.parseLong(getAttributeValue(item, USER_ID_ATTRIBUTE))))
                .collect(Collectors.toList());
    }

    private LocalDateTime parseDate(String attribute) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(getAttributeFromRunLogTag(attribute), formatter);
    }

    public Long getDuration() {
        Long duration = 18000L;
        try {
            duration = Long.parseLong(getAttributeFromRunLogTag(DURATION));
        } catch (NullPointerException ex) {
            log.warning("Contest duration tag is not present!");
        }
        return duration;
    }


    public LocalDateTime getCurrentTime() {
        return parseDate("current_time");
    }

    public LocalDateTime getStartTime() {
        LocalDateTime startTime = getCurrentTime();
        try {
            startTime = parseDate(START_TIME_TAG);
        } catch (NullPointerException ex) {
            log.warning("Start time tag is not present! Contest is not started!");
        }
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return getStartTime().plusSeconds(getDuration());
    }

    public Long getContestId() {
        return Long.parseLong(getAttributeFromRunLogTag(CONTEST_ID_TAG));
    }
}
