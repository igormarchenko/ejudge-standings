angular.module("ejudgeStandings.WebSocketService", []).service("WebSocketService", function($q, $timeout) {

    var service = {}, listener = $q.defer(), socket = {
        client: null,
        stomp: null
    };

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/listener";
    service.UPDATE_LISTENER_URL = "/updates/get-updates/";
    service.CONTEST_ID = 0;

    service.receive = function() {
        return listener.promise;
    };

    var reconnect = function() {
        $timeout(function() {
            connect();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function(data) {
        return JSON.parse(data);
    };

    var startListener = function() {
        socket.stomp.subscribe(service.UPDATE_LISTENER_URL + service.CONTEST_ID, function(data) {
            listener.notify(getMessage(data.body));
        });
    };

    var connect = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };
    service.initialize = function(contestId) {
        this.CONTEST_ID = contestId;
        connect();
    };
    return service;
});