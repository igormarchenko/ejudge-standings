package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ssu.standings.parser.entity.ProblemNode;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskTest {
    private ProblemNode taskNode;
    @Before
    public void setUp() {
        taskNode = mock(ProblemNode.class);
        when(taskNode.getLongName()).thenReturn("Long task name");
        when(taskNode.getShortName()).thenReturn("Short task name");
        when(taskNode.getId()).thenReturn(1L);
    }

    @Test
    public void getId() throws Exception {
        Task task = new Task(taskNode);
        Assert.assertThat(task.getId(), is(1L));
    }

    @Test
    public void getShortName() throws Exception {
        Task task = new Task(taskNode);
        Assert.assertThat(task.getShortName(), is("Short task name"));
    }

    @Test
    public void getLongName() throws Exception {
        Task task = new Task(taskNode);
        Assert.assertThat(task.getLongName(), is("Long task name"));
    }

    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Task task = new Task(taskNode);

        String actualJson = mapper.writeValueAsString(task);
        Assert.assertThat(mapper.readTree(actualJson).size(), is(3));

        Assert.assertNotNull(mapper.readTree(actualJson).get("id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("shortName"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("longName"));

        Assert.assertThat(mapper.readTree(actualJson).get("id").asInt(), is(1));
        Assert.assertThat(mapper.readTree(actualJson).get("shortName").asText(), is("Short task name"));
        Assert.assertThat(mapper.readTree(actualJson).get("longName").asText(), is("Long task name"));


    }

}