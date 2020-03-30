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

    if (page === 'question-management') {
        $('#app-content-load').load('/components/panels/teacher/teacher-courses/question-management/question-management.html');
    }

    if (page === 'teacher-question-bank') {
        $('#app-content-load').load('/components/panels/teacher/teacher-courses/question-management/add-question-to-exam-from-question-bank/add-question-to-exam-from-question-bank.html');
    }

    if (page === 'student-exam-page') {
        $('#app-content-load').load('/components/panels/teacher/teacher-courses/see-student-exam-page/see-student-exam-page.html');
    }
}

function getRole(str) {
    return str.split('&')[1].split('=')[1];
}

function getSecondPart(str) {
    return str.split('@')[1];
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
window.loginRole = getRole(window.location.href);
let usernameHeader = getUsername(decrypt);
let passwordHeader = getPassword(decrypt);
