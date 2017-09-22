angular.module('ejudgeStandings',
    ['ejudgeStandings.controllers',
        'ejudgeStandings.services',
        'ejudgeStandings.WebSocketService',
        'ngRoute',
        'ngSanitize',
        'ngAnimate',
        'infinite-scroll',
        'angularMoment',
        'ngMaterial',
        'ngMessages',
        'material.svgAssetsCache'
    ]).config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when("/", {
        templateUrl: "/views/contest-list.html",
        controller: "contestListController"
    }).when("/contest/:contestId", {
        templateUrl: "/views/contest-result.jsp",
        controller: "resultsController"
    }).when("/socket/:contestId", {
        templateUrl: "/views/socket.html",
        controller: "webSocketController"
    }).when("/baylor-export/:contestId", {
        templateUrl: "/views/baylor.html",
        controller: "baylorExportController"
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);