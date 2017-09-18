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

        apiService.sendBaylorFileContent = function (contestId, content) {
            return $http({
                method: 'POST',
                url: '/api/baylor-export/' + contestId,
                data : {
                    'content' : content
                }
            });
        };



        return apiService;
    });