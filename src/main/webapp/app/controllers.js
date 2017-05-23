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
        var data = {};
        $scope.display = [];
        $scope.scrollDisabled = true;

        $scope.loadMore = function () {
            var batchSize = 20;
            if (data.results.length > 0) {
                for (var i = 0; i < batchSize && i < data.results.length; i++) {
                    $scope.display.push(data.results[i]);
                }
                data.results.splice(0, Math.min(batchSize, data.results.length));
            } else {
                $scope.scrollDisabled = true;
            }
        };

        function initContestData() {
            ejudgeApiService.contestData($routeParams.contestId).then(function (response) {
                data = response.data;
                $scope.contest = {
                    'name': data.name,
                    'tasks': data.tasks
                };
                $scope.scrollDisabled = false;
            });
        }

        $scope.formatTime = function (minutes) {
            return sprintf("%02d:%02d", minutes / 60, minutes % 60);
        };
    }).controller('webSocketController', function ($scope, $routeParams, WebSocketService) {
        WebSocketService.initialize($routeParams.contestId);
        WebSocketService.receive().then(null, null, function(message) {
            console.log(message);
        });
});