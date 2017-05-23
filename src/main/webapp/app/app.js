angular.module('ejudgeStandings',
    ['ejudgeStandings.controllers',
        'ejudgeStandings.services',
        'ejudgeStandings.WebSocketService',
        'ngRoute',
        'ngSanitize',
        'infinite-scroll'
    ]).config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when("/", {
        templateUrl: "/views/contest-list.html",
        controller: "contestListController"
    }).when("/contest/:contestId", {
        templateUrl: "/views/contest-result.html",
        controller: "resultsController"
    }).when("/socket", {
        templateUrl: "/views/socket.html",
        controller: "webSocketController"
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);