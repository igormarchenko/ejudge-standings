angular.module("standingsPage", ['ui.select', 'ngSanitize', 'ngAnimate', 'sly']).controller("standingsController",
    ["$scope", '$window', "$http", '$interval', '$location', '$timeout', function ($scope, $window, $http, $interval, $location, $timeout) {
        $scope.results = [];
        $scope.contest = {};
        $scope.tasks = [];
        $scope.universityTypes = [];
        $scope.regionList = [];
        $scope.selectors = {
            regions: [],
            universityTypes: []
        };

        var results = {};
        var contestId;
        var lastSubmitTime = -1;
        var frozenSubmits = {};
        var unfrozeMode = false;
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

        $scope.unfrozeResults = function () {
            $interval.cancel(interval);
            $http({
                url: '/api/frozenresults/' + contestId,
                method: "GET",
                params: {}
            }).then(function (response) {
                angular.copy(response.data, frozenSubmits);
                unfrozeMode = true;
            });
        };

        $scope.unfrozeNext = function () {
            if (unfrozeMode) {
                var frozenTask = findNextUnknownSubmission();
                if (frozenTask != null) {
                    var teamPosition = findTeamPosition(frozenTask.team.contest_team_id);
                    $('html, body').animate({
                        scrollTop: $("#team" + frozenTask.team.contest_team_id).offset().top - 300
                    }, 500).promise().then(function() {
                        var submitsForTask = frozenSubmits[frozenTask.team.contest_team_id][frozenTask.taskId];
                        frozenTask.task.submissions = frozenTask.task.submissions.filter(function (submit) {
                            return submit.status != "UNKNOWN";
                        });
                        frozenTask.task.count = frozenTask.task.submissions.length;
                        angular.forEach(submitsForTask, function (submit) {
                            pushSubmitOnline(teamPosition, submit);
                        });
                        updateTeamPosition(teamPosition);
                        $scope.$apply();
                    });
                }
            }
        };
        var findNextUnknownSubmission = function () {
            var result = null;
            var keepGoing = true;
            $scope.results.slice().reverse().forEach(function (team) {
                if (keepGoing) {
                    angular.forEach(team.tasks, function (task, index) {
                        if (keepGoing) {
                            if (task.status == "UNKNOWN") {
                                keepGoing = false;
                                result = {
                                    'team': team,
                                    'task': task,
                                    'taskId': index
                                };
                            }
                        }
                    });
                }
            });
            return result;
        };

        var initResults = function (contestId) {
            $http({
                method: 'GET',
                url: '/api/init-results/' + contestId
            }).then(function (response) {
                $scope.contest.name = response.data.name;
                $scope.contest.last_success = {};
                $scope.contest.last_submit = {};
                angular.copy(response.data.tasks, $scope.tasks);
                var tasks = {};
                angular.forEach(response.data.tasks, function (task) {
                    tasks[task.id] = {
                        'count': 0,
                        'status': 'N/A',
                        'ok_time': 0,
                        'submissions': []
                    };
                });

                angular.forEach(response.data.teams, function (team) {
                    results[team.contest_team_id] = team;
                    results[team.contest_team_id].solved = 0;
                    results[team.contest_team_id].penalty = 0;
                    results[team.contest_team_id].tasks = {};
                    results[team.contest_team_id].isDisplayed = true;

                    angular.copy(tasks, results[team.contest_team_id].tasks);
                    $scope.universityTypes.push(team.university.type);
                    $scope.regionList.push(team.university.region);
                });

                $scope.universityTypes = $.unique($scope.universityTypes).sort();
                $scope.regionList = $.unique($scope.regionList).sort();

                angular.copy($scope.universityTypes, $scope.selectors.universityTypes);
                angular.copy($scope.regionList, $scope.selectors.regions);


                angular.forEach(response.data.submissions, function (submission) {
                    pushSubmission(submission);
                });

                angular.forEach(results, function (result) {
                    $scope.results.push(result);
                });

                $scope.results.sort(compareTeams);

                $timeout(function () {
                    $("#resultsTable").floatThead({});
                    $('html, body').animate({
                        scrollTop: $("#resultsTable").offset().top
                    }, 1000);
                }, 0);
            }, function() {});
        };

        $scope.filter = function () {
            angular.forEach(results, function (team) {
                team.isDisplayed = $scope.selectors.universityTypes.indexOf(team.university.type) >= 0 &&
                    $scope.selectors.regions.indexOf(team.university.region) >= 0;
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
                if (submission.status != "UNKNOWN") {
                    currentTask.penalty = (currentTask.count - 1) * 20 + currentTask.ok_time;
                }
                if (submission.status == "OK") {
                    team.solved++;
                    team.penalty += currentTask.penalty;
                    $scope.contest.last_success = {
                        'time': sprintf("%02d:%02d:%02d", submission.time / 3600, submission.time % 3600 / 60, submission.time % 60),
                        'team': team,
                        'task': String.fromCharCode(96 + submission.problemId).toUpperCase()
                    };
                }
            }
            if (submission.status != "UNKNOWN") {
                $scope.contest.last_submit = {
                    'time': sprintf("%02d:%02d:%02d", submission.time / 3600, submission.time % 3600 / 60, submission.time % 60),
                    'team': team,
                    'task': String.fromCharCode(96 + submission.problemId).toUpperCase()
                };
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

                    // var obj = $('#team' + $scope.results[teamPosition].contest_team_id);
                    // obj.fadeOut(600, function () {
                    //     obj.fadeIn(600);
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
            }).then(function (response) {
                if (response.data.length > 0) {
                    angular.forEach(response.data, function (submit) {
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