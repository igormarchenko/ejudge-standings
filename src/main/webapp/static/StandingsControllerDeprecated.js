angular.module("standingsPage", ['ui.select']).controller("standingsController",
    ["$scope", '$window', "$http", '$interval', function ($scope, $window, $http, $interval) {
        var data = {};
        var unfreezed = false;
        var freezedSumbiossions = {};
        var frozenSubmits = [];
        var universityNames = [];
        var universityTypes = [];
        var universityInfo = {};
        var lastSuccess = {};
        $scope.selectors = {
            regions: [],
            universityTypes: []
        };

        $scope.results = [];
        $scope.regions = {};
        $scope.lastSuccess = {};
        $scope.lastSubmit = {};
        $scope.tasks = {};
        $scope.regionList = [];
        var lastSubmitOnContest = {};
        var lastSubmit = {};
        $scope.contest = {};
        $scope.submissions = {};

        $scope.init = function () {
            $http({
                url: '/api/init-results',
                method: "GET",
                params: {}
            }).success(function (response) {
                angular.forEach(response, function (region) {
                    angular.forEach(region.teams, function (team) {
                            $scope.regionList.push(team.university.region);
                        }
                    );
                });

                $scope.regionList = $scope.regionList.filter(onlyUnique);
                $scope.selectors.regions = $scope.regionList;

                angular.forEach($scope.regionList, function (region, index) {
                    $scope.regions[region] = index;
                });

                angular.forEach(response, function (contest) {
                    fillContestData(contest);
                });

                $scope.universityTypes = universityTypes.filter(onlyUnique);
                $scope.selectors.universityTypes = $scope.universityTypes;

                $scope.fillDisplayData();
                $scope.updatePage();
            });
        };
        function onlyUnique(value, index, self) {
            return self.indexOf(value) === index;
        }

        $scope.updatePage = function () {
            $(".taskHead").remove();
            $("#results_body").empty();
            renderPage();
            updateLastSuccessSubmit();
            updateLastSubmit();
        };

        $scope.fillDisplayData = function () {
            $scope.results = angular.copy([]);
            angular.forEach(data, function (result) {
                $scope.results.push(result);
            });

            $scope.results.sort(compareTeams);
        };

        function getLastSubmitFromArray(list) {
            var result = null;
            angular.forEach(list, function (submit, university) {
                if (isUniversityTypeSelected(universityInfo[university].type) && isRegionSelected(universityInfo[university].region)) {
                    if (result == null || submit.time > result.time) {
                        result = angular.copy(submit);
                    }
                }
            });
            return result;
        }

        function updateLastSuccessSubmit() {
            $scope.lastSuccess = getLastSubmitFromArray(lastSuccess);
            if (!$scope.$$phase) {
                $scope.$apply();
            }
        }

        function updateLastSubmit() {
            $scope.lastSubmit = getLastSubmitFromArray(lastSubmit);
            if (!$scope.$$phase) {
                $scope.$apply();
            }
        }

        var fillContestData = function (response) {
            $scope.contest.name = response.name;
            $scope.tasks = response.tasks;
            var taskObject = filltasksData(response.tasks);

            angular.forEach(response.teams, function (team) {
                universityTypes.push(team.university.type);
                universityNames.push(team.university.name);
                universityInfo[team.university.name] = angular.copy(team.university);
                data[generateGlobalTeamId(team.id, response.id)] = {
                    'id': generateGlobalTeamId(team.id, response.id),
                    'name': team.name,
                    'region': response.id,
                    'result': angular.copy(taskObject),
                    'solved_tasks': 0,
                    'penalty': 0,
                    'university': team.university
                };
            });
            lastSubmitOnContest[response.id] = -1;
            angular.forEach(response.submissions, function (submission) {
                pushSubmission(submission, response.id);
            });
        };

        var compareTeams = function (a, b) {
            if (a.solved_tasks != b.solved_tasks)
                return b.solved_tasks - a.solved_tasks;

            return a.penalty - b.penalty;
        };

        var filltasksData = function (tasks) {
            var taskObject = {};
            angular.forEach(tasks, function (task) {
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

        var pushSubmission = function (submission, contestId) {
            var teamId = generateGlobalTeamId(submission.userId, contestId);
            var previousResult = data[teamId].result[submission.problemId].result;
            if (submission.status === "OK") {
                lastSuccess[data[teamId].university.name] = {
                    'team': data[teamId].name,
                    'problem': $scope.tasks[submission.problemId - 1].shortName,
                    'time': submission.time
                };
            }
            lastSubmit[data[teamId].university.name] = {
                'team': data[teamId].name,
                'problem': $scope.tasks[submission.problemId - 1].shortName,
                'time': submission.time
            };
            if (previousResult != 'OK') {
                if (submission.status === "OK")
                    newSolvedTask(submission, contestId);
                else
                    wrongTry(submission, contestId);
            }
            if (submission.status == "UNKNOWN") {
                if (freezedSumbiossions[teamId] == undefined)
                    freezedSumbiossions[teamId] = {};
                if (freezedSumbiossions[teamId][submission.problemId] == undefined)
                    freezedSumbiossions[teamId][submission.problemId] = {'tries': 0};

                freezedSumbiossions[teamId][submission.problemId].tries++;
            }
            lastSubmitOnContest[contestId] = submission.runId;
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
            data[teamId].result[submission.problemId].time = submission.time;
            data[teamId].result[submission.problemId].result = submission.status;
            data[teamId].result[submission.problemId].tries += (submission.tries == undefined) ? 1 : submission.tries;
            data[teamId].penalty += Math.ceil(submission.time / 60.0) +
                (data[teamId].result[submission.problemId].tries - 1) * 20;
        };


        function generateGlobalTeamId(userId, contestId) {
            return userId + 100000 * contestId;
        }

        function contestIdForUser(userId) {
            return Math.floor(userId / 100000);
        }

        function pushSubmitOnline(submit, contestId) {
            var teamId = generateGlobalTeamId(submit.userId, contestId);
            var teamPosInResultsArray = findTeamPositionInResultArray(teamId, $scope.results);
            var teamPositionOnDisplay = getTeamPositionOnDisplayedTable(teamId, $scope.results);

            $scope.results[teamPosInResultsArray] = pushSubmission(submit, contestId);
            $("#team" + $scope.results[teamPosInResultsArray].id).replaceWith($.parseHTML(renderTableRow($scope.results[teamPosInResultsArray], teamPositionOnDisplay)));
            updateLastSuccessSubmit();
            updateLastSubmit();
            var interval = setInterval(function () {
                if (!canSlideUp(teamId)) {
                    clearInterval(interval);
                } else {
                    swapTeams(teamId);
                }
            }, 1000);

        }

        var canSlideUp = function (teamId) {
            var previousTeamPos = findPreviousDisplayedTeamPositionInArray(teamId, $scope.results);
            return previousTeamPos >= 0 && compareTeams($scope.results[findTeamPositionInResultArray(teamId, $scope.results)], $scope.results[previousTeamPos]) < 0;
        };

        var swapTeams = function (teamId) {
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
        };

        var updateResults = function () {
            $http({
                url: '/api/results',
                method: "GET",
                params: {last_submit: lastSubmitOnContest}
            }).success(function (data) {
                angular.forEach(data, function (region, ind) {
                    angular.forEach(region, function (submission) {
                        pushSubmitOnline(submission, ind);
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
            return $scope.selectors.regions.indexOf(region) >= 0;
        };

        var isUniversityTypeSelected = function (type) {
            return $scope.selectors.universityTypes.indexOf(type) >= 0;
        };

        var isTeamDisplayed = function (team) {
            return isRegionSelected(team.university.region) && isUniversityTypeSelected(team.university.type);
        };

        var renderTable = function (results) {
            var content = "";
            var pos = 0;
            angular.forEach(results, function (team) {
                if (isTeamDisplayed(team))
                    content += renderTableRow(team, pos++);
            });

            $("#results_body").append(content);

        };

        var renderCell = function (result, team) {
            var css = "empty-cell";
            var digit = "";
            var sign = "";
            var date = "";

            if (result.result === "OK") {
                css = 'OK-cell';
                sign = "+";
                if (result.tries > 1)
                    digit = result.tries - 1;
                var time = Math.ceil(result.time / 60);
                var minutes = time % 60;
                var hours = Math.floor(time / 60);
                date = "<div class = 'row' style='font-size:10px'>" + hours + ":" + ((minutes < 10) ? "0" : "") + minutes + "</div>";

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
            if (result.tries == 0) {
                date = "";
            }
            return "<td class = '" + css + "' align = 'center'><div class = 'row'>" + sign + digit + date + "</div></td>"
        };


        function renderTeamSolverTasks(team) {
            var tasks = "";
            angular.forEach(team.result, function (result) {
                tasks += renderCell(result, team);
            });
            return tasks;
        }

        function renderTeamInfo(team, position) {
            var teamIdCol = "<td id = 'team" + team.id + "pos'>" + (position + 1) + "</td>";
            var universityNameCol = "[" + team.university.name + "]";
            var teamNameCol = "<td style='width:500px'>" + team.name + " " + universityNameCol + "</td>";
            var regionCol = "<td>" + team.university.region + "</td>";
            var solvedTaskCol = "<td>" + team.solved_tasks + "</td>";
            var penaltyCol = "<td>" + team.penalty + "</td>";
            return teamIdCol + teamNameCol + regionCol + renderTeamSolverTasks(team) + solvedTaskCol + penaltyCol;
        }

        function renderTableRow(team, position) {
            return "<tr id = 'team" + team.id + "'>" + renderTeamInfo(team, position) + "</tr>"
        }

        var renderTaskList = function (tasks) {
            var content = "";
            angular.forEach(tasks, function (task) {
                content += "<th class='taskHead'>" + task.shortName + "</th>";
            });
            $("#solvedTasks").before(content);
        };

        var scrollToTeam = function (teamId) {
            return $('html, body').animate({
                scrollTop: $("#team" + teamId).offset().top - 100
            }, 1000);
        };

        $scope.isUserAuthorized = function () {
            return true;
            // return $cookies.get('authorize') != null;
        };

        $scope.unFreezeNextSubmit = function () {
            if (unfreezed) {
                var nextSubmit = getNextFrozenResult($scope.results);
                if (nextSubmit) {
                    scrollToTeam(nextSubmit.team).promise().then(function () {
                        pushSubmitOnline(frozenSubmits[nextSubmit.team][nextSubmit.task], contestIdForUser(nextSubmit.team));
                    });
                }
            }
        };

        $scope.unFreezeResults = function () {
            if (!unfreezed) {
                $interval.cancel(interval);
                $http({
                    url: '/api/frozen-results',
                    method: "GET",
                    params: {}
                }).success(function (response) {
                    unfreezed = true;
                    angular.forEach(response, function (regionResults, region) {
                        angular.forEach(regionResults, function (teamResult) {
                            var teamId = generateGlobalTeamId(teamResult.userId, region);
                            if (frozenSubmits[teamId] == undefined)
                                frozenSubmits[teamId] = {};
                            var tries = 0;
                            if (frozenSubmits[teamId][teamResult.problemId] != undefined && frozenSubmits[teamId][teamResult.problemId].status != "OK") {
                                tries = frozenSubmits[teamId][teamResult.problemId].tries;
                            }
                            frozenSubmits[teamId][teamResult.problemId] = teamResult;
                            frozenSubmits[teamId][teamResult.problemId].tries = tries + 1;
                        });
                    });
                });
            }
        };

        var interval = $interval(updateResults, 1000);

        function getFirstFrozenTaskForTeam(tasks) {
            var keepGoing = true,
                result = null;
            angular.forEach(tasks, function (task) {
                if (keepGoing) {
                    if (task.result == "UNKNOWN") {
                        result = angular.copy(task);
                        keepGoing = false;
                    }
                }
            });
            return result;
        }

        function getNextFrozenResult(results) {
            var keepGoing = true,
                result = null;
            var reversed = angular.copy(results).reverse();
            angular.forEach(reversed, function (team) {
                if (keepGoing && isTeamDisplayed(team)) {
                    var res = getFirstFrozenTaskForTeam(team.result);
                    if (res != null) {
                        result = {
                            'team': team.id,
                            'task': res.id
                        };
                        keepGoing = false;
                    }
                }
            });
            return result;
        }

        function getTeamPositionOnDisplayedTable(teamId, results) {
            var keepGoing = true,
                position = 0;
            angular.forEach(results, function (team) {
                if (keepGoing) {
                    if (team.id == teamId) keepGoing = false;
                    else if (isTeamDisplayed(team)) position++;
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
            while (teamPositionInArray >= 0 && !isTeamDisplayed(results[teamPositionInArray])) {
                teamPositionInArray--;
            }
            return teamPositionInArray;
        }
    }]).filter('secondsToDateTime', [function () {
    return function (seconds) {
        return new Date(1970, 0, 1).setSeconds(seconds);
    };
}]);