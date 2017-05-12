angular.module('ejudgeStandings.services', [])
    .factory('ejudgeApiService', function ($http) {
        var apiService = {};

        apiService.contestList = function () {
            return $http({
                method: 'GET',
                url: '/api/contestlist'
            });
        };

        apiService.contestData = function (contestId) {
            return $http({
                method: 'GET',
                url: '/api/init-results/' + contestId
            });
        };

        return apiService;
    });