$("#menu-toggle").click(function (e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});
$('#app-content-load').load('/components/panels/teacher/home/teacher-home.html');

function loadPage(page) {
    if (page === 'teacher-home') {
        location.reload();
    }

    if (page === 'teacher-courses') {
        $('#app-content-load').load('/components/panels/teacher/teacher-courses/teacher-courses.html');
    }

}

function getSecondPart(str) {
    return str.split('?')[1];
}

function getUsername(str) {
    return str.split(':')[0];
}

function getPassword(str) {
    return str.split(':')[1];
}

// use the function:
let url = getSecondPart(window.location.href);
let decrypt = atob(url.replace('#', ''));
let usernameHeader = getUsername(decrypt);
let passwordHeader = getPassword(decrypt);