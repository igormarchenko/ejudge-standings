package org.ssu.standings.entity.score;

import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.List;

public class ScoreCalculatorFactory {
    public static ContestType selectCalculator(List<SubmissionNode> submission) {
        if(submission.stream().anyMatch(submit -> submit.getScore() != null))
            return ContestType.KIROV;
        else
            return ContestType.ACM;
    }
}
