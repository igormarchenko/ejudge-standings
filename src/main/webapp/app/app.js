angular.module('ejudgeStandings',
    ['ejudgeStandings.controllers',
        'ejudgeStandings.services',
        'ngRoute',
        'ngSanitize'
    ]).config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when("/", {
        templateUrl: "/views/contest-list.html",
        controller: "contestListController"
    }).when("/contest/:contestId", {
        templateUrl: "/views/contest-result.html",
        controller: "resultsController"
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);