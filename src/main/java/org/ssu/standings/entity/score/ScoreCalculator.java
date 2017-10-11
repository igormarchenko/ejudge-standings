package org.ssu.standings.entity.score;

import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.List;

public interface ScoreCalculator {
    Integer tries(List<SubmissionNode> submissions);
    Long acceptedTime(List<SubmissionNode> submissions);
    SubmissionStatus status(List<SubmissionNode> submissions);
    Long penalty(List<SubmissionNode> submissions);
    Boolean isProblemSolved(List<SubmissionNode> submissions);
}
