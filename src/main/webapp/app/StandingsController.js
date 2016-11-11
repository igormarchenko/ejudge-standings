angular.module("standingsPage", ['ui.select', 'ngSanitize', 'ngAnimate']).controller("standingsController",
    ["$scope", '$window', "$http", '$interval', '$location', '$timeout', function ($scope, $window, $http, $interval, $location, $timeout) {
        $scope.results = [];
        $scope.contest = {};
        $scope.tasks = [];

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
                $scope.contest.name = response.name;
                $scope.contest.last_success = {};
                $scope.contest.last_submit = {};
                // console.log(response);
                angular.copy(response.tasks, $scope.tasks);
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

                $timeout(function () {
                    $("#resultsTable").floatThead({});
                }, 0);
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
                    $scope.contest.last_success = {
                        'time': sprintf("%02d:%02d:%02d", submission.time / 3600, submission.time % 3600 / 60, submission.time % 60),
                        'team': team,
                        'task': String.fromCharCode(97 + submission.problemId).toUpperCase()
                    };
                }
            }

            $scope.contest.last_submit = {
                'time': sprintf("%02d:%02d:%02d", submission.time / 3600, submission.time % 3600 / 60, submission.time % 60),
                'team': team,
                'task': String.fromCharCode(97 + submission.problemId).toUpperCase()
            };

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

        var canTeamMoveUp = function (teamPosition) {
            return teamPosition - 1 >= 0 && compareTeams($scope.results[teamPosition], $scope.results[teamPosition - 1]) < 0;
        };

        var updateTeamPosition = function (teamPosition) {

            var slideInterval = setInterval(function () {
                if (!canTeamMoveUp(teamPosition)) {
                    clearInterval(slideInterval);
                } else {
                    var tempTeam = {};

                    angular.copy($scope.results[teamPosition - 1], tempTeam);
                    angular.copy($scope.results[teamPosition], $scope.results[teamPosition - 1]);
                    angular.copy(tempTeam, $scope.results[teamPosition]);

                    // console.log($scope.results[teamPosition - 1].contest_team_id);
                    // $('#team' + $scope.results[teamPosition - 1].contest_team_id).fadeOut(600, function () {
                    //     console.log($scope.results[teamPosition - 1].contest_team_id);
                    //     $('#team' + $scope.results[teamPosition - 1].contest_team_id).fadeIn(600);
                    // });
                    teamPosition--;
                    $scope.$apply();
                }
            }, 900);
        };

        var updateResults = function () {
            $http({
                method: 'GET',
                url: '/api/results/' + contestId + '/' + lastSubmitTime
            }).success(function (response) {
                if (response.length > 0) {
                    angular.forEach(response, function (submit) {
                        var team = findTeamPosition(submit.userId);
                        pushSubmitOnline(team, submit);
                        updateTeamPosition(team);

                    });
                }
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