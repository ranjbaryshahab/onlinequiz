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
        url: "http://localhost:7777/student/student-course/" + usernameHeader + "/" + pageNo + "/" + pageSize,
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
        url: "http://localhost:7777/student/student-course/" + usernameHeader + "/" + pageNo + "/" + pageSize,
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
            '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showExamCourseManagementModal(\'' + id + '\')">آزمون ها</button>' +
            '</td>';
        content += "</tr>";
    }
    $('#courses-list-table-body').html(content);
}

function showExamCourseManagementModal(id) {
    window.courseIdExamModal = id;
    let content = '';
    for (let i = 0; i < courseData.data.content.length; i++) {
        let exam = courseData.data.content[i].exams;
        if (courseData.data.content[i].id.toString().trim() === id.toString().trim()) {
            for (let j = 0; j < exam.length; j++) {
                let answer = true;
                let studentAnswer = exam[j].studentAnswers;
                for (let k = 0; k < studentAnswer.length; k++) {
                    if (studentAnswer[k].exam.id === exam[j].id && studentAnswer[k].student.account.username === usernameHeader) {
                        answer = false;
                        window.studentId = studentAnswer[k].student.id;
                    }
                }
                content += "<tr>";
                content += "<td>" + exam[j].id + "</td>";
                content += "<td >" + exam[j].title + "</td>";
                content += "<td >" + exam[j].time + "</td>";
                content += "<td >" + exam[j].description + "</td>";
                if (answer && exam[j].started === true && exam[j].ended === false)
                    content += '<td><button type="button" class="btn btn-primary btn-sm mr-2" onclick="startExamByStudent(\'' + exam[j].id + '\')">شروع آزمون</button>' + '</td>';
                else
                    content += '<td><button type="button" class="btn btn-info btn-sm mr-2" onclick="showScoreExam(\'' + exam[j].id + '\')">مشاهده نمره</button>' + '</td>';
                content += "</tr>";
            }

        }
        $('#examsCourseList').html(content);
    }
    $('#examsCourseManagementModal').modal('show');
}

function startExamByStudent(id) {
    window.globalExamStartedIdForStudent = id;
    loadPage('start-exam');
}

function showScoreExam(id) {
    jQuery.ajax({
        url: "http://localhost:7777/student/" + window.studentId + "/student-course/exam/" + id + "/score",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            let content = '';
            content += "<tr>";
            content += "<td>" + data.multipleChoicesQuestionScore + "</td>";
            content += "<td>" + data.descriptiveQuestionScore + "</td>";
            content += "<td>" + data.finalScore + "</td>";
            content += "</tr>";
            $('#myScore').html(content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
    $('#scoreExamModal').modal('show');
}
