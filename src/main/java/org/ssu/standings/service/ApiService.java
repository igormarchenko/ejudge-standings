package org.ssu.standings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.standings.entity.ContestInfo;
import org.ssu.standings.entity.ExternalFileDescription;
import org.ssu.standings.entity.Team;
import org.ssu.standings.entity.University;
import org.ssu.standings.repository.ExternalFilesRepository;
import org.ssu.standings.repository.TeamRepository;
import org.ssu.standings.repository.UniversityRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiService {
    @Resource
    private ExternalFilesRepository externalFilesRepository;

    @Resource
    private TeamRepository teamRepository;

    @Resource
    private UniversityRepository universityRepository;

    public List<Team> teamList() {
        return teamRepository.findAll();
    }

    public List<University> universityList() {
        return universityRepository.findAll();
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public void removeTeam(Long teamId) {
        teamRepository.delete(teamId);
    }


    public void removeUniversity(Long universityId) {
        universityRepository.delete(universityId);
    }

    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }

    public List<ContestInfo> contestList() {
        Map<Long, List<ExternalFileDescription>> externalFiles = externalFilesRepository.findAll().stream()
                .collect(Collectors.groupingBy(ExternalFileDescription::getContestId));

        return externalFiles.entrySet().stream()
                .map(item -> new ContestInfo.Builder(item.getKey(), item.getValue().get(0).getIsFinal(), item.getValue()).build())
                .collect(Collectors.toList());
    }

    private Long lastContestId() {
        Optional<ExternalFileDescription> lastContest = externalFilesRepository.findAll().stream().sorted((a, b) -> Long.compare(a.getContestId(), b.getContestId())).reduce((a, b) -> b);
        if (lastContest.isPresent())
            return lastContest.get().getContestId();
        return 0L;
    }

    @Transactional
    public ContestInfo saveContest(ContestInfo contestInfo) {
        deleteContest(contestInfo.getContestId());
        if (contestInfo.getContestId() == null) {
            Long contestId = lastContestId() + 1;
            Boolean isFinal = contestInfo.getIsFinal();

            contestInfo = new ContestInfo.Builder()
                    .withContestId(lastContestId() + 1)
                    .withExternalFileDesriptions(contestInfo.getExternalFileDescriptionList().stream()
                            .map(item -> new ExternalFileDescription.Builder(item)
                                    .withContestId(contestId)
                                    .withIsFinal(isFinal)
                                    .build())
                            .collect(Collectors.toList()))
                    .withIsFinal(contestInfo.getIsFinal()).build();
        }

        return new ContestInfo.Builder(contestInfo.getContestId(),
                contestInfo.getIsFinal(),
                externalFilesRepository.save(contestInfo.getExternalFileDescriptionList().stream().filter(Objects::nonNull).collect(Collectors.toList()))).build();
    }

    @Transactional
    public void deleteContest(Long contestId) {
        externalFilesRepository.deleteByContestId(contestId);
    }
}
