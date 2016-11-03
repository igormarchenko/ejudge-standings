angular.module('ejudgeStandings.services', [])
    .factory('ejudgeStandingsApiService', function ($http) {
        var apiService = {};

        apiService.teamList = function () {
            return $http({
                method: 'POST',
                url: '/teamList'
            })
        };

        return apiService;
    });