angular.module('ejudgeStandings',
    ['ejudgeStandings.controllers',
        'ejudgeStandings.services',
        'ejudgeStandings.WebSocketService',
        'ngRoute',
        'ngSanitize',
        'ngAnimate',
        'infinite-scroll',
        'angularMoment'
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
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);