package org.ssu.standings.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class ContestSubmissionsChangesTest {
    private List<SubmissionNode> newSubmissions;
    private List<SubmissionNode> rejudgedSubmissions;

    @Before
    public void init() {
        newSubmissions = Arrays.asList(new SubmissionNode.Builder().build(), new SubmissionNode.Builder().build(), new SubmissionNode.Builder().build());
        rejudgedSubmissions = Arrays.asList(new SubmissionNode.Builder().build(), new SubmissionNode.Builder().build());
    }


    @Test
    public void getNewSubmissions() throws Exception {
        ContestSubmissionsChanges changes = new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
        Assert.assertThat(changes.getNewSubmissions().size(), is(3));

        changes = new ContestSubmissionsChanges(new ArrayList<>(), rejudgedSubmissions);
        Assert.assertThat(changes.getNewSubmissions().size(), is(0));
    }

    @Test
    public void getChangedSubmissions() throws Exception {
        ContestSubmissionsChanges changes = new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
        Assert.assertThat(changes.getChangedSubmissions().size(), is(2));

        changes = new ContestSubmissionsChanges(newSubmissions, new ArrayList<>());
        Assert.assertThat(changes.getChangedSubmissions().size(), is(0));
    }

    @Test
    public void addNewSubmission() throws Exception {
        ContestSubmissionsChanges changes = new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
        changes.addNewSubmission(new SubmissionNode.Builder().build());
        Assert.assertThat(changes.getNewSubmissions().size(), is(4));

        changes = new ContestSubmissionsChanges(new ArrayList<>(), new ArrayList<>());
        changes.addNewSubmission(new SubmissionNode.Builder().build());
        Assert.assertThat(changes.getNewSubmissions().size(), is(1));
    }

    @Test
    public void addNewSubmissionList() throws Exception {
        ContestSubmissionsChanges changes = new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
        changes.addNewSubmission(newSubmissions);
        Assert.assertThat(changes.getNewSubmissions().size(), is(6));

        changes = new ContestSubmissionsChanges(new ArrayList<>(), new ArrayList<>());
        changes.addNewSubmission(newSubmissions);
        Assert.assertThat(changes.getNewSubmissions().size(), is(3));
    }

    @Test
    public void addRejudgedSubmission() throws Exception {
        ContestSubmissionsChanges changes = new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
        changes.addRejudgedSubmission(new SubmissionNode.Builder().build());
        Assert.assertThat(changes.getChangedSubmissions().size(), is(3));

        changes = new ContestSubmissionsChanges(new ArrayList<>(), new ArrayList<>());
        changes.addRejudgedSubmission(new SubmissionNode.Builder().build());
        Assert.assertThat(changes.getChangedSubmissions().size(), is(1));
    }

    @Test
    public void addRejudgedSubmissionList() throws Exception {
        ContestSubmissionsChanges changes = new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
        changes.addRejudgedSubmission(rejudgedSubmissions);
        Assert.assertThat(changes.getChangedSubmissions().size(), is(4));

        changes = new ContestSubmissionsChanges(new ArrayList<>(), new ArrayList<>());
        changes.addRejudgedSubmission(rejudgedSubmissions);
        Assert.assertThat(changes.getChangedSubmissions().size(), is(2));
    }

    @Test
    public void hasChanges() throws Exception {
        ContestSubmissionsChanges changes = new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
        Assert.assertThat(changes.hasChanges(), is(true));

        changes = new ContestSubmissionsChanges(new ArrayList<>(), new ArrayList<>());
        Assert.assertThat(changes.hasChanges(), is(false));
    }

}