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
    .controller('resultsController', function ($scope, $routeParams, ejudgeApiService, WebSocketService) {
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
            WebSocketService.initialize($routeParams.contestId);
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

        teamUp = function (index) {
            if (index <= 0 || index >= $scope.display.length)
                return;
            var obj = $('#teamrow-' + (index - 1));
            obj.fadeOut(600, function () {
                obj.fadeIn(600);
            });

            var temp = $scope.display[index];
            $scope.display[index] = $scope.display[index - 1];
            $scope.display[index - 1] = temp;
        };

        slideTeam = function(startPos, endPos) {
            var index = startPos;
            var interval  = setInterval(function() {
                $scope.$apply(function() {
                    teamUp(index);
                });
                index--;
                if(index < endPos) clearInterval(interval);
            }, 1200);
        };

        WebSocketService.receive().then(null, null, function (response) {
            angular.forEach(response.updates, function(team) {
               // console.log(team);
               $scope.display[team.previousPlace] = team.result;
               slideTeam(team.previousPlace, team.currentPlace);

            });
        });
    });