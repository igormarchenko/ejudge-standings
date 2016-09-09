module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        concat: {
            userjs: {
                src: [
                    'bower_components/jquery/dist/jquery.js',
                    'bower_components/angular/angular.js',
                    'bower_components/angular-animate/angular-animate.js',
                    'bower_components/jquery.floatThead/dist/jquery.floatThead.js',
                    'bower_components/angular-ui-select/dist/select.js',
                    'bower_components/angular-cookies/angular-cookies.js'
                ],
                dest: "src/main/webapp/static/scripts.js"
            },

            usercss: {
                src: [
                    'bower_components/bootstrap/dist/css/bootstrap.min.css',
                    'bower_components/angular-ui-select/dist/select.css'
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