angular.module('ejudgeStandings.controllers', ['datatables'])
    .controller('teamsController', function ($scope, ejudgeStandingsApiService, DTOptionsBuilder, DTColumnDefBuilder) {
        $scope.teams = initTeams();
        $scope.universities = universityList();
        $scope.selectedTeam = {};

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
                });
            });

        }

        dtColumnDefs = [
            DTColumnDefBuilder.newColumnDef(0),
            DTColumnDefBuilder.newColumnDef(1),
            DTColumnDefBuilder.newColumnDef(2),
            DTColumnDefBuilder.newColumnDef(4).notSortable()
        ];

        $scope.editTeam = function (teamId) {
            angular.copy($scope.teams[teamId], $scope.selectedTeam);
            $('#editTeam').modal('show');
        };


        $scope.removeTeam = function (teamId) {
            ejudgeStandingsApiService.removeTeam(teamId).success(function () {
                delete $scope.teams[teamId];
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
            });
        };

    })
    .controller('contestsEditorController', function ($scope, ejudgeStandingsApiService, DTOptionsBuilder, DTColumnDefBuilder) {
        $scope.contests = contestList();
        $scope.selectedContest = {'is_final': false, 'external_files': []};

        function contestList() {
            ejudgeStandingsApiService.contestList().then(function (response) {
                $scope.contests = {};
                angular.forEach(response.data, function (contest) {
                    $scope.contests[contest.id] = contest;
                });
            });
        }

        $scope.editContest = function (contestId) {
            angular.copy($scope.contests[contestId], $scope.selectedContest);
            $('#editContest').modal('show');
        };

        $scope.removeURL = function (index) {
            delete $scope.selectedContest.external_files[index];
        };

        $scope.addUrl = function () {
            $scope.selectedContest.external_files.push({
                'is_final': $scope.selectedContest.is_final,
                'contest_id': $scope.selectedContest.id
            });
        };

        $scope.saveSelectedContest = function () {
            ejudgeStandingsApiService.saveContest($scope.selectedContest).then(function (response) {
                $scope.contests[response.data.id] = {};
                angular.copy(response.data, $scope.contests[response.data.id]);
                $('#editContest').modal('hide');
                angular.copy({'is_final': false, 'external_files': []}, $scope.selectedContest);
            });
        };

        $scope.removeContest = function (contestId) {
            ejudgeStandingsApiService.removeContest(contestId).then(function () {
                delete $scope.contests[contestId];
            });
        };

    });