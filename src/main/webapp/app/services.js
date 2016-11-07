angular.module('ejudgeStandings.services', [])
    .factory('ejudgeStandingsApiService', function ($http) {
        var apiService = {};

        apiService.teamList = function () {
            return $http({
                method: 'GET',
                url: '/api/teamlist'
            });
        };


        apiService.universityList = function () {
            return $http({
                method: 'GET',
                url: '/api/universitylist'
            });
        };

        apiService.removeTeam = function (teamId) {
            return $http({
                method: 'GET',
                url: '/admin/deleteteam/' + teamId
            });
        };

        apiService.saveTeam = function (team) {
            return $http({
                method: 'POST',
                url: '/admin/saveteam',
                data: {'data': team}
            });
        };

        apiService.saveUniversity = function (university) {
            return $http({
                method: 'POST',
                url: '/admin/saveuniversity',
                data: {'data': university}
            });
        };

        apiService.removeUniversity = function (universityId) {
            return $http({
                method: 'GET',
                url: '/admin/deleteuniversity/' + universityId
            });

        };

        return apiService;
    });