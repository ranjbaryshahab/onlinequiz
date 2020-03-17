$(document).ready(function () {
    $(".menu").click(function () {
        let submenu = $(this).next();
        submenu.slideToggle(500);
        submenu.siblings("ul").hide(500);
    });
    $("#menu-ul").append('<li class="menu-li" >مشاهده همه اعضا</li>');
});

$("#menu-toggle").click(function (e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});
$('#app-content-load').load('/components/panels/manager/home/manager-home.html');

function loadPage(page) {
    if (page === 'personal-information-form') {
        $('#app-content-load').load('/components/panels/guest/personal-information-form/personal-information-form.html');
    }

    if (page === 'manager-home') {
        location.reload();
    }

    if (page === 'new-users-list') {
        $('#app-content-load').load('/components/panels/manager/user-management/new-users-list/new-users-list.html');
    }

    if (page === 'users-list') {
        $('#app-content-load').load('/components/panels/manager/user-management/users-list/users-list.html');
    }

    if (page === 'lessons-list') {
        $('#app-content-load').load('/components/panels/manager/lesson-management/lessons-list.html');
    }

    if (page === 'teacher-management') {
        $('#app-content-load').load('/components/panels/manager/user-management/teacher-management/teacher-management.html');
    }

    if (page === 'student-management') {
        $('#app-content-load').load('/components/panels/manager/user-management/student-management/student-management.html');
    }

    if (page === 'course-management') {
        $('#app-content-load').load('/components/panels/manager/course-management/course-management.html');
    }

    if (page === 'add-lesson-to-course') {
        $('#app-content-load').load('/components/panels/manager/course-management/add-lesson-to-course/add-lesson-to-course.html');
    }

    if(page==='add-teacher-to-course'){
        $('#app-content-load').load('/components/panels/manager/course-management/add-teacher-to-course/add-teacher-to-course.html');
    }

    if(page==='add-students-to-course'){
        $('#app-content-load').load('/components/panels/manager/course-management/add-students-to-course/add-students-to-course.html');
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



