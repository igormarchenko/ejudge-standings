angular.module('ejudgeStandings',
    ['ejudgeStandings.controllers',
        'ejudgeStandings.services',
        'ngRoute',
        'ngSanitize'
    ]).config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when("/admin/teams", {
        templateUrl: "/views/teams.html",
        controller: "teamsController"
    }).when("/admin/universities", {
        templateUrl: "/views/universities.html",
        controller: "universityController"
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);