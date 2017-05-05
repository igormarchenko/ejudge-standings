angular.module('ejudgeStandings.controllers', [])
    .controller('contestListController', function ($scope, ejudgeApiService) {
        $scope.contests = initTeams();
        function initTeams() {
            var contests = {};
            ejudgeApiService.contestList().then(function (response) {
                angular.forEach(response.data, function (contest) {
                    contests[contest.id] = contest;
                });
            });
            return contests;
        }
    })
    .controller('resultsController', function ($scope, $routeParams, ejudgeApiService) {
        initContestData();
        function initContestData() {
            ejudgeApiService.contestData($routeParams.contestId).then(function (response) {
                $scope.contest = response.data;
                console.log($scope.contest);
            });
        }

        $scope.formatTime = function(minutes) {
            return sprintf("%02d:%02d", minutes / 60 / 60, minutes / 60 % 60);
        }
    });