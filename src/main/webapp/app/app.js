angular.module('ejudgeStandings',
    ['ejudgeStandings.controllers',
        'ejudgeStandings.services',
        'ngRoute',
        'ngSanitize',
        'ui.select'
    ]).config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when("/admin/userlist", {
        templateUrl: "views/datasetinfo.html",
        controller: "datasetStatisticsController"
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]).filter('range', function () {
    return function (input, total) {
        total = parseInt(total);
        for (var i = 0; i < total; i++) {
            input.push(i);
        }
        return input;
    };
});