package org.ssu.standings.entity;

import org.junit.Assert;
import org.junit.Test;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.parser.StandingsFileParser;
import org.ssu.standings.parser.entity.ContestNode;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class ContestMergerTest {
    private List<String> input = Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<runlog contest_id=\"211\" start_time=\"2017/03/25 03:57:10\" duration = \"18000\" stop_time=\"2017/03/25 08:57:10\" current_time=\"2017/05/06 06:09:08\" fog_time=\"7200\" unfog_time=\"72000000\">\n" +
                    "<name>ACM ICPC 2017 Stage I (25-03-2017) day one</name>\n" +
                    "<users>\n" +
                    "<user id=\"16\" name=\"KNEU_Binary\"/>\n" +
                    "<user id=\"17\" name=\"KISIT_DEV\"/>\n" +
                    "<user id=\"18\" name=\"TLKPI_BUGS\"/>\n" +
                    "</users>\n" +
                    "<problems>\n" +
                    "<problem id=\"1\" short_name=\"A\" long_name=\"task 1\"/>\n" +
                    "<problem id=\"2\" short_name=\"B\" long_name=\"task 2\"/>\n" +
                    "<problem id=\"3\" short_name=\"C\" long_name=\"task 3\"/>\n" +
                    "<problem id=\"4\" short_name=\"D\" long_name=\"task 4\"/>\n" +
                    "</problems>\n" +
                    "<languages>\n" +
                    "<language id=\"1\" short_name=\"fpc\" long_name=\"Free Pascal 2.6.4+dfsg-4\"/>\n" +
                    "<language id=\"2\" short_name=\"gcc\" long_name=\"GNU C 4.9.2\"/>\n" +
                    "</languages>\n" +
                    "<runs>\n" +
                    "<run run_id=\"24\" time=\"13\" run_uuid=\"886355de-c5d9-4efc-a347-15f161fd0799\" status=\"SE\" user_id=\"16\" prob_id=\"2\" lang_id=\"3\" test=\"0\" nsec=\"673947000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"25\" time=\"24\" run_uuid=\"9ce4c377-cd8c-470f-8099-79ddae86d828\" status=\"WA\" user_id=\"17\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"394133000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"26\" time=\"30\" run_uuid=\"8c99b7ba-645a-4fc2-8c23-f3fc6b7922eb\" status=\"OK\" user_id=\"18\" prob_id=\"2\" lang_id=\"3\" test=\"12\" nsec=\"399036000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"27\" time=\"33\" run_uuid=\"4261b31e-69c3-4061-b38a-c5a57b48c47f\" status=\"WA\" user_id=\"16\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"138566000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"28\" time=\"42\" run_uuid=\"0726b0d8-69a8-44c6-8b91-b83bb1e41874\" status=\"WA\" user_id=\"18\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"102742000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"29\" time=\"51\" run_uuid=\"25a79043-d404-40ce-b757-fb25fcedee44\" status=\"WA\" user_id=\"18\" prob_id=\"1\" lang_id=\"3\" test=\"0\" nsec=\"964440000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"30\" time=\"55\" run_uuid=\"7ca22388-5907-4ea8-80b9-308e57e66feb\" status=\"OK\" user_id=\"16\" prob_id=\"1\" lang_id=\"3\" test=\"0\" nsec=\"42306000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"31\" time=\"56\" run_uuid=\"7fb57089-435d-45f5-94d2-b195e139a0ce\" status=\"OK\" user_id=\"18\" prob_id=\"1\" lang_id=\"3\" test=\"7\" nsec=\"86718000\" passed_mode=\"yes\"/>\n" +
                    "</runs>\n" +
                    "</runlog>\n",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<runlog contest_id=\"212\" start_time=\"2018/03/25 03:57:10\" duration = \"18000\" stop_time=\"2018/03/25 08:57:10\" current_time=\"2018/05/06 06:09:08\" fog_time=\"720\" unfog_time=\"7200\">\n" +
                    "<name>ACM ICPC 2017 Stage I (25-03-2017) day two</name>\n" +
                    "<users>\n" +
                    "<user id=\"16\" name=\"KNEU_Binary\"/>\n" +
                    "<user id=\"17\" name=\"KISIT_DEV\"/>\n" +
                    "<user id=\"18\" name=\"TLKPI_BUGS\"/>\n" +
                    "</users>\n" +
                    "<problems>\n" +
                    "<problem id=\"1\" short_name=\"A\" long_name=\"task 1-1\"/>\n" +
                    "<problem id=\"2\" short_name=\"B\" long_name=\"task 2-1\"/>\n" +
                    "<problem id=\"3\" short_name=\"C\" long_name=\"task 3-1\"/>\n" +
                    "<problem id=\"4\" short_name=\"D\" long_name=\"task 4-1\"/>\n" +
                    "</problems>\n" +
                    "<languages>\n" +
                    "<language id=\"1\" short_name=\"fpc\" long_name=\"Free Pascal 2.6.4+dfsg-4\"/>\n" +
                    "<language id=\"2\" short_name=\"gcc\" long_name=\"GNU C 4.9.2\"/>\n" +
                    "</languages>\n" +
                    "<runs>\n" +
                    "<run run_id=\"24\" time=\"13\" run_uuid=\"886355de-c5d9-4efc-a347-15f161fd0799\" status=\"OK\" user_id=\"16\" prob_id=\"1\" lang_id=\"3\" test=\"0\" nsec=\"673947000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"25\" time=\"24\" run_uuid=\"9ce4c377-cd8c-470f-8099-79ddae86d828\" status=\"OK\" user_id=\"17\" prob_id=\"2\" lang_id=\"3\" test=\"7\" nsec=\"394133000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"26\" time=\"30\" run_uuid=\"8c99b7ba-645a-4fc2-8c23-f3fc6b7922eb\" status=\"WA\" user_id=\"18\" prob_id=\"1\" lang_id=\"3\" test=\"12\" nsec=\"399036000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"27\" time=\"33\" run_uuid=\"4261b31e-69c3-4061-b38a-c5a57b48c47f\" status=\"OK\" user_id=\"16\" prob_id=\"2\" lang_id=\"3\" test=\"7\" nsec=\"138566000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"28\" time=\"42\" run_uuid=\"0726b0d8-69a8-44c6-8b91-b83bb1e41874\" status=\"WA\" user_id=\"18\" prob_id=\"3\" lang_id=\"3\" test=\"7\" nsec=\"102742000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"29\" time=\"51\" run_uuid=\"25a79043-d404-40ce-b757-fb25fcedee44\" status=\"WA\" user_id=\"16\" prob_id=\"3\" lang_id=\"3\" test=\"0\" nsec=\"964440000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"30\" time=\"55\" run_uuid=\"7ca22388-5907-4ea8-80b9-308e57e66feb\" status=\"WA\" user_id=\"18\" prob_id=\"2\" lang_id=\"3\" test=\"0\" nsec=\"42306000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"31\" time=\"56\" run_uuid=\"7fb57089-435d-45f5-94d2-b195e139a0ce\" status=\"WA\" user_id=\"17\" prob_id=\"3\" lang_id=\"3\" test=\"7\" nsec=\"86718000\" passed_mode=\"yes\"/>\n" +
                    "</runs>\n" +
                    "</runlog>\n",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<runlog contest_id=\"213\" start_time=\"2018/03/25 03:57:10\" duration = \"18000\" stop_time=\"2018/03/25 08:57:10\" current_time=\"2018/05/06 06:09:08\" fog_time=\"720\" unfog_time=\"7200\">\n" +
                    "<name>ACM ICPC 2017 Stage I (25-03-2017) day two</name>\n" +
                    "<users>\n" +
                    "<user id=\"16\" name=\"KNEU_Binary\"/>\n" +
                    "<user id=\"17\" name=\"KISIT_DEV\"/>\n" +
                    "<user id=\"218\" name=\"TLKPI_BUGS-1\"/>\n" +
                    "</users>\n" +
                    "<problems>\n" +
                    "<problem id=\"1\" short_name=\"A\" long_name=\"task 1-1\"/>\n" +
                    "<problem id=\"2\" short_name=\"B\" long_name=\"task 2-1\"/>\n" +
                    "<problem id=\"3\" short_name=\"C\" long_name=\"task 3-1\"/>\n" +
                    "<problem id=\"4\" short_name=\"D\" long_name=\"task 4-1\"/>\n" +
                    "</problems>\n" +
                    "<languages>\n" +
                    "<language id=\"1\" short_name=\"fpc\" long_name=\"Free Pascal 2.6.4+dfsg-4\"/>\n" +
                    "<language id=\"2\" short_name=\"gcc\" long_name=\"GNU C 4.9.2\"/>\n" +
                    "</languages>\n" +
                    "<runs>\n" +
                    "<run run_id=\"24\" time=\"13\" run_uuid=\"886355de-c5d9-4efc-a347-15f161fd0799\" status=\"OK\" user_id=\"16\" prob_id=\"1\" lang_id=\"3\" test=\"0\" nsec=\"673947000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"25\" time=\"24\" run_uuid=\"9ce4c377-cd8c-470f-8099-79ddae86d828\" status=\"OK\" user_id=\"17\" prob_id=\"2\" lang_id=\"3\" test=\"7\" nsec=\"394133000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"26\" time=\"30\" run_uuid=\"8c99b7ba-645a-4fc2-8c23-f3fc6b7922eb\" status=\"WA\" user_id=\"218\" prob_id=\"1\" lang_id=\"3\" test=\"12\" nsec=\"399036000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"27\" time=\"33\" run_uuid=\"4261b31e-69c3-4061-b38a-c5a57b48c47f\" status=\"OK\" user_id=\"16\" prob_id=\"2\" lang_id=\"3\" test=\"7\" nsec=\"138566000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"28\" time=\"42\" run_uuid=\"0726b0d8-69a8-44c6-8b91-b83bb1e41874\" status=\"WA\" user_id=\"218\" prob_id=\"3\" lang_id=\"3\" test=\"7\" nsec=\"102742000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"29\" time=\"51\" run_uuid=\"25a79043-d404-40ce-b757-fb25fcedee44\" status=\"WA\" user_id=\"16\" prob_id=\"3\" lang_id=\"3\" test=\"0\" nsec=\"964440000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"30\" time=\"55\" run_uuid=\"7ca22388-5907-4ea8-80b9-308e57e66feb\" status=\"WA\" user_id=\"218\" prob_id=\"2\" lang_id=\"3\" test=\"0\" nsec=\"42306000\" passed_mode=\"yes\"/>\n" +
                    "<run run_id=\"31\" time=\"56\" run_uuid=\"7fb57089-435d-45f5-94d2-b195e139a0ce\" status=\"WA\" user_id=\"17\" prob_id=\"3\" lang_id=\"3\" test=\"7\" nsec=\"86718000\" passed_mode=\"yes\"/>\n" +
                    "</runs>\n" +
                    "</runlog>\n"
            );

    @Test
    public void mergeContestsWithTheSameTeams() throws Exception {
        StandingsFileParser parser = new StandingsFileParser();
        List<ContestNode> collect = Arrays.asList(parser.parse(input.get(0)).get(), parser.parse(input.get(1)).get());
        ContestMerger merger = new ContestMerger();
        Contest result = merger.mergeContests(collect, new HashMap<>());
        Assert.assertThat(result.getTasks().size(), is(8));
        Assert.assertThat(result.getTasks().get(0).getId(), is(211 * 1000000 + 1L));
        Assert.assertThat(result.getTasks().get(0).getShortName(), is("A1"));
        Assert.assertThat(result.getTasks().get(1).getId(), is(211 * 1000000 + 2L));
        Assert.assertThat(result.getTasks().get(1).getShortName(), is("B1"));
        Assert.assertThat(result.getTasks().get(2).getId(), is(211 * 1000000 + 3L));
        Assert.assertThat(result.getTasks().get(2).getShortName(), is("C1"));
        Assert.assertThat(result.getTasks().get(3).getId(), is(211 * 1000000 + 4L));
        Assert.assertThat(result.getTasks().get(3).getShortName(), is("D1"));

        Assert.assertThat(result.getTasks().get(4).getId(), is(212 * 1000000 + 1L));
        Assert.assertThat(result.getTasks().get(4).getShortName(), is("A2"));
        Assert.assertThat(result.getTasks().get(5).getId(), is(212 * 1000000 + 2L));
        Assert.assertThat(result.getTasks().get(5).getShortName(), is("B2"));
        Assert.assertThat(result.getTasks().get(6).getId(), is(212 * 1000000 + 3L));
        Assert.assertThat(result.getTasks().get(6).getShortName(), is("C2"));
        Assert.assertThat(result.getTasks().get(7).getId(), is(212 * 1000000 + 4L));
        Assert.assertThat(result.getTasks().get(7).getShortName(), is("D2"));

        Assert.assertThat(result.getName(), is("ACM ICPC 2017 Stage I (25-03-2017) day two"));
        Assert.assertThat(result.getStopTime(), is(LocalDateTime.of(2018, 3, 25, 8, 57, 10)));
        Assert.assertThat(result.getStartTime(), is(LocalDateTime.of(2018, 3, 25, 3, 57, 10)));
        Assert.assertThat(result.getCurrentTime(), is(LocalDateTime.of(2018, 5, 6, 6, 9, 8)));

        Assert.assertThat(result.getFogTime(), is(720L));
        Assert.assertThat(result.getUnfogTime(), is(7200L));

        Assert.assertThat(result.getResults().size(), is(3));
        Assert.assertThat(result.getResults().get(0).solvedProblems(), is(3));
        Assert.assertThat(result.getResults().get(0).getResults().size(), is(5));
        Assert.assertThat(result.getResults().get(0).getParticipant().getId(), is(16L));

        Assert.assertThat(result.getResults().get(1).solvedProblems(), is(2));
        Assert.assertThat(result.getResults().get(1).getResults().size(), is(5));
        Assert.assertThat(result.getResults().get(1).getParticipant().getId(), is(18L));

        Assert.assertThat(result.getResults().get(2).solvedProblems(), is(1));
        Assert.assertThat(result.getResults().get(2).getResults().size(), is(3));
        Assert.assertThat(result.getResults().get(2).getParticipant().getId(), is(17L));
    }


    @Test
    public void mergeSingleContestTest() {
        StandingsFileParser parser = new StandingsFileParser();
        ContestMerger merger = new ContestMerger();
        Contest result = merger.mergeContests(Arrays.asList(parser.parse(input.get(0)).get()), new HashMap<>());

        Assert.assertThat(result.getTasks().size(), is(4));
        Assert.assertThat(result.getTasks().get(0).getId(), is(211 * 1000000 + 1L));
        Assert.assertThat(result.getTasks().get(0).getShortName(), is("A"));
        Assert.assertThat(result.getTasks().get(1).getId(), is(211 * 1000000 + 2L));
        Assert.assertThat(result.getTasks().get(1).getShortName(), is("B"));
        Assert.assertThat(result.getTasks().get(2).getId(), is(211 * 1000000 + 3L));
        Assert.assertThat(result.getTasks().get(2).getShortName(), is("C"));
        Assert.assertThat(result.getTasks().get(3).getId(), is(211 * 1000000 + 4L));
        Assert.assertThat(result.getTasks().get(3).getShortName(), is("D"));
    }

    @Test
    public void mergeContestsWithNotSameTeams() {
        StandingsFileParser parser = new StandingsFileParser();
        List<ContestNode> collect = Arrays.asList(parser.parse(input.get(0)).get(), parser.parse(input.get(2)).get());
        ContestMerger merger = new ContestMerger();
        Contest result = merger.mergeContests(collect, new HashMap<>());


        Assert.assertThat(result.getTasks().size(), is(8));
        Assert.assertThat(result.getTasks().get(0).getId(), is(211 * 1000000 + 1L));
        Assert.assertThat(result.getTasks().get(0).getShortName(), is("A1"));
        Assert.assertThat(result.getTasks().get(1).getId(), is(211 * 1000000 + 2L));
        Assert.assertThat(result.getTasks().get(1).getShortName(), is("B1"));
        Assert.assertThat(result.getTasks().get(2).getId(), is(211 * 1000000 + 3L));
        Assert.assertThat(result.getTasks().get(2).getShortName(), is("C1"));
        Assert.assertThat(result.getTasks().get(3).getId(), is(211 * 1000000 + 4L));
        Assert.assertThat(result.getTasks().get(3).getShortName(), is("D1"));

        Assert.assertThat(result.getTasks().get(4).getId(), is(213 * 1000000 + 1L));
        Assert.assertThat(result.getTasks().get(4).getShortName(), is("A2"));
        Assert.assertThat(result.getTasks().get(5).getId(), is(213 * 1000000 + 2L));
        Assert.assertThat(result.getTasks().get(5).getShortName(), is("B2"));
        Assert.assertThat(result.getTasks().get(6).getId(), is(213 * 1000000 + 3L));
        Assert.assertThat(result.getTasks().get(6).getShortName(), is("C2"));
        Assert.assertThat(result.getTasks().get(7).getId(), is(213 * 1000000 + 4L));
        Assert.assertThat(result.getTasks().get(7).getShortName(), is("D2"));

        Assert.assertThat(result.getName(), is("ACM ICPC 2017 Stage I (25-03-2017) day two"));
        Assert.assertThat(result.getStopTime(), is(LocalDateTime.of(2018, 3, 25, 8, 57, 10)));
        Assert.assertThat(result.getStartTime(), is(LocalDateTime.of(2018, 3, 25, 3, 57, 10)));
        Assert.assertThat(result.getCurrentTime(), is(LocalDateTime.of(2018, 5, 6, 6, 9, 8)));

        Assert.assertThat(result.getFogTime(), is(720L));
        Assert.assertThat(result.getUnfogTime(), is(7200L));

        Assert.assertThat(result.getResults().size(), is(4));
        Assert.assertThat(result.getResults().get(0).solvedProblems(), is(3));
        Assert.assertThat(result.getResults().get(0).getResults().size(), is(5));
        Assert.assertThat(result.getResults().get(0).getParticipant().getId(), is(16L));

        Assert.assertThat(result.getResults().get(1).solvedProblems(), is(2));
        Assert.assertThat(result.getResults().get(1).getResults().size(), is(2));
        Assert.assertThat(result.getResults().get(1).getParticipant().getId(), is(18L));

        Assert.assertThat(result.getResults().get(2).solvedProblems(), is(1));
        Assert.assertThat(result.getResults().get(2).getResults().size(), is(3));
        Assert.assertThat(result.getResults().get(2).getParticipant().getId(), is(17L));


    }


}