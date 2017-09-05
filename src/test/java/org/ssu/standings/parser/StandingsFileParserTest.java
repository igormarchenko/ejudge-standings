package org.ssu.standings.parser;

import org.junit.Assert;
import org.junit.Test;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.ContestNode;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;

public class StandingsFileParserTest {
    private String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<runlog contest_id=\"212\" start_time=\"2017/03/25 03:57:10\" duration = \"18000\" stop_time=\"2017/03/25 08:57:10\" current_time=\"2017/05/06 06:09:08\" fog_time=\"7200\" unfog_time=\"72000000\">\n" +
            "<name>ACM ICPC 2017 Stage I (25-03-2017)</name>\n" +
            "<users>\n" +
            "<user id=\"16\" name=\"KNEU_Binary\"/>\n" +
            "<user id=\"17\" name=\"KISIT_DEV\"/>\n" +
            "<user id=\"18\" name=\"TLKPI_BUGS\"/>\n" +
            "<user id=\"19\" name=\"TLKPI_LOREM_IPSUM\"/>\n" +
            "<user id=\"20\" name=\"KNEU_VARD\"/>\n" +
            "<user id=\"21\" name=\"KNEU_Dreamteam\"/>\n" +
            "<user id=\"22\" name=\"KISIT_code_villain\"/>\n" +
            "<user id=\"23\" name=\"KISIT_Dwa_bayta\"/>\n" +
            "</users>\n"+
            "<problems>\n" +
            "<problem id=\"1\" short_name=\"A\" long_name=\"task 1\"/>\n" +
            "<problem id=\"2\" short_name=\"B\" long_name=\"task 2\"/>\n" +
            "<problem id=\"3\" short_name=\"C\" long_name=\"task 3\"/>\n" +
            "<problem id=\"4\" short_name=\"D\" long_name=\"task 4\"/>\n" +
            "</problems>\n" +
            "<languages>\n" +
            "<language id=\"1\" short_name=\"fpc\" long_name=\"Free Pascal 2.6.4+dfsg-4\"/>\n" +
            "<language id=\"2\" short_name=\"gcc\" long_name=\"GNU C 4.9.2\"/>\n" +
            "<language id=\"3\" short_name=\"g++\" long_name=\"GNU C++ 4.9.2\"/>\n" +
            "<language id=\"6\" short_name=\"gfortran\" long_name=\"GNU Fortran 4.9.2-10)\"/>\n" +
            "<language id=\"13\" short_name=\"python\" long_name=\"Python 2.7.9\"/>\n" +
            "</languages>\n" +
            "<runs>\n" +
            "<run run_id=\"24\" time=\"13\" run_uuid=\"886355de-c5d9-4efc-a347-15f161fd0799\" status=\"SE\" user_id=\"16\" prob_id=\"1\" lang_id=\"3\" test=\"0\" nsec=\"673947000\" passed_mode=\"yes\"/>\n" +
            "<run run_id=\"25\" time=\"24\" run_uuid=\"9ce4c377-cd8c-470f-8099-79ddae86d828\" status=\"WA\" user_id=\"17\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"394133000\" passed_mode=\"yes\"/>\n" +
            "<run run_id=\"26\" time=\"30\" run_uuid=\"8c99b7ba-645a-4fc2-8c23-f3fc6b7922eb\" status=\"OK\" user_id=\"34\" prob_id=\"1\" lang_id=\"3\" test=\"12\" nsec=\"399036000\" passed_mode=\"yes\"/>\n" +
            "<run run_id=\"27\" time=\"33\" run_uuid=\"4261b31e-69c3-4061-b38a-c5a57b48c47f\" status=\"WA\" user_id=\"18\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"138566000\" passed_mode=\"yes\"/>\n" +
            "<run run_id=\"28\" time=\"42\" run_uuid=\"0726b0d8-69a8-44c6-8b91-b83bb1e41874\" status=\"WA\" user_id=\"43\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"102742000\" passed_mode=\"yes\"/>\n" +
            "<run run_id=\"29\" time=\"51\" run_uuid=\"25a79043-d404-40ce-b757-fb25fcedee44\" status=\"CE\" user_id=\"47\" prob_id=\"1\" lang_id=\"3\" test=\"0\" nsec=\"964440000\" passed_mode=\"yes\"/>\n" +
            "<run run_id=\"30\" time=\"55\" run_uuid=\"7ca22388-5907-4ea8-80b9-308e57e66feb\" status=\"CE\" user_id=\"23\" prob_id=\"1\" lang_id=\"3\" test=\"0\" nsec=\"42306000\" passed_mode=\"yes\"/>\n" +
            "<run run_id=\"31\" time=\"56\" run_uuid=\"7fb57089-435d-45f5-94d2-b195e139a0ce\" status=\"WA\" user_id=\"48\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"86718000\" passed_mode=\"yes\"/>\n" +
            "</runs>\n" +
            "</runlog>\n";

