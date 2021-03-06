$("#teachers-list").ready(function () {
    teacherListFirstTime(0, 5);
});

$('#pageSizeTeacherList').change(function () {
    var value = $(this).val();
    if (value === null || value === "") {
        value = 5;
    }
    $("#teachers-list-paging").empty();
    teacherListFirstTime(0, parseInt(value));
});

var teacherData = {
    data: null,
    set lang(data) {
        this.data = data;
    },
    get lang() {
        return this.data;
    }
};

function teacherListFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/teacher/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            teacherData.data = data;
            pagingTable(JSON.parse(data.totalPages));
            prepareTable(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });

}

function teacherListAfterFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/teacher/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            teacherData.data = data;
            prepareTable(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function pagingTable(totalPage) {
    var value = $("#pageSizeTeacherList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="teacherListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#teachers-list-paging").append(middle);
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
        content += "<td >" + data[i].teacherCode + "</td>";
        content += "<td >" +
            '<button type="button" class="btn btn-info btn-sm" onclick="showTeacherCourseDetails(' + data[i].id + ')">مشاهده</button></td>';
        content += "</tr>";
    }
    $('#teachers-list-table-body').html(content);
}

function showTeacherCourseDetails(id) {
    let content = '';
    for (let i = 0; i < teacherData.data.content.length; i++) {
        let courses = teacherData.data.content[i].courses;
        if (teacherData.data.content[i].id === id) {
            for (let j = 0; j < courses.length; j++) {
                content += "<tr>";
                content += "<td>" + courses[j].courseName + "</td>";
                content += "<td>" + courses[j].startCourse + "</td>";
                content += "<td>" + courses[j].endCourse + "</td>";
                content += "</tr>";
            }
        }
        $('#courseTeacherDetails').html(content);
    }
    $("#teacherCourseDetailsModal").modal('show');
}


