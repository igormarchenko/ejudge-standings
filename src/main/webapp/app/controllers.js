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
            ejudgeStandingsApiService.teamList().success(function (response) {
                $scope.teams = {};
                angular.forEach(response, function (team) {
                    $scope.teams[team.id] = team;
                });
            });
        }

        function universityList() {
            ejudgeStandingsApiService.universityList().success(function (response) {
                $scope.universities = {};
                angular.forEach(response, function (university) {
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
            ejudgeStandingsApiService.saveTeam($scope.selectedTeam).success(function (response) {
                $scope.teams[response.id] = {};
                angular.copy(response, $scope.teams[response.id]);
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
            ejudgeStandingsApiService.universityList().success(function (response) {
                $scope.universities = {};
                angular.forEach(response, function (university) {
                    $scope.universities[university.id] = university;
                });
            });
        }

        $scope.editUniversity = function (universityId) {
            angular.copy($scope.universities[universityId], $scope.selectedUniversity);
            $('#editUniversity').modal('show');
        };

        $scope.saveSelectedUniversity = function () {
            ejudgeStandingsApiService.saveUniversity($scope.selectedUniversity).success(function (response) {
                $scope.universities[response.id] = {};
                angular.copy(response, $scope.universities[response.id]);
                $('#editUniversity').modal('hide');
                angular.copy(emptyUniversity, $scope.selectedUniversity);
            });
        }

        $scope.removeUniversity = function (universityId) {
            ejudgeStandingsApiService.removeUniversity(universityId).success(function () {
                delete $scope.universities[universityId];
            });
        };

    });