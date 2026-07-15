const {src, dest, watch, series} = require('gulp');
const sass = require('gulp-sass')(require('sass'));
const postcss = require('gulp-postcss');
const cssnano = require('cssnano');
const terser = require('gulp-terser');
const browser_sync = require('browser-sync').create();

// -- gulp tasks
// scss task
function scssTask(){
    return src('app/scss/**/*.scss', { sourcemaps: true})
        .pipe(sass())
        .pipe(postcss([cssnano()]))
        //.pipe(dest('dist', {sourcemaps: '.'}));
        .pipe(dest('src/main/resources/static/dist', {sourcemaps: '.'}));
}

// js task
function jsTask() {
    return src('app/js/**/*.js', {sourcemaps: true})
        .pipe(terser())
        //.pipe(dest('dist', {sourcemaps: '.'}));
        .pipe(dest('src/main/resources/static/dist', {sourcemaps: '.'}));
}

//browser sync task
function browserSyncServe(cb) {
    browser_sync.init({
        server: {
            baseDir: '.'
        }
    });
    cb();
}

function browserSyncReload(cb) {
    browser_sync.reload()
    cb();
}

// watch task
function watchTask() {
    watch('*.html', browserSyncReload);
    watch(['app/scss/**/*.scss', 'app/js/**/*.js'], series(scssTask, jsTask, browserSyncReload));
}

//default gulp task
exports.default = series(
    scssTask,
    jsTask,
    browserSyncServe,
    watchTask
);

