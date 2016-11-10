angular.module("standingsPage", ['ui.select', 'ngSanitize']).controller("standingsController",
    ["$scope", '$window', "$http", '$interval', '$location', function ($scope, $window, $http, $interval, $location) {
        $scope.results = [];

        var results = {};
        var contestId;
        var lastSubmitTime = -1;
        $scope.init = function () {
            contestId = window.location.pathname.split('/').pop();
            initResults(contestId);
        };

        $scope.taskResult = function (task) {
            if (task.status == 'OK') {
                return '<b>+' + ((task.count - 1 > 0) ? task.count - 1 : '') + '</b><br/>'
                    + '<i><small>' + sprintf("%02d:%02d", task.ok_time / 60, task.ok_time % 60) + '</small></i>';
            }
            if (task.status == 'UNKNOWN') return '<b>?</b>';
            if (task.status == 'N/A') return '';
            return '<b>-' + task.count + '</b>';
        };

        $scope.cellStyle = function (status) {
            if (status == 'OK') return 'cell-ok';
            if (status == 'UNKNOWN') return 'cell-unknown';
            if (status == 'N/A') return 'cell-empty';
            return 'cell-wrong';
        };


        var initResults = function (contestId) {
            $http({
                method: 'GET',
                url: '/api/init-results/' + contestId
            }).success(function (response) {
                var tasks = {};
                angular.forEach(response.tasks, function (task) {
                    tasks[task.id] = {
                        'count': 0,
                        'status': 'N/A',
                        'ok_time': 0,
                        'submissions': []
                    };
                });

                angular.forEach(response.teams, function (team) {
                    results[team.contest_team_id] = team;
                    results[team.contest_team_id].solved = 0;
                    results[team.contest_team_id].penalty = 0;
                    results[team.contest_team_id].tasks = {};
                    angular.copy(tasks, results[team.contest_team_id].tasks);
                });

                angular.forEach(response.submissions, function (submission) {
                    pushSubmission(submission);
                });

                angular.forEach(results, function (result) {
                    $scope.results.push(result);
                });

                $scope.results.sort(compareTeams);

                console.log($scope.results);
            });
        };

        var pushSubmission = function (submission) {
            results[submission.userId] = addSubmitToTeam(results[submission.userId], submission);
            lastSubmitTime = Math.max(lastSubmitTime, submission.time);
        };


        var addSubmitToTeam = function (team, submission) {
            var currentTask = team.tasks[submission.problemId];
            if (currentTask.status != "OK") {
                currentTask.submissions.push(submission);
                currentTask.count++;
                currentTask.status = submission.status;


                if (submission.status == "OK") {
                    currentTask.ok_time = Math.ceil(submission.time / 60.0);
                }
                currentTask.penalty = (currentTask.count - 1) * 20 + currentTask.ok_time;
                if (submission.status == "OK") {
                    team.solved++;
                    team.penalty += currentTask.penalty;
                }
            }
            return team;
        };

        var pushSubmitOnline = function (teamPosition, submission) {
            $scope.results[teamPosition] = addSubmitToTeam($scope.results[teamPosition], submission);
            lastSubmitTime = Math.max(lastSubmitTime, submission.time);
        };

        var findTeamPosition = function (teamId) {
            var result = 0,
                keepGoing = true;
            angular.forEach($scope.results, function (team, index) {
                if (keepGoing && team.contest_team_id == teamId) {
                    result = index;
                    keepGoing = false;
                }
            });
            return result;
        };

        var updateTeamPosition = function (teamPosition) {

            while (true) {
                if (teamPosition - 1 < 0) break;
                if (compareTeams($scope.results[teamPosition], $scope.results[teamPosition - 1]) > 0) break;

                var tempTeam = {};
                angular.copy($scope.results[teamPosition], tempTeam);
                angular.copy($scope.results[teamPosition - 1], $scope.results[teamPosition]);
                angular.copy(tempTeam, $scope.results[teamPosition - 1]);

                teamPosition--;
            }
        };

        var updateResults = function () {
            $http({
                method: 'GET',
                url: '/api/results/' + contestId + '/' + lastSubmitTime
            }).success(function (response) {
                console.log(response);
                angular.forEach(response, function (submit) {
                    var team = findTeamPosition(submit.userId);
                    pushSubmitOnline(team, submit);
                    updateTeamPosition(team);
                });
            })
        };

        var interval = $interval(updateResults, 1000);

        var compareTeams = function (a, b) {
            if (a.solved == b.solved) {
                return a.penalty - b.penalty;
            } else {
                return b.solved - a.solved;
            }
        };
    }]);