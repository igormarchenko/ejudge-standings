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
    .controller('baylorExportController', function ($scope, $routeParams, $window, ejudgeApiService) {
        $scope.content = "";
        $scope.result = "";
        $scope.sendBaylorData = function () {
            ejudgeApiService.sendBaylorFileContent($routeParams.contestId, $scope.content).then(
                function (response) {
                    $scope.result = response.data;
                }
            );
        };

        $scope.redirectToContestPage = function () {
            $window.location.href = '/contest/' + $routeParams.contestId;
        };

    })
    .controller('resultsController', function ResultsController($scope, $routeParams, $window, $mdDialog, ejudgeApiService, WebSocketService) {
        initContestData();
        var data = {};
        $scope.display = [];
        $scope.scrollDisabled = true;
        $scope.universities = [];
        $scope.regions = [];
        $scope.selectedUniversityTypes = [];
        $scope.selectedRegions = [];
        $scope.selectedUniversities = [];
        $scope.filterApplied = false;


        $scope.loadMore = function () {
            var batchSize = 100;
            if (data.results.length > 0) {
                for (var i = 0; i < batchSize && i < data.results.length; i++) {
                    $scope.display.push(data.results[i]);
                }
                data.results.splice(0, Math.min(batchSize, data.results.length));
                return true;
            } else {
                $scope.scrollDisabled = true;
                return false;
            }
        };

        $scope.filterTeams = function () {
            $mdDialog.show({
                scope: $scope,
                preserveScope: true,
                templateUrl: 'dialog1.tmpl.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: true
            });
        };

        $scope.redirectToExportPage = function () {
            $window.location.href = '/baylor-export/' + $routeParams.contestId;
        };

        function parseDate(date) {
            return moment().year(date.year).month(date.monthValue).date(date.dayOfMonth).hour(date.hour).minute(date.minute).second(date.second).toDate();
        }

        $scope.teamDisplay = function (team) {
            $scope.filterApplied = !($scope.selectedUniversityTypes.length === 0 &&
                $scope.selectedRegions.length === 0 &&
                $scope.selectedUniversities.length === 0);

            if(!$scope.filterApplied){
                return true;
            }
            if (team.participant.university) {
                var data = team.participant.university;
                if ($scope.selectedRegions.indexOf(data.region) !== -1) return true;
                if ($scope.selectedUniversityTypes.indexOf(data.type) !== -1) return true;
                if ($scope.selectedUniversities.indexOf(data.name) !== -1) return true;
            }
            return false;
        };

        function initContestData() {
            WebSocketService.initialize($routeParams.contestId);
            ejudgeApiService.contestData($routeParams.contestId).then(function (response) {
                data = response.data;
                generateUniversityData(data);
                $scope.contest = {
                    'name': data.name,
                    'tasks': data.tasks,
                    'startTime': parseDate(data.startTime),
                    'stopTime': parseDate(data.stopTime),
                    'currentTime': parseDate(data.currentTime)
                };
                $scope.scrollDisabled = false;
            });

        }

        function generateUniversityData(data) {
            var universities = [];
            var regions = [];
            var universityTypes = [];

            angular.forEach(data.results, function (result) {
                if (result.participant.university) {
                    universities.push(result.participant.university.name);
                    if(result.participant.university.region) {
                        regions.push(result.participant.university.region);
                    }
                    if(result.participant.university.type) {
                        universityTypes.push(result.participant.university.type);
                    }
                }
            });
            $scope.universities = $.unique(universities).sort();
            $scope.regions = $.unique(regions).sort();
            $scope.universityTypes = $.unique(universityTypes).sort();
        }

        $scope.formatTime = function (minutes) {
            return sprintf("%02d:%02d", minutes / 60, minutes % 60);
        };

        slideUp = function (array, index, increment) {
            var temp = array[index];
            array[index] = array[index + increment];
            array[index + increment] = temp;
        };

        teamUp = function (index) {
            if (index > 0 && index < $scope.display.length)
                slideUp($scope.display, index, -1);
        };

        teamDown = function (index) {
            if (index > 0 && index < $scope.display.length)
                slideUp($scope.display, index, 1);
        };

        slideTeam = function (startPos, endPos) {
            if (startPos !== endPos) {
                var index = startPos;
                var interval = setInterval(function () {

                    $scope.$apply(function () {
                        if(startPos > endPos) {
                            teamUp(index);
                            index--;
                        } else {
                            teamDown(index);
                            index++;
                        }
                    });

                    if (index === endPos) clearInterval(interval);
                }, 1200);
            }
        };


        $scope.hide = function () {
            $mdDialog.hide();
        };

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.applyFilter = function () {
            $scope.filterApplied = true;
            $scope.cancel();
        };

        $scope.discardFilter = function () {
            $scope.filterApplied = false;
            $scope.selectedUniversityTypes.length = [];
            $scope.selectedRegions.length = [];
            $scope.selectedUniversities.length = [];
            // $scope.cancel();
        };

        WebSocketService.receive().then(null, null, function (response) {
            angular.forEach(response.updates, function (team) {
                $scope.display[team.previousPlace] = team.result;
                slideTeam(team.previousPlace, team.currentPlace);

            });
        });
    });