angular.module("standingsPage", ['ui.select', 'ngCookies']).controller("standingsController",
    ["$scope", '$window', "$http", '$interval', '$cookies', function ($scope, $window, $http, $interval, $cookies) {
        var data = {};
        var unfreezed = false;
        var freezedSumbiossions = {};
        var frozenSubmits = [];
        var universityNames = [];
        var universityTypes = [];
        var regionList = [];
        $scope.results = [];
        $scope.regionSelector = {};
        $scope.regions = {};
        $scope.lastSuccess = {};
        $scope.lastSubmit = {};
        $scope.tasks = {};
        $scope.regionList = [];
        $scope.selectedUniversities = {};
        $scope.selectedUniversitiesTypes = {};
        var lastSubmit = {};
        var contests = [];

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
                    contests.push(region.id);
                    angular.forEach(region.teams, function (team, index) {
                            regionList.push(team.university.region);
                        }
                    );
                    // fillRegionData(region);
                });

                $scope.regionList = regionList.filter(onlyUnique);
                $scope.regionSelector.data = $scope.regionList;
                angular.forEach($scope.regionList, function (region, index) {
                    $scope.regions[region] = index;
                });

                // console.log( $scope.regions);
                angular.forEach(response, function (region, index) {
                    fillRegionData(region);
                });

                $scope.universityNames = universityNames.filter(onlyUnique);
                $scope.selectedUniversities.data = $scope.universityNames;

                $scope.universityTypes = universityTypes.filter(onlyUnique);
                $scope.selectedUniversitiesTypes.data = $scope.universityTypes;

                // console.log(data);
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
                result = angular.copy(submit);
                // if (isRegionSelected(ind) && (result == null || result.time < submit.time))
                //     result = angular.copy(submit);
            });
            return result;
        };

        $scope.getLastSubmit = function () {

            var result = null;
            angular.forEach($scope.lastSubmit, function (submit, ind) {
                // console.log(submit);
                result = angular.copy(submit);
                // if (isRegionSelected(ind) && (result == null || result.time < submit.time))
                //     result = angular.copy(submit);
            });
            return result;
        };

        var fillRegionData = function (response) {
            $scope.contest.name = response.name;
            $scope.tasks = response.tasks;
            // console.log(response);
            // debugger;
            var taskObject = filltasksData(response.tasks);

            angular.forEach(response.teams, function (team, index) {
                universityTypes.push(team.university.type);
                universityNames.push(team.university.name);
                // regionList.push(team.university.region);

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
            // console.log(data);
            angular.forEach(response.submissions, function (submission, index) {
                // console.log(submission);
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
            // debugger;
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
            data[teamId].result[submission.problemId].time = submission.time;
            data[teamId].result[submission.problemId].result = submission.status;
            data[teamId].result[submission.problemId].tries += (submission.tries == undefined) ? 1 : submission.tries;
            data[teamId].penalty += Math.ceil(submission.time / 60.0) +
                (data[teamId].result[submission.problemId].tries - 1) * 20;
        };


        function generateGlobalTeamId(userId, contestId) {
            // debugger;
            return userId + 100000 * contestId;
        }

        function pushSubmitOnline(submit, region) {
            var teamId = generateGlobalTeamId(submit.userId, region);
            var teamPosInResultsArray = findTeamPositionInResultArray(teamId, $scope.results);
            var teamPositionOnDisplay = getTeamPositionOnDisplayedTable(teamId, $scope.results);

            $scope.results[teamPosInResultsArray] = pushSubmission(submit, region);
            $("#team" + $scope.results[teamPosInResultsArray].id).replaceWith($.parseHTML(renderTeam($scope.results[teamPosInResultsArray], teamPositionOnDisplay)));

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
                params: {last_submit: lastSubmit}
            }).success(function (data) {
                angular.forEach(data, function (region, ind) {
                    angular.forEach(region, function (submission, index) {
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
            return $scope.regionSelector.data.indexOf(region) >= 0;
        };

        var isUniversityTypeSelected = function (type) {
            return $scope.selectedUniversitiesTypes.data.indexOf(type) >= 0;
        };

        var isUniversitySelected = function (name) {
            return $scope.selectedUniversities.data.indexOf(name) >= 0;
        };

        var isTeamDisplayed = function (team) {
            // return true;
            return isRegionSelected(team.university.region) && isUniversityTypeSelected(team.university.type);
        };

        var renderTable = function (results) {
            var content = "";
            var pos = 0;
            angular.forEach(results, function (team, index) {
                if (isTeamDisplayed(team))
                    content += renderTeam(team, pos++);
            });
            $("#results_body").append(content);
        };

        var renderCell = function (result, team) {
            var css = "empty-cell";
            var digit = "";
            var sign = "";
            var date = new Date(null);
            date.setSeconds(result.time);
            date = date.toISOString().substr(14, 5);

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
            if (result.tries == 0) {
                date = "";
            }
            return "<td class = '" + css + "' align = 'center'><div class = 'row'>" + sign + digit + "</div><div class = 'row' style='font-size:10px'>" + date + "</div></td>"
        };

        var renderTeam = function (team, index) {
            var row = "";
            angular.forEach(team.result, function (result, ind) {
                row += renderCell(result, team);
            });
            var teamIdCol = "<td id = 'team" + team.id + "pos'>" + (index + 1) + "</td>";
            var universityNameCol = "[" + team.university.name + "]";
            var teamNameCol = "<td>" + team.name + " " + universityNameCol + "</td>";
            var universityNameCol = "<td>" + team.university.name + "</td>";
            var universityTypeCol = "<td>" + team.university.type + "</td>";
            var regionCol = "<td>" + team.university.region + "</td>";
            var solvedTaskCol = "<td>" + team.solved_tasks + "</td>";
            var penaltyCol = "<td>" + team.penalty + "</td>";
            return "<tr id = 'team" + team.id + "'>" + teamIdCol + teamNameCol + regionCol + row + solvedTaskCol + penaltyCol + "</tr>"
        };

        var renderTaskList = function (tasks) {
            var content = "";
            angular.forEach(tasks, function (task, index) {
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
            return $cookies.get('authorize') != null;
        };

        $scope.unFreezeNextSubmit = function () {
            if (unfreezed) {
                var nextSubmit = getNextFrozenResult($scope.results);
                if (nextSubmit) {
                    scrollToTeam(nextSubmit.team).promise().then(function () {
                        pushSubmitOnline(frozenSubmits[nextSubmit.team][nextSubmit.task], data[nextSubmit.team].region);
                    });
                }
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
                    angular.forEach(response, function (regionResults, region) {
                        angular.forEach(regionResults, function (teamResult, index) {
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