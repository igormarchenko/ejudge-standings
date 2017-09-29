package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotSame;

public class UniversityDAOTest {
    @Test
    public void cloneTest() throws Exception {
        UniversityDAO universityDAO = new UniversityDAO.Builder().withId(1L).withName("name").withRegion("region").withType("type").build();
        UniversityDAO copy = universityDAO.clone();

        assertNotSame(universityDAO, copy);
    }

    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UniversityDAO universityDAO = new UniversityDAO.Builder().withId(1L).withName("Test").withRegion("Test region").withType("Test type").build();

        String actualJson = mapper.writeValueAsString(universityDAO);
        Assert.assertThat(mapper.readTree(actualJson).size(), is(4));

        Assert.assertNotNull(mapper.readTree(actualJson).get("id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("name"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("type"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("region"));

        Assert.assertThat(mapper.readTree(actualJson).get("id").asInt(), is(1));
        Assert.assertThat(mapper.readTree(actualJson).get("name").asText(), is("Test"));
        Assert.assertThat(mapper.readTree(actualJson).get("region").asText(), is("Test region"));
        Assert.assertThat(mapper.readTree(actualJson).get("type").asText(), is("Test type"));
    }
}