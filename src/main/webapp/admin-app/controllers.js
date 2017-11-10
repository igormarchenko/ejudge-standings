angular.module('ejudgeStandings.controllers', ['datatables'])
    .controller('teamsController', function ($scope, ejudgeStandingsApiService, DTOptionsBuilder, DTColumnDefBuilder) {
        $scope.teams = initTeams();
        $scope.universities = universityList();
        $scope.selectedTeam = {};
        $scope.displayedUniversities = [];

        var emptyTeam = {
            'name': null,
            'university': null
        };

        function initTeams() {
            ejudgeStandingsApiService.teamList().then(function (response) {
                $scope.teams = {};
                angular.forEach(response.data, function (team) {
                    $scope.teams[team.id] = team;
                });
            });
        }

        function universityList() {
            ejudgeStandingsApiService.universityList().then(function (response) {
                $scope.universities = {};
                angular.forEach(response.data, function (university) {
                    $scope.universities[university.id] = university;
                    $scope.displayedUniversities.push(university);
                });
            });

        }

        dtColumnDefs = [
            DTColumnDefBuilder.newColumnDef(0),
            DTColumnDefBuilder.newColumnDef(1),
            DTColumnDefBuilder.newColumnDef(2),
            DTColumnDefBuilder.newColumnDef(4).notSortable()
        ];

        $scope.clearSelectedTeam = function () {
            $scope.selectedTeam = {};
        };
        $scope.editTeam = function (teamId) {
            angular.copy($scope.teams[teamId], $scope.selectedTeam);
            $('#editTeam').modal('show');
        };

        $scope.showModalDialogForTeam = function (teamId) {
            angular.copy($scope.teams[teamId], $scope.selectedTeam);
            $('#deleteTeamModal').modal('show');
        };
        $scope.removeTeam = function (teamId) {
            ejudgeStandingsApiService.removeTeam(teamId).then(function () {
                delete $scope.teams[teamId];
                $('#deleteTeamModal').modal('hide');
            });
        };

        $scope.saveSelectedTeam = function () {
            ejudgeStandingsApiService.saveTeam($scope.selectedTeam).then(function (response) {
                $scope.teams[response.data.id] = {};
                angular.copy(response.data, $scope.teams[response.data.id]);
                $('#editTeam').modal('hide');
                angular.copy(emptyTeam, $scope.selectedTeam);
            });
        }
    })
    .controller('universityController', function ($scope, ejudgeStandingsApiService, DTOptionsBuilder, DTColumnDefBuilder) {
        $scope.universities = universityList();
        $scope.selectedUniversity = {};

        var emptyUniversity = {
            'name': null,
            'region': null,
            'type': null
        };

        function universityList() {
            ejudgeStandingsApiService.universityList().then(function (response) {
                $scope.universities = {};
                angular.forEach(response.data, function (university) {
                    $scope.universities[university.id] = university;
                });
            });
        }

        $scope.clearSelectedUniversity = function () {
            $scope.selectedUniversity = {};
        };

        $scope.showModalDialogForUniversity = function (universityId) {
            angular.copy($scope.universities[universityId], $scope.selectedUniversity);
            $('#deleteUniversityModal').modal('show');
        };
        $scope.editUniversity = function (universityId) {
            angular.copy($scope.universities[universityId], $scope.selectedUniversity);
            $('#editUniversity').modal('show');
        };

        $scope.saveSelectedUniversity = function () {
            ejudgeStandingsApiService.saveUniversity($scope.selectedUniversity).then(function (response) {
                $scope.universities[response.data.id] = {};
                angular.copy(response.data, $scope.universities[response.data.id]);
                $('#editUniversity').modal('hide');
                angular.copy(emptyUniversity, $scope.selectedUniversity);
            });
        };

        $scope.removeUniversity = function (universityId) {
            ejudgeStandingsApiService.removeUniversity(universityId).then(function () {
                delete $scope.universities[universityId];
                $('#deleteUniversityModal').modal('hide');
            });
        };

    })
    .controller('contestsEditorController', function ($scope, ejudgeStandingsApiService, DTOptionsBuilder, DTColumnDefBuilder) {
        $scope.contests = contestList();
        $scope.selectedContest = {'standingsFiles': []};

        function contestList() {
            ejudgeStandingsApiService.contestList().then(function (response) {
                $scope.contests = {};
                angular.forEach(response.data, function (contest) {
                    $scope.contests[contest.id] = contest;
                });
            });
        }

        $scope.showModalDialogForContest = function (contestId) {
            angular.copy($scope.contests[contestId], $scope.selectedContest);
            $('#deleteContestModal').modal('show');
        };

        $scope.clearContest = function () {
            $scope.selectedContest = {'standingsFiles': []};
        };

        $scope.editContest = function (contestId) {
            angular.copy($scope.contests[contestId], $scope.selectedContest);
            $('#editContest').modal('show');
        };

        $scope.removeURL = function (index) {
            $scope.selectedContest.standingsFiles.splice(index, 1);
        };

        $scope.addUrl = function () {
            $scope.selectedContest.standingsFiles.push({
                'frozen': $scope.selectedContest.is_final
            });
        };

        $scope.saveSelectedContest = function () {
            ejudgeStandingsApiService.saveContest($scope.selectedContest).then(function (response) {
                $scope.contests[response.data.id] = {};
                angular.copy(response.data, $scope.contests[response.data.id]);
                $('#editContest').modal('hide');
                angular.copy({'standingsFiles': []}, $scope.selectedContest);
            });
        };

        $scope.removeContest = function (contestId) {
            ejudgeStandingsApiService.removeContest(contestId).then(function () {
                delete $scope.contests[contestId];
                $('#deleteContestModal').modal('hide');
            });
        };

    });