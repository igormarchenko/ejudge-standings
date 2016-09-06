package org.ssu.standings.parser;

import org.ssu.standings.utils.XmlStream;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamParser extends Parser {

    public static final String TEAM_TAG = "team";
    public static final String TEAM_NAME_ATTRIBUTE = "name";
    public static final String UNIVERSITY_ATTRIBUTE = "university";

    public TeamParser(File file) {
        super(file);
    }

    public TeamParser(String uri) {
        super(uri);
    }

    public Map<String, String> teamList() {
        return XmlStream.of(getCurrentNode(TEAM_TAG))
                .collect(Collectors.toMap(item -> getAttributeValue(item, TEAM_NAME_ATTRIBUTE),
                        item -> getAttributeValue(item, UNIVERSITY_ATTRIBUTE),
                        (team1, team2) -> team2));
    }
}
