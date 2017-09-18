package org.ssu.standings.entity;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;

public class TeamResultsInBaylorTest {

    @Test
    public void createBaylorteamWithBuilder() throws NoSuchFieldException, IllegalAccessException {
        TeamResultsInBaylor team = new TeamResultsInBaylor.Builder()
                .withLastProblemTime(10L)
                .withProblemsSolved(4)
                .withRank(1)
                .withTeamId(1L)
                .withTotalTime(11L)
                .withTeamName("test team")
                .build();

        Class teamClass = TeamResultsInBaylor.class;
        Field lastProblemSolvedTimeField = teamClass.getDeclaredField("lastProblemTime");
        lastProblemSolvedTimeField.setAccessible(true);
        Long lastProblemSolvedTime = (Long)lastProblemSolvedTimeField.get(team);


        Field problemsSolvedField = teamClass.getDeclaredField("problemsSolved");
        problemsSolvedField.setAccessible(true);
        Integer problemsSolved = (Integer)problemsSolvedField.get(team);

        Field rankField = teamClass.getDeclaredField("rank");
        rankField.setAccessible(true);
        Integer rank = (Integer) rankField.get(team);

        Field teamIdField = teamClass.getDeclaredField("teamId");
        teamIdField.setAccessible(true);
        Long teamId = (Long)teamIdField.get(team);

        Field totalTimeField = teamClass.getDeclaredField("totalTime");
        totalTimeField.setAccessible(true);
        Long totalTime = (Long)totalTimeField.get(team);

        Field teamNameField = teamClass.getDeclaredField("teamName");
        teamNameField.setAccessible(true);
        String teamName = (String)teamNameField.get(team);

        Assert.assertThat(lastProblemSolvedTime, is(10L));
        Assert.assertThat(problemsSolved, is(4));
        Assert.assertThat(rank, is(1));
        Assert.assertThat(teamId, is(1L));
        Assert.assertThat(totalTime, is(11L));
        Assert.assertThat(teamName, is("test team"));
    }

}
