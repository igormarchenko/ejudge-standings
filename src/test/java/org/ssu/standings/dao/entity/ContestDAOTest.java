package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;

public class ContestDAOTest {
    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ContestDAO contestDAO = new ContestDAO.Builder().withName("Test").withIsFinal(true).withId(1L).withStandingsFiles(new ArrayList<>()).build();

        String actualJson = mapper.writeValueAsString(contestDAO);
        Assert.assertThat(mapper.readTree(actualJson).size(), is(4));

        Assert.assertNotNull(mapper.readTree(actualJson).get("id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("name"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("is_final"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("standingsFiles"));

        Assert.assertThat(mapper.readTree(actualJson).get("id").asInt(), is(1));
        Assert.assertThat(mapper.readTree(actualJson).get("name").asText(), is("Test"));
        Assert.assertThat(mapper.readTree(actualJson).get("is_final").asBoolean(), is(true));
    }

}