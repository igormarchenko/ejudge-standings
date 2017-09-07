package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;

public class ParticipantUpdatesTest {
    ParticipantUpdates updates = new ParticipantUpdates(1L, null, 25, 12);

    @Test
    public void getTeamId() throws Exception {
        Assert.assertThat(updates.getTeamId(), is(1L));
    }

    @Test
    public void getPreviousPlace() throws Exception {
        Assert.assertThat(updates.getPreviousPlace(), is(25));
    }

    @Test
    public void getCurrentPlace() throws Exception {
        Assert.assertThat(updates.getCurrentPlace(), is(12));
    }

    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String actualJson = mapper.writeValueAsString(updates);

        Assert.assertThat(mapper.readTree(actualJson).size(), is(4));

        Assert.assertNotNull(mapper.readTree(actualJson).get("id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("result"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("previousPlace"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("currentPlace"));


        Assert.assertThat(mapper.readTree(actualJson).get("id").asInt(), is(1));
        Assert.assertThat(mapper.readTree(actualJson).get("previousPlace").asInt(), is(25));
        Assert.assertThat(mapper.readTree(actualJson).get("currentPlace").asInt(),is(12));
    }
}