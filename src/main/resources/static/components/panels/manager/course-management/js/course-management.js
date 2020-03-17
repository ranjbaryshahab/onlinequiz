$("#courses-list").ready(function () {
    courseListFirstTime(0, 5);
});

var courseData = {
    data: null,
    set lang(data) {
        this.data = data;
    },
    get lang() {
        return this.data;
    }
};

$('#pageSizeCourseList').change(function () {
    var value = $(this).val();
    if (value === null || value === "") {
        value = 5;
    }
    $("#courses-list-paging").empty();
    courseListFirstTime(0, parseInt(value));
});

function createCourse() {
    let newCourse = {
        'courseId': '',
        'courseName': $('#courseNameCreateInput').val(),
        'courseStartDate': $("#courseStartCreateInput").attr('data-gdate'),
        'courseEndDate': $("#courseEndCreateInput").attr('data-gdate')
    };
    $('#courseNameCreateInput').empty();
    $('#courseStartCreateInput').empty();
    $('#courseEndCreateInput').empty();
    jQuery.ajax({
        url: "http://localhost:7777/manager/course/create",
        type: "POST",
        data: JSON.stringify(newCourse),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $("#create-course-modal").modal('hide');
            courseListAfterFirstTime(0, 5);
            showAlert('success', data);
        },
        error: function (errorMessage) {
            $("#create-course-modal").modal('hide');
            showAlert('danger', errorMessage.responseText);
        }
    });
}

function editCourse() {
    let startDate = $("#courseStartEditInput").attr('data-gdate');
    let endDate = $("#courseEndEditInput").attr('data-gdate');
    let editCourse = {
        'courseId': $('#courseIdEditInput').val(),
        'courseName': $('#courseNameEditInput').val(),
        'courseStartDate': startDate,
        'courseEndDate': endDate
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/course/edit",
        type: "POST",
        data: JSON.stringify(editCourse),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $("#create-course-modal").modal('hide');
            courseListAfterFirstTime(0, 5);
            showAlert('success', data);
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseText);
        }
    });
}

function courseListFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/course/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            pagingTable(JSON.parse(data.totalPages));
            prepareTable(data.content);
            courseData.data = data;
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function courseListAfterFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/course/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            prepareTable(data.content);
            courseData.data = data;
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function pagingTable(totalPage) {
    var value = $("#pageSizeCourseList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="courseListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#courses-list-paging").append(middle);
    }
}

function prepareTable(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        let id = data[i].id;
        let courseName = data[i].courseName;
        let courseStartDate = jDateFunctions.prototype.gregorian_to_jalali(new Date(data[i].startCourse));
        let courseEndDate = jDateFunctions.prototype.gregorian_to_jalali(new Date(data[i].endCourse));
        content += "<tr>";
        content += "<td class='text-center'><input class='form-check-input' type='checkbox' value='" + data[i].id + "'></td>";
        content += "<td>" + id + "</td>";
        content += "<td >" + courseName + "</td>";
        content += "<td >" + courseStartDate + "</td>";
        content += "<td >" + courseEndDate + "</td>";
        content += "<td class='align-content-center'>" +
            '<button type="button" class="btn btn-warning btn-sm mr-2" onclick="showEditCourseModal(\'' + id + '\' , \'' + courseName + '\' , \'' + data[i].startCourse + '\' , \'' + data[i].endCourse + '\')">ویرایش</button>' +
            '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showLessonCourseManagementModal(\'' + id + '\')">دروس</button>' +
            '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showTeacherCourseManagementModal(\'' + id + '\')">استاد</button>' +
            '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showStudentsCourseManagementModal(\'' + id + '\')">دانشجویان</button>' +
            '</td>';
        content += "</tr>";
    }
    $('#courses-list-table-body').html(content);
}

function showEditCourseModal(id, courseName, courseStartDate, courseEndDate) {
    $("#courseIdEditInput").val(id);
    $("#courseNameEditInput").val(courseName);
    $("#courseStartEditInput").attr('data-gdate', courseStartDate);
    $("#courseEndEditInput").attr('data-gdate', courseEndDate);
    $("#courseStartEditInput").val(jDateFunctions.prototype.gregorian_to_jalali(new Date(courseStartDate)));
    $("#courseEndEditInput").val(jDateFunctions.prototype.gregorian_to_jalali(new Date(courseEndDate)));
    $("#editCourseModal").modal('show');
}

var submitCourseData = function () {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });

    let action = $('#action option:selected').val();

    if (action === 'DeleteAllSelectedCourse') {
        deleteAllSelectedCourse(checks);
    } else if (action === 'DeleteAllCourse') {
        deleteAllCourse(checks);
    }
    return false;
};

