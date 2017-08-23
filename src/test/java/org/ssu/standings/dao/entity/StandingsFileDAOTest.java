package org.ssu.standings.dao.entity;

import org.junit.Assert;
import org.junit.Test;

public class StandingsFileDAOTest {
    @Test
    public void equals() throws Exception {
        StandingsFileDAO standingsFile = new StandingsFileDAO.Builder().withContestId(1L).withId(1L).withIsFrozen(false).withLink("dummy link").build();

        StandingsFileDAO equals = new StandingsFileDAO.Builder().withContestId(1L).withId(1L).withIsFrozen(false).withLink("dummy link").build();

        StandingsFileDAO differentContestId = new StandingsFileDAO.Builder().withContestId(12L).withId(1L).withIsFrozen(false).withLink("dummy link").build();
        StandingsFileDAO contestIdIsNull = new StandingsFileDAO.Builder().withId(1L).withIsFrozen(false).withLink("dummy link").build();

        StandingsFileDAO differentId = new StandingsFileDAO.Builder().withContestId(1L).withId(12L).withIsFrozen(false).withLink("dummy link").build();
        StandingsFileDAO idIsNull = new StandingsFileDAO.Builder().withContestId(1L).withIsFrozen(false).withLink("dummy link").build();

        StandingsFileDAO differentFrozen = new StandingsFileDAO.Builder().withContestId(1L).withId(1L).withIsFrozen(true).withLink("dummy link").build();
        StandingsFileDAO frozenIsNull = new StandingsFileDAO.Builder().withContestId(1L).withId(1L).withLink("dummy link").build();

        StandingsFileDAO differentLink = new StandingsFileDAO.Builder().withContestId(1L).withId(1L).withIsFrozen(false).withLink("dummy link test").build();
        StandingsFileDAO inkIsNull = new StandingsFileDAO.Builder().withContestId(1L).withId(1L).withIsFrozen(false).build();

        StandingsFileDAO nullObject = null;

        Assert.assertEquals(standingsFile, equals);
        Assert.assertNotEquals(standingsFile, differentContestId);
        Assert.assertNotEquals(standingsFile, contestIdIsNull);

        Assert.assertNotEquals(standingsFile, differentId);
        Assert.assertNotEquals(standingsFile, idIsNull);

        Assert.assertNotEquals(standingsFile, differentFrozen);
        Assert.assertNotEquals(standingsFile, frozenIsNull);

        Assert.assertNotEquals(standingsFile, differentLink);
        Assert.assertNotEquals(standingsFile, inkIsNull);

        Assert.assertNotEquals(standingsFile, nullObject);
    }

}