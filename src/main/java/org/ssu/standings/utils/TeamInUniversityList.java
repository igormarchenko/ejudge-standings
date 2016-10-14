package org.ssu.standings.utils;


import org.ssu.standings.entity.UniversityEntity;

import java.util.Map;

public class TeamInUniversityList {
    private static final String DEFAULT_TYPE = "-";
    private static final String DEFAULT_NAME = "-";
    private static final String DEFAULT_REGION = "-";

    private static Map<String, UniversityEntity> teamUniversity;

    public static void setTeamUniversity(Map<String, UniversityEntity> teamUniversity) {
        TeamInUniversityList.teamUniversity = teamUniversity;
    }

    public static UniversityEntity universityForTeam(String team) {
        return (teamUniversity.containsKey(team.trim())) ?
                teamUniversity.get(team.trim()) :
                new UniversityEntity()
                        .setType(DEFAULT_TYPE)
                        .setName(DEFAULT_NAME)
                        .setRegion(DEFAULT_REGION);
    }
}
