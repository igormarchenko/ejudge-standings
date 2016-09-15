package org.ssu.standings.parser;

import org.ssu.standings.entity.Submission;
import org.ssu.standings.entity.Task;
import org.ssu.standings.entity.Team;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SubmissionsParser extends Parser{
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

    public SubmissionsParser(File file) throws ParserConfigurationException, FileNotFoundException {
        super(file);
    }

    public SubmissionsParser(String uri) throws ParserConfigurationException {
        super(uri);
    }

    private String getAttributeFromRunLogTag(String attribute) {
        return getNodeList(RUNLOG_TAG).item(0).getAttributes().getNamedItem(attribute).getNodeValue();
    }

    public String getContestName() {
        return getNodeList(NAME_TAG).item(0).getChildNodes().item(0).getNodeValue();
    }
    public LocalDateTime getFrozenTime() {
        Long fogSeconds = Long.parseLong(getAttributeFromRunLogTag(FOG_TIME));
        return getEndTime().minusSeconds(fogSeconds);
    }
    
    public LocalDateTime getUnFrozenTime() {
        Long unfogSeconds = Long.parseLong(getAttributeFromRunLogTag(UNFOG_TIME));
        return getEndTime().plusSeconds(unfogSeconds);
    }
    
    public List<Task> parseTaskList() {
        List<Task> result = new ArrayList<>();
        NodeList tasks = getSingleNode(TASKS_TAG);

        for (int pos = 0; pos < tasks.getLength(); pos++) {
            Node currentNode = tasks.item(pos);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element task = (Element) currentNode;
                result.add(new Task()
                        .setId(Long.parseLong(task.getAttribute("id")))
                        .setLongName(task.getAttribute("long_name"))
                        .setShortName(task.getAttribute("short_name")));
            }
        }
        return result;

    }

    public List<Team> parseTeamList() {
        List<Team> result = new ArrayList<>();
        NodeList users = getSingleNode(USERS_TAG);

        for (int pos = 0; pos < users.getLength(); pos++) {
            Node currentNode = users.item(pos);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element user = (Element) currentNode;
                result.add(new Team(Long.parseLong(user.getAttribute("id")), user.getAttribute("name")));
            }
        }
        return result;
    }

    public List<Submission> parseSubmissionList() {
        List<Submission> result = new ArrayList<>();
        NodeList runs = getSingleNode(RUN_TAG);
        for (int pos = 0; pos < runs.getLength(); pos++) {
            Node currentNode = runs.item(pos);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element run = (Element) currentNode;
                result.add(
                        new Submission()
                                .setRunId(Long.parseLong(run.getAttribute("run_id")))
                                .setProblemId(Long.parseLong(run.getAttribute("prob_id")))
                                .setLanguageId(Long.parseLong(run.getAttribute("lang_id")))
                                .setStatus(run.getAttribute("status"))
                                .setTime(Long.parseLong(run.getAttribute("time")))
                                .setUserId(Long.parseLong(run.getAttribute("user_id"))));
            }
        }


        return result;
    }

    public LocalDateTime parseDate(String attribute) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(getAttributeFromRunLogTag(attribute), formatter);
    }

    public LocalDateTime getStartTime() {
        return parseDate(START_TIME_TAG);
    }

    public LocalDateTime getEndTime() {
        return parseDate(STOP_TIME_TAG);
    }

    public Long getContestId() {
        return Long.parseLong(getAttributeFromRunLogTag(CONTEST_ID_TAG));
    }
}
