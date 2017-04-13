package org.ssu.standings.utils;


import org.ssu.standings.dao.entity.University;

import java.util.Map;

public class TeamInUniversityList {
    private static final String DEFAULT_TYPE = "-";
    private static final String DEFAULT_NAME = "-";
    private static final String DEFAULT_REGION = "-";

    private static Map<String, University> teamUniversity;

    public static void setTeamUniversity(Map<String, University> teamUniversity) {
        TeamInUniversityList.teamUniversity = teamUniversity;
    }

    public static University universityForTeam(String team) {
        return (teamUniversity.containsKey(team.trim())) ?
                teamUniversity.get(team.trim()) :
                new University()
                        .setType(DEFAULT_TYPE)
                        .setName(DEFAULT_NAME)
                        .setRegion(DEFAULT_REGION);
    }
}
