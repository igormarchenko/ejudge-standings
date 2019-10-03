module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        concat: {
            userjs: {
                src: [
                    'bower_components/jquery/dist/jquery.js',
                    'bower_components/angular/angular.js',
                    'bower_components/angular-route/angular-route.js',
                    'bower_components/angular-sanitize/angular-sanitize.js',
                    'bower_components/angular-animate/angular-animate.js',
                    'bower_components/angular-aria/angular-aria.js',
                    'bower_components/angular-material/angular-material.js',
                    'bower_components/angular-messages/angular-messages.js',
                    'bower_components/angular-datatables/dist/angular-datatables.js',
                    'bower_components/datatables.net/js/jquery.dataTables.js',
                    'bower_components/svg-assets-cache/svg-assets-cache.js',
                    'bower_components/moment/moment.js',
                    'bower_components/sockjs/sockjs.js',
                    'bower_components/sprintf.js/dist/sprintf.min.js',
                    'bower_components/stomp-websocket/lib/stomp.js',
                    'bower_components/angular-moment/angular-moment.js',
                    'bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
                    'bower_components/angular-animate/angular-animate.js',
                    'bower_components/jquery.floatThead/dist/jquery.floatThead.js',
                    'bower_components/angular-ui-select/dist/select.js',
                    'bower_components/underscore/underscore.js',
                    'bower_components/angular-cookies/angular-cookies.js',
                    'bower_components/bootstrap/js/modal.js'
                ],
                dest: "src/main/webapp/static/scripts.js"
            },

            usercss: {
                src: [
                    'bower_components/bootstrap/dist/css/bootstrap.min.css',
                    'bower_components/angular-ui-select/dist/select.css',
                    'bower_components/datatables.net-dt/css/jquery.dataTables.css'
                ],
                dest: "src/main/webapp/static/styles.css"
            }
        },
        cssmin: {
            user: {
                src: 'src/main/webapp/static/styles.css',
                dest: 'src/main/webapp/static/styles.min.css'
            }
        },
        uglify: {
            admin: {
                src: 'src/main/webapp/static/scripts.js',
                dest: 'src/main/webapp/static/scripts.min.js'
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');

    grunt.registerTask('default', ['concat', 'uglify', 'cssmin']);

};