    @Test
    public void parseEmptyFileTest() throws Exception {
        Optional<ContestNode> result = new StandingsFileParser().parse("");
        Assert.assertThat(result, is(Optional.empty()));
    }

    @Test
    public void parseEmptyFileWithHeaderTest() throws Exception {
        Optional<ContestNode> result = new StandingsFileParser().parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        Assert.assertThat(result, is(Optional.empty()));
    }

    @Test
    public void parseFileTest() throws Exception {
        Optional<ContestNode> result = new StandingsFileParser().parse(input);
        Assert.assertThat(result.isPresent(), is(true));
        ContestNode contest = result.get();

        Assert.assertThat(contest.getContestId(), is(212L));
        Assert.assertThat(contest.getStartTime(), is(LocalDateTime.of(2017, 3, 25, 3, 57, 10)));
        Assert.assertThat(contest.getStopTime(), is(LocalDateTime.of(2017, 3, 25, 8, 57, 10)));
        Assert.assertThat(contest.getName(), is("ACM ICPC 2017 Stage I (25-03-2017)"));
        Assert.assertThat(contest.getDuration(), is(5 * 60 * 60L));
        Assert.assertThat(contest.getCurrentTime(), is(LocalDateTime.of(2017, 5, 6, 6, 9, 8)));
        Assert.assertThat(contest.getFogTime(), is(7200L));
        Assert.assertThat(contest.getUnfogTime(), is(72000000L));

        Assert.assertNotNull(contest.getParticipants());
        Assert.assertThat(contest.getParticipants().size(), is(8));
        Assert.assertThat(contest.getParticipants().get(0).getId(), is(16L));
        Assert.assertThat(contest.getParticipants().get(0).getName(), is("KNEU_Binary"));

        Assert.assertNotNull(contest.getLanguages());
        Assert.assertThat(contest.getLanguages().size(), is(5));
        Assert.assertThat(contest.getLanguages().get(0).getId(), is(1L));
        Assert.assertThat(contest.getLanguages().get(0).getLongName(), is("Free Pascal 2.6.4+dfsg-4"));
        Assert.assertThat(contest.getLanguages().get(0).getShortName(), is("fpc"));

        Assert.assertNotNull(contest.getProblems());
        Assert.assertThat(contest.getProblems().size(), is(4));
        Assert.assertThat(contest.getProblems().get(0).getId(), is(1L));
        Assert.assertThat(contest.getProblems().get(0).getLongName(), is("task 1"));
        Assert.assertThat(contest.getProblems().get(0).getShortName(), is("A"));

        Assert.assertNotNull(contest.getSubmissions());
        Assert.assertThat(contest.getSubmissions().size(), is(8));
        Assert.assertThat(contest.getSubmissions().get(0).getId(), is(24L));
        Assert.assertThat(contest.getSubmissions().get(0).getTime(), is(13L));
        Assert.assertThat(contest.getSubmissions().get(0).getRunUuid(), is("886355de-c5d9-4efc-a347-15f161fd0799"));
        Assert.assertThat(contest.getSubmissions().get(0).getStatus(), is(SubmissionStatus.SE));
        Assert.assertThat(contest.getSubmissions().get(0).getUserId(), is(16L));
        Assert.assertThat(contest.getSubmissions().get(0).getProblemId(), is(1L));
        Assert.assertThat(contest.getSubmissions().get(0).getLanguageId(), is(3L));
        Assert.assertThat(contest.getSubmissions().get(0).getTest(), is(0L));

        Assert.assertThat(contest.getSubmissions().get(0).getNsec(), is(673947000L));
        Assert.assertThat(contest.getSubmissions().get(0).getPassedMode(), is("yes"));
    }

    @Test
    public void parseFileWithoutDurationTag() {
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<runlog contest_id=\"212\" start_time=\"2017/03/25 03:57:10\" stop_time=\"2017/03/25 08:57:10\" current_time=\"2017/05/06 06:09:08\" fog_time=\"7200\" unfog_time=\"72000000\">\n" +
                "<name>ACM ICPC 2017 Stage I (25-03-2017)</name>\n" +
                "</runlog>";

        Optional<ContestNode> result = new StandingsFileParser().parse(inputData);

        Assert.assertThat(result.isPresent(), is(true));
        ContestNode contest = result.get();

        Assert.assertThat(contest.getDuration(), is(5 * 60 * 60L));
    }

    @Test
    public void parseFileWithoutStopTimeTag() {
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<runlog contest_id=\"212\" start_time=\"2017/03/25 03:57:10\" duration = \"18000\" current_time=\"2017/05/06 06:09:08\" fog_time=\"7200\" unfog_time=\"72000000\">\n" +
                "<name>ACM ICPC 2017 Stage I (25-03-2017)</name>\n" +
                "</runlog>";

        Optional<ContestNode> result = new StandingsFileParser().parse(inputData);

        Assert.assertThat(result.isPresent(), is(true));
        ContestNode contest = result.get();

        Assert.assertThat(contest.getStopTime(), is(LocalDateTime.of(2017, 3, 25, 8, 57, 10)));
    }
}