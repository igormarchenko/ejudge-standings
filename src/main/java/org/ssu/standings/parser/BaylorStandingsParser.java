package org.ssu.standings.parser;

import org.w3c.dom.NodeList;

import java.io.File;

public class BaylorStandingsParser extends Parser {

    private static final String TEAM_NODE = "Standing";

    public BaylorStandingsParser(File file) {
        super(file);
    }

    public BaylorStandingsParser(String uri) {
        super(uri);
    }

    public NodeList getTeams() {
        return getCurrentNode(TEAM_NODE);
    }
}
