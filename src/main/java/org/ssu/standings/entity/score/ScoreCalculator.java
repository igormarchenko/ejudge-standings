package org.ssu.standings.entity.score;

import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.List;

public interface ScoreCalculator {
    Integer tries(List<SubmissionNode> submissions);

    Long acceptedTime(List<SubmissionNode> submissions);

    Long penalty(List<SubmissionNode> submissions);

    default SubmissionStatus status(List<SubmissionNode> submissions) {
        if (isProblemSolved(submissions)) return SubmissionStatus.OK;
        return submissions.stream()
                .filter(submit -> submit.getStatus() != SubmissionStatus.CE)
                .map(SubmissionNode::getStatus)
                .reduce((a, b) -> b)
                .orElse(SubmissionStatus.EMPTY);
    }

    default Boolean isProblemSolved(List<SubmissionNode> submissions) {
        return submissions.stream().anyMatch(submit -> submit.getStatus() == SubmissionStatus.OK);
    }
}
