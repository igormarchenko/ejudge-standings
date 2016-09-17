package org.ssu.standings.utils;

import org.ssu.standings.entity.StandingsTableInfo;
import org.ssu.standings.entity.University;
import org.ssu.standings.parser.SettingsParser;
import org.ssu.standings.parser.TeamParser;
import org.ssu.standings.parser.UniversityParser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Settings {
    private static final String STANDINGS_FILE_PATH = "value";
    private static final String CONTEST_ID = "contest";
    private static final String IS_FINAL = "final";
    private static final String SETTINGS_FILE = "sources.xml";
    private static SettingsParser parser;

    static {
        parser = new SettingsParser(new File(Settings.class.getClassLoader().getResource(SETTINGS_FILE).getFile()));
        Map<String, University> universityList = new UniversityParser(parser.getUniversityTypeFile()).universityInfo();
        TeamInUniversityList.setTeamUniversity(new TeamParser(parser.getTeamToUniversityMappingFile())
                .teamList()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> universityList.get(item.getValue()))));
    }

    public static List<StandingsTableInfo> getStandingsFiles() {
        return XmlStream.of(parser.getStandingsFiles())
                .map(item -> new StandingsTableInfo()
                        .setLink(parser.getAttributeValue(item, STANDINGS_FILE_PATH))
                        .setContestId(Long.parseLong(parser.getAttributeValue(item, CONTEST_ID)))
                        .setFinal(Boolean.parseBoolean(parser.getAttributeValue(item, IS_FINAL))))
                .collect(Collectors.toList());
    }

    public static String getBaylorTeamsFile() {
        return parser.getBaylorTeamsFile();
    }

    public static String getLogin() {
        return parser.getLogin();
    }

    public static String getPassword() {
        return parser.getPassword();
    }
}
