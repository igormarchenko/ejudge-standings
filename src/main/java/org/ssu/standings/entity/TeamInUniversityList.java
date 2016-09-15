package org.ssu.standings.entity;

import java.util.Map;

public class TeamInUniversityList {
    private static Map<String, University> teamUniversity;

    public static Map<String, University> getTeamUniversity() {
        return teamUniversity;
    }

    public static void setTeamUniversity(Map<String, University> teamUniversity) {
        TeamInUniversityList.teamUniversity = teamUniversity;
    }

    public static University teamUniversity(String team) {
        if(teamUniversity.containsKey(team))
            return teamUniversity.get(team);
        return new University().setType("Classic").setName("Empty");
    }
}
