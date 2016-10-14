package org.ssu.standings.parser;

import org.w3c.dom.NodeList;

import java.io.File;

public class SettingsParser extends Parser {
    private static final String STANDINGS_FILES = "file";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String BAYLOR_TEAMS_FILE = "baylor-teams-file";

    public SettingsParser(File file) {
        super(file);
    }

    public SettingsParser(String uri) {
        super(uri);
    }

    public NodeList getStandingsFiles() {
        return getCurrentNode(STANDINGS_FILES);
    }

    public String getLogin() {
        return getDataStringFromXML(LOGIN);
    }

    public String getPassword() {
        return getDataStringFromXML(PASSWORD);
    }

    public String getBaylorTeamsFile() {
        return getCurrentNode(BAYLOR_TEAMS_FILE).item(0).getAttributes().item(0).getNodeValue();
    }

    private String getDataStringFromXML(String attribute) {
        return getChildNodes(attribute).item(0).getNodeValue();
    }
}
