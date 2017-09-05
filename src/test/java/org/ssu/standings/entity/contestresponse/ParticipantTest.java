package org.ssu.standings.entity.contestresponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.parser.entity.ParticipantNode;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParticipantTest {
    private UniversityDAO university = new UniversityDAO.Builder().withId(1L).withName("Test uni").withRegion("South").withType("Classical").build();

    private ParticipantNode participantNode;
    private ParticipantNode participantNodeWithNullName;

    @Before
    public void setUp() {
        participantNode = mock(ParticipantNode.class);
        participantNodeWithNullName = mock(ParticipantNode.class);

        when(participantNode.getId()).thenReturn(12L);
        when(participantNode.getName()).thenReturn("Test username");

        when(participantNodeWithNullName.getId()).thenReturn(12L);
        when(participantNodeWithNullName.getName()).thenReturn(null);
    }
    @Test
    public void getIdTest() throws Exception {
        Participant participant = new Participant.Builder(participantNode, university).build();
        Assert.assertThat(participant.getId(), is(12L));
    }

    @Test
    public void getNameTest() throws Exception {
        Participant participant = new Participant.Builder(participantNode, university).build();
        Assert.assertThat(participant.getName(), is("Test username"));

        participant = new Participant.Builder(participantNodeWithNullName, university).build();
        Assert.assertThat(participant.getName(), is(String.format("team%d", participant.getId())));
    }

    @Test
    public void cloneTest() throws Exception {
        Participant participant = new Participant.Builder(participantNode, university).build();
        Participant clone = participant.clone();
        Assert.assertNotSame(participant.getUniversity(), clone.getUniversity());
    }

}