angular.module("standingsPage", ['ui.select', 'ngCookies']).controller("standingsController",
    ["$scope", '$window', "$http", '$interval', '$cookies', function ($scope, $window, $http, $interval, $cookies) {
        var data = {};
        var unfreezed = false;
        var freezedSumbiossions = {};
        var frozenSubmitsOrder = [];
        var frozenSubmits = [];
        var lastUnfrozenSubmitInRegion = {};
        $scope.results = [];
        $scope.regionSelector = {};
        $scope.regions = {};
        $scope.lastSuccess = {};
        $scope.lastSubmit = {};
        $scope.tasks = {};
        $scope.regionList = [];
        var lastSubmit = {};

        $scope.contest = {};
        $scope.submissions = {};

        $scope.init = function () {
            $http({
                url: '/api/init-results',
                method: "GET",
                params: {}
            }).success(function (response) {
                $scope.regionSelector.data = [];
                angular.forEach(response, function (region, index) {
                    $scope.regions[region.region] = index;
                    $scope.regionList.push(region.region);
                    $scope.regionSelector.data.push(region.region);
                    fillRegionData(region);
                });

                $scope.fillDisplayData();
                $scope.updatePage();
            });
        };

        $scope.updatePage = function () {
            $(".taskHead").remove();
            $("#results_body").empty();
            renderPage();
        };

        $scope.fillDisplayData = function () {
            $scope.results = angular.copy([]);
            angular.forEach(data, function (result, index) {
                $scope.results.push(result);
            });

            $scope.results.sort(compareTeams);
        };

        $scope.getLastSuccess = function () {
            var result = null;
            angular.forEach($scope.lastSuccess, function (submit, ind) {
                if (isRegionSelected(ind) && (result == null || result.time < submit.time))
                    result = angular.copy(submit);
            });
            return result;
        };

        $scope.getLastSubmit = function () {
            var result = null;
            angular.forEach($scope.lastSubmit, function (submit, ind) {
                if (isRegionSelected(ind) && (result == null || result.time < submit.time))
                    result = angular.copy(submit);
            });
            return result;
        };

        var fillRegionData = function (response) {
            $scope.contest.name = response.name;
            $scope.tasks = response.tasks;

            var taskObject = filltasksData(response.tasks);
            angular.forEach(response.teams, function (team, index) {
                data[generateGlobalTeamId(team.id, response.region)] = {
                    'id': generateGlobalTeamId(team.id, response.region),
                    'name': team.name,
                    'region': response.region,
                    'result': angular.copy(taskObject),
                    'solved_tasks': 0,
                    'penalty': 0
                };
            });

            angular.forEach(response.submissions, function (submission, index) {
                pushSubmission(submission, response.region);
            });
        };

        var compareTeams = function (a, b) {
            if (a.solved_tasks != b.solved_tasks)
                return b.solved_tasks - a.solved_tasks;

            return a.penalty - b.penalty;
        };

        var filltasksData = function (tasks) {
            var taskObject = {};
            angular.forEach(tasks, function (task, index) {
                taskObject[task.id] = {
                    'id': task.id,
                    'short_name': task.shortName,
                    'long_name': task.longName,
                    'result': '-',
                    'tries': 0,
                    'penalty': 0,
                    'time': 0
                };
            });
            return taskObject;
        };

        var pushSubmission = function (submission, region) {
            var teamId = generateGlobalTeamId(submission.userId, region);
            var previousResult = data[teamId].result[submission.problemId].result;
            if (submission.status === "OK") {
                $scope.lastSuccess[region] = {
                    'team': data[teamId].name,
                    'problem': $scope.tasks[submission.problemId - 1].shortName,
                    'time': submission.time
                };
            }
            $scope.lastSubmit[region] = {
                'team': data[teamId].name,
                'problem': $scope.tasks[submission.problemId - 1].shortName,
                'time': submission.time
            };
            if (previousResult != 'OK') {
                if (submission.status === "OK")
                    newSolvedTask(submission, region);
                else
                    wrongTry(submission, region);
            }
            if (submission.status == "UNKNOWN") {
                if (freezedSumbiossions[teamId] == undefined)
                    freezedSumbiossions[teamId] = {};
                if (freezedSumbiossions[teamId][submission.problemId] == undefined)
                    freezedSumbiossions[teamId][submission.problemId] = {'tries': 0};

                freezedSumbiossions[teamId][submission.problemId].tries++;
            }
            lastSubmit[region] = submission.runId;
            return data[teamId];
        };

        var wrongTry = function (submission, region) {
            var teamId = generateGlobalTeamId(submission.userId, region);
            data[teamId].result[submission.problemId].result = submission.status;
            if (submission.status != "UNKNOWN") {
                data[teamId].result[submission.problemId].penalty = Math.ceil(submission.time / 60.0);
                data[teamId].result[submission.problemId].time = submission.time;
                data[teamId].result[submission.problemId].tries += submission.tries == undefined ? 1 : submission.tries;
            }
        };

        var newSolvedTask = function (submission, region) {
            var teamId = generateGlobalTeamId(submission.userId, region);
            data[teamId].solved_tasks++;
            data[teamId].result[submission.problemId].result = submission.status;
            data[teamId].result[submission.problemId].tries += (submission.tries == undefined) ? 1 : submission.tries;
            data[teamId].penalty += Math.ceil(submission.time / 60.0) +
                (data[teamId].result[submission.problemId].tries - 1) * 20;
        };


        function generateGlobalTeamId(userId, region) {
            return userId + 100000 * $scope.regions[region];
        }

        function pushSubmitOnline(submit, region, followTask) {
            var teamId = generateGlobalTeamId(submit.userId, region);
            var teamPosInResultsArray = findTeamPositionInResultArray(teamId, $scope.results);
            var teamPositionOnDisplay = getTeamPositionOnDisplayedTable(teamId, $scope.results);

            $scope.results[teamPosInResultsArray] = pushSubmission(submit, region);
            $("#team" + $scope.results[teamPosInResultsArray].id).replaceWith($.parseHTML(renderTeam($scope.results[teamPosInResultsArray], teamPositionOnDisplay)));

            var interval = setInterval(function () {
                if (!canSlideUp(teamId)) {
                    clearInterval(interval);
                } else {
                    swapTeams(teamId, followTask);
                }
            }, 1000);
        }

        var canSlideUp = function (teamId) {
            var previousTeamPos = findPreviousDisplayedTeamPositionInArray(teamId, $scope.results);
            return previousTeamPos >= 0 && compareTeams($scope.results[findTeamPositionInResultArray(teamId, $scope.results)], $scope.results[previousTeamPos]) < 0;
        };

        var swapTeams = function (teamId, followTask) {
            var teamPos = findTeamPositionInResultArray(teamId, $scope.results);
            var temp = $scope.results[teamPos];
            var previousPos = findPreviousDisplayedTeamPositionInArray(teamId, $scope.results);

            $scope.results[teamPos] = $scope.results[previousPos];
            $scope.results[previousPos] = angular.copy(temp);

            var previousTeam = $scope.results[previousPos].id;
            var currentTeam = $scope.results[teamPos].id;

            var previousElement = $("#team" + previousTeam);

            previousElement.fadeOut(500);
            var prevPos = $("#team" + currentTeam + "pos").text();
            var newPos = $("#team" + previousTeam + "pos").text();
            $("#team" + currentTeam + "pos").text(newPos);
            $("#team" + previousTeam + "pos").text(prevPos);
            previousElement.after($("#team" + currentTeam));
            previousElement.fadeIn();
            if (followTask)
                scrollToTeam($scope.results[teamPos].id);
        };

        var updateResults = function () {
            $http({
                url: '/api/results',
                method: "GET",
                params: {last_submit: lastSubmit}
            }).success(function (data) {
                angular.forEach(data, function (region, ind) {
                    angular.forEach(region, function (submission, index) {
                        pushSubmitOnline(submission, ind, false);
                    });
                });
            });
        };

        var renderPage = function () {
            renderTable($scope.results);
            renderTaskList($scope.tasks);
            $("#results_table").floatThead({});
        };

        var isRegionSelected = function (region) {
            return $scope.regionSelector.data.indexOf(region) >= 0;
        };

        var renderTable = function (results) {
            var content = "";
            var pos = 0;
            angular.forEach(results, function (team, index) {
                if (isRegionSelected(team.region))
                    content += renderTeam(team, pos++);
            });
            $("#results_body").append(content);
        };

        var renderCell = function (result, team) {
            var css = "empty-cell";
            var digit = "";
            var sign = "";
            if (result.result === "OK") {
                css = 'OK-cell';
                sign = "+";
                if (result.tries > 1)
                    digit = result.tries - 1;
            } else if (result.tries > 0 && result.result !== "UNKNOWN") {
                css = 'WA-cell';
                sign = "-";
                digit = result.tries;
            }
            if (result.result === "UNKNOWN") {
                css = "unknown-cell";
                sign = "?";
                digit = freezedSumbiossions[team.id][result.id].tries;
            }
            return "<td class = '" + css + "' align = 'center'>" + sign + digit + "</td>"
        };

        var renderTeam = function (team, index) {
            var row = "";
            angular.forEach(team.result, function (result, ind) {
                row += renderCell(result, team);
            });
            return "<tr id = 'team" + team.id + "'><td id = 'team" + team.id + "pos'>" + (index + 1) + "</td><td>" + team.name + "</td><td>" + team.region + "</td>" +
                row +
                "<td>" + team.solved_tasks + "</td><td>" + team.penalty + "</td></tr>";
        };

        var renderTaskList = function (tasks) {
            var content = "";
            angular.forEach(tasks, function (task, index) {
                content += "<th class='taskHead'>" + task.shortName + "</th>";
            });
            $("#regionCol").after(content);
        };

        var scrollToTeam = function (teamId) {
            $('html, body').animate({
                scrollTop: $("#team" + teamId).offset().top - 100
            }, 1000);
        };

        $scope.isUserAuthorized = function () {
            return $cookies.get('authorize') != null;
        };

        var getUnsolvedTasksForTeam = function (teamId) {
            var unsolved = [];
            angular.forEach(data[teamId].result, function (task, index) {
                if (task.result == "UNKNOWN") {
                    unsolved.push(task.id);
                }
            });
            return unsolved;
        };

        var getUnknownTasks = function () {
            var unknownTasks = [];
            angular.forEach($scope.results, function (team, index) {
                var unsolved = getUnsolvedTasksForTeam(team.id);
                if (unsolved.length > 0)
                    unknownTasks.push({
                        'id': team.id,
                        'tasks': unsolved
                    });
            });
            return unknownTasks;
        };

        var parseUnfrozenResults = function (response) {
            var result = {};
            angular.forEach(response, function (submissions, region) {
                    var unfrozenResults = {};
                    angular.forEach(submissions, function (submit, index) {
                        var pos = generateGlobalTeamId(submit.userId, region);
                        if (unfrozenResults[pos] == undefined)
                            unfrozenResults[pos] = {'tasks': {}};
                        if (unfrozenResults[pos].tasks[submit.problemId] == undefined)
                            unfrozenResults[pos].tasks[submit.problemId] = [];

                        var tasks = unfrozenResults[pos].tasks[submit.problemId];
                        if (tasks.length > 0 && tasks[tasks.length - 1].status == "OK")
                            return;

                        submit.tries = tasks.length + 1;
                        unfrozenResults[pos].tasks[submit.problemId].push(submit);
                    });
                    result[region] = unfrozenResults;
                }
            );
            return result;
        };


        var getNextSubmitToUnfog = function () {
            var result = null;
            angular.forEach(frozenSubmitsOrder, function (submit, index) {
                if (result != null) return;
                if (!submit.unfrozen && isRegionSelected(data[submit.team].region)) {
                    frozenSubmitsOrder[index].unfrozen = true;
                    result = submit.id;
                }
            });
            return result;
        };

        $scope.unFreezeNextSubmit = function () {
            var id = getNextSubmitToUnfog();
            if (unfreezed && id != null) {
                var submit = frozenSubmits[id];
                scrollToTeam(submit.team);
                pushSubmitOnline(submit.submit, data[submit.team].region, true);
            }
        };


        $scope.unFreezeResults = function () {
            if (!unfreezed) {
                unfreezed = true;
                $http({
                    url: '/api/frozen-results',
                    method: "GET",
                    params: {}
                }).success(function (response) {
                    $interval.cancel(interval);
                    var res = parseUnfrozenResults(response);
                    angular.forEach(res, function (results, ind) {
                        frozenSubmits[ind] = [];
                        lastUnfrozenSubmitInRegion[ind] = -1;
                    });
                    var id = 0;
                    angular.forEach(getUnknownTasks().reverse(), function (team, index) {
                        angular.forEach(team.tasks, function (task, index) {
                            var tasks = res[data[team.id].region][team.id].tasks[task];
                            frozenSubmits.push({
                                'team': team.id,
                                'submit': tasks[tasks.length - 1],
                                'id': id
                            });
                            frozenSubmitsOrder.push({
                                'id': id,
                                'team': team.id,
                                'unfrozen': false
                            });
                            id++;
                        });
                    });
                });
            }
        };

        var interval = $interval(updateResults, 1000);

        function getTeamPositionOnDisplayedTable(teamId, results) {
            var keepGoing = true,
                position = 0;
            angular.forEach(results, function(team) {
                if(keepGoing) {
                    if(team.id == teamId) keepGoing = false;
                    else if(isRegionSelected(team.region)) position++;
                }
            });
            return position;
        }

        function findTeamPositionInResultArray(teamId, results) {
            var positionInArray = 0,
                keepGoing = true;
            angular.forEach(results, function (team, index) {
                if (keepGoing) {
                    if (team.id == teamId) {
                        positionInArray = index;
                        keepGoing = false;
                    }
                }
            });
            return positionInArray;
        }

        function findPreviousDisplayedTeamPositionInArray(teamId, results) {
            var teamPositionInArray = findTeamPositionInResultArray(teamId, results) - 1;
            while (teamPositionInArray > 0 && !isRegionSelected(results[teamPositionInArray].region)) {
                teamPositionInArray--;
            }
            return teamPositionInArray;
        }
    }]).filter('secondsToDateTime', [function () {
    return function (seconds) {
        return new Date(1970, 0, 1).setSeconds(seconds);
    };
}]);