package org.ssu.standings.entity.score;

import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.Comparator;
import java.util.List;

public class KirovScoreCalculator implements ScoreCalculator{
    @Override
    public Integer tries(List<SubmissionNode> submissions) {
        return Long.valueOf(submissions.stream().filter(submit -> submit.getStatus() != SubmissionStatus.CE).count()).intValue();
    }

    @Override
    public Long acceptedTime(List<SubmissionNode> submissions) {
        Long seconds = submissions.stream().sorted(Comparator.comparingLong(SubmissionNode::getScore)).findFirst().map(SubmissionNode::getTime).orElse(0L);
        return (seconds > 0) ? (seconds + 60 - seconds % 60) / 60 : 0L;
    }

    @Override
    public SubmissionStatus status(List<SubmissionNode> submissions) {
        if(isProblemSolved(submissions)) return SubmissionStatus.OK;
        return submissions.stream()
                .filter(submit -> submit.getStatus() != SubmissionStatus.CE)
                .map(SubmissionNode::getStatus)
                .reduce((a, b) -> b)
                .orElse(SubmissionStatus.EMPTY);
    }

    @Override
    public Long penalty(List<SubmissionNode> submissions) {
        return submissions.stream().mapToLong(SubmissionNode::getScore).max().orElse(0L);
    }

    @Override
    public Boolean isProblemSolved(List<SubmissionNode> submissions) {
        return submissions.stream().anyMatch(submit -> submit.getStatus() == SubmissionStatus.OK);
    }
}
