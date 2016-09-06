angular.module("standingsPage", ['ui.select']).controller("standingsController",
    ["$scope", '$window', "$http", '$interval', function ($scope, $window, $http, $interval) {
        var mul = 100000;
        var data = {};
        $scope.results = [];

        $scope.regionSelector = {};

        $scope.regions = {};
        $scope.lastSuccess = {};
        $scope.lastSubmit = {};
        $scope.tasks = {};
        $scope.regionList = [];
        var lastSubmit = {};

        var order = {};

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
            order = angular.copy({});
            angular.forEach(data, function (result, index) {
                $scope.results.push(result);
            });

            $scope.results.sort(compareTeams);

            angular.forEach($scope.results, function (team, index) {
                order[team.id] = index;
            });
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
                data[team.id + mul * $scope.regions[response.region]] = {
                    'id': team.id + mul * $scope.regions[response.region],
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
            var teamId = submission.userId + mul * $scope.regions[region];
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
                wrongTry(submission, region);
            }

            if (submission.status === "OK" && previousResult != "OK") {
                newSolvedTask(submission, region);
            }

            lastSubmit[region] = submission.runId;
            return data[teamId];
        };

        var wrongTry = function (submission, region) {
            var teamId = submission.userId + mul * $scope.regions[region];
            data[teamId].result[submission.problemId].result = submission.status;
            if(submission.status != "UNKNOWN") {
                data[teamId].result[submission.problemId].penalty = Math.ceil(submission.time / 60.0);
                data[teamId].result[submission.problemId].time = submission.time;
                data[teamId].result[submission.problemId].tries++;
            }
        };

        var newSolvedTask = function (submission, region) {
            var teamId = submission.userId + mul * $scope.regions[region];
            data[teamId].solved_tasks++;
            data[teamId].penalty += Math.ceil(submission.time / 60.0) +
                (data[teamId].result[submission.problemId].tries - 1) * 20;
        };

        var pushSubmitOnline = function (submit, region) {
            var teamId = submit.userId + mul * $scope.regions[region];
            var teamPos = order[teamId];
            $scope.results[teamPos] = pushSubmission(submit, region);
            $("#team" + $scope.results[teamPos].id).replaceWith($.parseHTML(renderTeam($scope.results[teamPos], teamPos)));

            var interval = setInterval(function () {
                if (!canSlideUp(teamPos)) {
                    clearInterval(interval);
                } else {
                    swapTeams(teamPos);
                    teamPos--;
                }
            }, 1000);
        };

        var canSlideUp = function (teamPos) {
            return teamPos > 0 && compareTeams($scope.results[teamPos], $scope.results[teamPos - 1]) < 0;
        };

        var swapTeams = function (teamPos) {
            var temp = $scope.results[teamPos];
            $scope.results[teamPos] = $scope.results[teamPos - 1];
            $scope.results[teamPos - 1] = temp;

            var previousTeam = $scope.results[teamPos - 1].id;
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

        var renderTable = function (results) {
            var content = "";
            var pos = 0;
            angular.forEach(results, function (team, index) {
                if (isRegionSelected(team.region))
                    content += renderTeam(team, pos++);
            });
            $("#results_body").append(content);
        };

        var renderCell = function (result) {
            var css = "empty-cell";
            var digit = "";
            var sign = "";
            if (result.result === "OK") {
                css = 'OK-cell';
                sign = "+";
                if (result.tries > 1)
                    digit = result.tries - 1;
            }
            if (result.result === "WA") {
                css = 'WA-cell';
                sign = "-";
                digit = result.tries;
            }
            if (result.result === "UNKNOWN") {
                css = "unknown-cell";
                sign = "?";
            }
            return "<td class = '" + css + "' align = 'center'>" + sign + digit + "</td>"
        };

        var renderTeam = function (team, index) {
            var row = "";
            angular.forEach(team.result, function (result, ind) {
                row += renderCell(result);
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
        $interval(updateResults, 6000);
    }]).filter('secondsToDateTime', [function () {
    return function (seconds) {
        return new Date(1970, 0, 1).setSeconds(seconds);
    };
}]);