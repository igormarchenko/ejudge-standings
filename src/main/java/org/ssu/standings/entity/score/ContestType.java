package org.ssu.standings.entity.score;

import com.fasterxml.jackson.annotation.JsonValue;
import org.ssu.standings.entity.contestresponse.ParticipantResult;

import java.util.Comparator;

public enum ContestType {
    ACM("acm", new AcmScoreCalculator(), Comparator.comparing(ParticipantResult::solvedProblems).reversed().thenComparing(ParticipantResult::getPenalty)),
    KIROV("kirov", new KirovScoreCalculator(), Comparator.comparing(ParticipantResult::solvedProblems).thenComparing(ParticipantResult::getPenalty).reversed());

    ContestType(String type, ScoreCalculator calculator, Comparator comparator) {
        this.type = type;
        this.calculator = calculator;
        this.comparator = comparator;
    }

    @JsonValue
    private String type;
    private ScoreCalculator calculator;
    private Comparator comparator;

    public String getType() {
        return type;
    }

    public ScoreCalculator getCalculator() {
        return calculator;
    }

    public Comparator getComparator() {
        return comparator;
    }
}
