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


function courseListFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/" + usernameHeader + "/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization":
                "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        }
        ,
        success: function (data, textStatus, jQxhr) {
            pagingTable(JSON.parse(data.totalPages));
            prepareTable(data.content);
            courseData.data = data;
        }
        ,
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    })
    ;
}

function courseListAfterFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/" + usernameHeader + "/" + pageNo + "/" + pageSize,
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
            '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showLessonCourseManagementModal(\'' + id + '\')">دروس</button>' +
            '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showStudentsCourseManagementModal(\'' + id + '\')">دانشجویان</button>' +
            '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showExamCourseManagementModal(\'' + id + '\')">آزمون ها</button>' +
            '</td>';
        content += "</tr>";
    }
    $('#courses-list-table-body').html(content);
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


// Start Student Course Script //
function showStudentsCourseManagementModal(id) {
    window.courseIdStudentModal = id;
    let content = '';
    for (let i = 0; i < courseData.data.content.length; i++) {
        let students = courseData.data.content[i].students;
        if (courseData.data.content[i].id.toString().trim() === id.toString().trim()) {
            for (let j = 0; j < students.length; j++) {
                content += "<tr>";
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

function showExamCourseManagementModal(id) {
    window.courseIdExamModal = id;
    let content = '';
    for (let i = 0; i < courseData.data.content.length; i++) {
        let exam = courseData.data.content[i].exams;
        if (courseData.data.content[i].id.toString().trim() === id.toString().trim()) {
            for (let j = 0; j < exam.length; j++) {
                content += "<tr>";
                content += "<td><input type='checkbox' value='" + exam[j].id + "'></td>";
                content += "<td>" + exam[j].id + "</td>";
                content += "<td >" + exam[j].title + "</td>";
                content += "<td >" + exam[j].time + "</td>";
                content += "<td >" + exam[j].description + "</td>";
                content += "<td >" +
                    "<select class='custom-select' id='questionTypeSelect' onchange='questionTypeSelect(" + exam[j].id + ")'>" +
                    "  <option selected value='text'>افزودن سوال</option>" +
                    "  <option value='newQuestion'>سوال جدید</option>" +
                    "  <option value='myQuestionsBank'>افزودن از بانک سوالات شخصی</option>" +
                    "  <option value='lessonQuestionsBank'>افزودن از بانک سوالات درس</option>" +
                    "</select></td>";
                content += "</tr>";
            }
        }
        $('#examsCourseList').html(content);
    }
    $('#examsCourseManagementModal').modal('show');
}

function createExam() {
    let newExam = {
        "examId": '',
        "courseId": window.courseIdExamModal,
        "title": $("#titleExamCreateInput").val(),
        "time": $("#timeExamCreateInput").val(),
        "description": $("#descriptionExamCreateInput").val(),
    };

    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/add-exam-to-course",
        type: "POST",
        data: JSON.stringify(newExam),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $('#addExamToCourseModal').modal('hide');
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('teacher-courses');
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function examDeleteAction() {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });

    let action = $('#examAction option:selected').val();

    if (action === 'DeleteAllSelectedExamsOfCourse') {
        deleteAllSelectedExamsOfCourse(checks);
    } else if (action === 'DeleteAllExamsOfCourse') {
        deleteAllExamsOfCourse();
    }
    return false;
}


function deleteAllSelectedExamsOfCourse(checks) {
    let examsCoursesIdsList = {
        "secret": window.courseIdExamModal,
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/teacher/teacher-course/exams/delete-all-selected",
        type: "POST",
        data: JSON.stringify(examsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('teacher-courses');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function deleteAllExamsOfCourse() {
    let examsCoursesIdsList = {
        "secret": window.courseIdExamModal,
        "listId": []
    };
    $.ajax({
        url: serverUrl() + "/teacher/teacher-course/exams/delete-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(examsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('teacher-courses');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function questionTypeSelect(examId) {
    window.examId = examId;
    var selectBox = document.getElementById("questionTypeSelect");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    if (selectedValue === 'newQuestion') {
        $('#addNewQuestionModal').modal('show');
    }
}

function questionTypeCreateChange(){
    var selectBox = document.getElementById("questionTypeCreateSelect");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    if(selectedValue === 'multipleChoice'){
        $('#multipleChoiceQuestionContent').html(`
            
        `)
    }else if(selectedValue==='descriptive'){
        $('#multipleChoiceQuestionContent').empty();

    }
}

function createNewQuestion() {

}

