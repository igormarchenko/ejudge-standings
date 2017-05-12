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

        apiService.contestList = function () {
            return $http({
                method: 'GET',
                url: '/api/contestlist'
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

        apiService.saveContest = function (contest) {
            return $http({
                method: 'POST',
                url: '/admin/savecontest',
                data: {'data': contest}
            });
        };

        apiService.removeUniversity = function (universityId) {
            return $http({
                method: 'GET',
                url: '/admin/deleteuniversity/' + universityId
            });

        };

        apiService.removeContest = function (contestId) {
            return $http({
                method: 'GET',
                url: '/admin/deletecontest/' + contestId
            });

        };

        return apiService;
    });