$("#menu-toggle").click(function (e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});

$('#app-content-load').load('/components/panels/student/home/student-home.html');

function loadPage(page) {
    if (page === 'student-home') {
        location.reload();
    }

    if (page === 'student-courses') {
        $('#app-content-load').load('/components/panels/student/student-courses/student-courses.html');
    }

    if (page === 'start-exam') {
        $('#app-content-load').load('/components/panels/student/start-exam/start-exam.html');
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