function deleteAllSelectedCourse(checks) {
    let coursesIdsList = {
        "secret": "ajax",
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/manager/course/delete-all-selected",
        type: "POST",
        data: JSON.stringify(coursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function deleteAllCourse() {
    $.ajax({
        url: serverUrl() + "/manager/course/delete-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

// Start Lessons Course Script //
function showLessonCourseManagementModal(id) {
    window.courseIdLessonModal = id;
    let content = '';
    for (let i = 0; i < courseData.data.content.length; i++) {
        let lessons = courseData.data.content[i].lessons;
        if (courseData.data.content[i].id.toString().trim() === id.toString().trim()) {
            for (let j = 0; j < lessons.length; j++) {
                content += "<tr>";
                content += "<td class='text-left'><input type='checkbox' value='" + lessons[j].id + "'></td>";
                content += "<td>" + lessons[j].id + "</td>";
                content += "<td >" + lessons[j].name + "</td>";
                content += "<td >" + lessons[j].topic + "</td>";
                content += "</tr>";
            }
        }
        $('#courseLessonsList').html(content);
    }
    $('#lessonCourseManagementModal').modal('show');
}

function lessonDeleteAction() {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });

    let action = $('#lessonAction option:selected').val();

    if (action === 'DeleteAllSelectedLessonsOfCourse') {
        deleteAllSelectedLessonsOfCourse(checks);
    } else if (action === 'DeleteAllLessonsOfCourse') {
        deleteAllLessonsOfCourse();
    }
    return false;
}

function deleteAllSelectedLessonsOfCourse(checks) {
    let lessonsCoursesIdsList = {
        "secret": window.courseIdLessonModal,
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/manager/course/lessons/delete-all-selected",
        type: "POST",
        data: JSON.stringify(lessonsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function deleteAllLessonsOfCourse() {
    let lessonsCoursesIdsList = {
        "secret": window.courseIdLessonModal,
        "listId": []
    };
    $.ajax({
        url: serverUrl() + "/manager/course/lessons/delete-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(lessonsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

// End Lessons Course Script //


// Start Teacher Course Script //
function showTeacherCourseManagementModal(id) {
    window.courseIdTeacherModal = id;
    for (let i = 0; i < courseData.data.content.length; i++) {
        if (courseData.data.content[i].id.toString().trim() === id.toString().trim()) {
            let teacher = courseData.data.content[i].teacher;
            if (teacher !== null) {
                $('#addTeacher').hide();
                let content = '';
                content += "<tr>";
                content += "<td>" + teacher.id + "</td>";
                content += "<td >" + teacher.firstName + "</td>";
                content += "<td >" + teacher.lastName + "</td>";
                content += "<td >" + teacher.degreeOfEducation + "</td>";
                content += "<td >" + teacher.teacherCode + "</td>";
                content += "<td >" + '<button type="button" class="btn btn-danger btn-sm" onclick="deleteTeachersOfCourse(' + teacher.id + ')">حذف</button></td>';
                content += "</tr>";
                $('#courseTeacherList').html(content);
            }else{

                $('#courseTeacherList').html('');
                $('#addTeacher').show();
            }
        }
    }
    $('#teacherCourseManagementModal').modal('show');
}

function deleteTeachersOfCourse(teacherId) {
    let lessonsCoursesIdsList = {
        "secret": window.courseIdTeacherModal,
        "listId": [teacherId]
    };
    $.ajax({
        url: serverUrl() + "/manager/course/teacher/delete",
        type: "POST",
        data: JSON.stringify(lessonsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

// End Teacher Course Script //


// Start Student Course Script //
function showStudentsCourseManagementModal(id) {
    window.courseIdStudentModal = id;
    let content = '';
    for (let i = 0; i < courseData.data.content.length; i++) {
        let students = courseData.data.content[i].students;
        if (courseData.data.content[i].id.toString().trim() === id.toString().trim()) {
            for (let j = 0; j < students.length; j++) {
                content += "<tr>";
                content += "<td class='text-left'><input type='checkbox' value='" + students[j].id + "'></td>";
                content += "<td>" + students[j].id + "</td>";
                content += "<td >" + students[j].firstName + "</td>";
                content += "<td >" + students[j].lastName + "</td>";
                content += "<td >" + students[j].degreeOfEducation + "</td>";
                content += "<td >" + students[j].studentCode + "</td>";
                content += "</tr>";
            }
        }
        $('#courseStudentsList').html(content);
    }
    $('#studentsCourseManagementModal').modal('show');
}
function studentDeleteAction() {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });

    let action = $('#studentAction option:selected').val();

    if (action === 'DeleteAllSelectedStudentsOfCourse') {
        deleteAllSelectedStudentsOfCourse(checks);
    } else if (action === 'DeleteAllStudentsOfCourse') {
        deleteAllStudentsOfCourse();
    }
    return false;
}

function deleteAllSelectedStudentsOfCourse(checks) {
    let studentsCoursesIdsList = {
        "secret": window.courseIdStudentModal,
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/manager/course/students/delete-all-selected",
        type: "POST",
        data: JSON.stringify(studentsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function deleteAllStudentsOfCourse() {
    let studentsCoursesIdsList = {
        "secret": window.courseIdStudentModal,
        "listId": []
    };
    $.ajax({
        url: serverUrl() + "/manager/course/students/delete-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(studentsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}
