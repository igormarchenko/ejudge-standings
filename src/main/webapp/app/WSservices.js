angular.module("ejudgeStandings.WebSocketService").service("WebSocketService", function($q, $timeout) {

    var service = {}, listener = $q.defer(), socket = {
        client: null,
        stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/listener";
    service.CHAT_TOPIC = "/topic/message";
    service.CHAT_BROKER = "/ws/listener";

    service.receive = function() {
        return listener.promise;
    };

    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function(data) {
        var message = JSON.parse(data), out = {};
        out.message = message.message;
        out.time = new Date(message.time);
        if (_.includes(messageIds, message.id)) {
            out.self = true;
            messageIds = _.remove(messageIds, message.id);
        }
        return out;
    };

    var startListener = function() {
        socket.stomp.subscribe(service.CHAT_TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };

    var initialize = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
});