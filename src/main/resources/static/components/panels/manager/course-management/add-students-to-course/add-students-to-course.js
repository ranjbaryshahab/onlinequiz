$("#students-list").ready(function () {
    studentListFirstTime(0, 5);
});

$('#pageSizeStudentList').change(function () {
    var value = $(this).val();
    if (value === null || value === "") {
        value = 5;
    }
    $("#students-list-paging").empty();
    studentListFirstTime(0, parseInt(value));
});

var studentData = {
    data: null,
    set lang(data) {
        this.data = data;
    },
    get lang() {
        return this.data;
    }
};

function studentListFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/student/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            studentData.data = data;
            pagingTable(JSON.parse(data.totalPages));
            prepareTable(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });

}

function studentListAfterFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/student/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            studentData.data = data;
            prepareTable(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function pagingTable(totalPage) {
    var value = $("#pageSizeStudentList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="studentListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#students-list-paging").append(middle);
    }
}

function prepareTable(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<td class='text-center'><input class='form-check-input' type='checkbox' value='" + data[i].id + "'></td>";
        content += "<td>" + data[i].id + "</td>";
        content += "<td >" + data[i].firstName + "</td>";
        content += "<td >" + data[i].lastName + "</td>";
        content += "<td >" + data[i].degreeOfEducation + "</td>";
        content += "<td >" + data[i].studentCode + "</td>";
        content += "<td >" + data[i].activeCourse + "</td>";
        content += "<td >" +
            '<button type="button" class="btn btn-info btn-sm" onclick="showStudentDetails(' + data[i].id + ')">مشاهده</button></td>';
        content += "</tr>";
    }
    $('#students-list-table-body').html(content);
}

function showStudentDetails(id) {
    let content = '';
    for (let i = 0; i < studentData.data.content.length; i++) {
        let courses = studentData.data.content[i].courses;
        if (studentData.data.content[i].id === id) {
            for (let j = 0; j < courses.length; j++) {
                content += "<tr>";
                content += "<td>" + courses[j].courseName + "</td>";
                content += "<td>" + courses[j].startCourse + "</td>";
                content += "<td>" + courses[j].endCourse + "</td>";
                content += "</tr>";
            }
        }
        $('#courseStudentTable').html(content);
    }
    $("#studentCourseDetailsModal").modal('show');
}

function addStudentsToCourse() {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });
    let lessonToCourse = {
        "secret": window.courseIdStudentModal,
        "listId": checks
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/course/add-students",
        type: "POST",
        data: JSON.stringify(lessonToCourse),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('course-management');
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}
