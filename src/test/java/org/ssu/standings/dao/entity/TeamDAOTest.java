package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;

public class TeamDAOTest {
    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TeamDAO teamDAO = new TeamDAO.Builder().withName("Test name").withId(1L).withUniversity(new UniversityDAO.Builder().build()).build();

        String actualJson = mapper.writeValueAsString(teamDAO);
        Assert.assertThat(mapper.readTree(actualJson).size(), is(3));

        Assert.assertNotNull(mapper.readTree(actualJson).get("id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("name"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("university"));

        Assert.assertThat(mapper.readTree(actualJson).get("id").asInt(), is(1));
        Assert.assertThat(mapper.readTree(actualJson).get("name").asText(), is("Test name"));
    }
}