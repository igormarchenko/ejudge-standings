package org.ssu.standings.entity.score;

import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.List;

public class ScoreCalculatorFactory {
    public static ContestType selectCalculator(List<SubmissionNode> submission) {
        if(submission.stream().anyMatch(submit -> submit.getScore() != null && submit.getScore().compareTo(0L) > 0))
            return ContestType.KIROV;
        else
            return ContestType.ACM;
    }
}
