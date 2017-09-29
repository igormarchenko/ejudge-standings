package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;

public class StandingsFileDAOTest {
    @Test
    public void equalsTest() throws Exception {
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


    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StandingsFileDAO standingsFileDAO = new StandingsFileDAO.Builder().withContestId(1L).withId(2L).withIsFrozen(true).withLink("Link").build();


        String actualJson = mapper.writeValueAsString(standingsFileDAO);
        Assert.assertThat(mapper.readTree(actualJson).size(), is(4));

        Assert.assertNotNull(mapper.readTree(actualJson).get("id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("link"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("contest_id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("frozen"));

        Assert.assertThat(mapper.readTree(actualJson).get("id").asInt(), is(2));
        Assert.assertThat(mapper.readTree(actualJson).get("link").asText(), is("Link"));
        Assert.assertThat(mapper.readTree(actualJson).get("contest_id").asInt(), is(1));
        Assert.assertThat(mapper.readTree(actualJson).get("frozen").asText(), is("true"));
    }

}