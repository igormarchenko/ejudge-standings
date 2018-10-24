global.$ = global.jQuery = require('jquery');
global.angular = require('angular');
require('angular-animate');
require('bootstrap');
require('angular-resource');
require('angular-route');

global.moment = require('moment');
require('angular-moment');

var Stomp = require('stompjs');
global.SockJS = require('sockjs-client');

var ngMaterial = require('angular-material');
var ngMessages = require('angular-messages');

var InfiniteScroll = require('ng-infinite-scroll');
var ngSanitize = require('angular-sanitize');

var sprintf = require("sprintf-js").sprintf;
require('svg-assets-cache');
// require('angular-datatables');
// require('datatables.net-dt')( window, jQuery );
require('ui-select');
require('datatables.net')( window, jQuery );
