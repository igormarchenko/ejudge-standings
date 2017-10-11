package org.ssu.standings.entity.score;

import org.springframework.stereotype.Component;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AcmScoreCalculator implements  ScoreCalculator{
    @Override
    public Integer tries(List<SubmissionNode> submissions) {
        Map<Integer, SubmissionStatus> collect = IntStream.range(0, submissions.size())
                .boxed()
                .collect(Collectors.toMap(index -> index, index -> submissions.get(index).getStatus()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(item -> item.getKey() + 1, item -> item.getValue()));


        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == SubmissionStatus.OK)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(collect.size());
    }

    @Override
    public Long acceptedTime(List<SubmissionNode> submissions) {
        Long seconds = submissions
                .stream()
                .filter(submission -> submission.getStatus() == SubmissionStatus.OK)
                .map(SubmissionNode::getTime)
                .sorted()
                .findFirst()
                .orElse(0L);
        return (seconds > 0) ? (seconds + 60 - seconds % 60) / 60 : 0L;
    }

    @Override
    public SubmissionStatus status(List<SubmissionNode> submissions) {
        if (isProblemSolved(submissions)) return SubmissionStatus.OK;
        return submissions.stream()
                .filter(submit -> submit.getStatus() != SubmissionStatus.CE)
                .map(SubmissionNode::getStatus)
                .reduce((a, b) -> b)
                .orElse(SubmissionStatus.EMPTY);
    }

    @Override
    public Long penalty(List<SubmissionNode> submissions) {
        return isProblemSolved(submissions) ? (Math.max(tries(submissions), 1) - 1) * 20L + acceptedTime(submissions) : 0L;
    }

    @Override
    public Boolean isProblemSolved(List<SubmissionNode> submissions) {
        return submissions.stream().anyMatch(submit -> submit.getStatus() == SubmissionStatus.OK);
    }
}
