package org.ssu.standings.entity;

import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.List;

public class ContestSubmissionsChanges {
    private List<SubmissionNode> newSubmissions = new ArrayList<>();
    private List<SubmissionNode> changedSubmissions = new ArrayList<>();

    public ContestSubmissionsChanges(List<SubmissionNode> newSubmissions, List<SubmissionNode> rejudgedSubmissions) {
        addNewSubmission(newSubmissions);
        addRejudgedSubmission(rejudgedSubmissions);
    }

    public List<SubmissionNode> getNewSubmissions() {
        return newSubmissions;
    }

    public List<SubmissionNode> getChangedSubmissions() {
        return changedSubmissions;
    }

    public void addNewSubmission(SubmissionNode submission) {
        newSubmissions.add(submission);
    }

    public void addNewSubmission(List<SubmissionNode> submissions) {
        submissions.forEach(submission -> newSubmissions.add(submission));
    }

    public void addRejudgedSubmission(SubmissionNode submission) {
        changedSubmissions.add(submission);
    }

    public void addRejudgedSubmission(List<SubmissionNode> submissions) {
        submissions.forEach(submission -> changedSubmissions.add(submission));
    }

    public Boolean hasChanges() {
        return !changedSubmissions.isEmpty() || !newSubmissions.isEmpty();
    }
}
