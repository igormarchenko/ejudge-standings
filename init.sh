npm install
cd src/main/webapp/static
browserify dependencies.js -o output.js -p [parcelify -o output.css]