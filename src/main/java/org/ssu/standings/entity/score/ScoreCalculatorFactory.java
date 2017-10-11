package org.ssu.standings.entity.score;

import org.ssu.standings.parser.entity.SubmissionNode;

public class ScoreCalculatorFactory {
    public static ScoreCalculator selectCalculator(SubmissionNode submissions) {
        return new AcmScoreCalculator();
    }

}
