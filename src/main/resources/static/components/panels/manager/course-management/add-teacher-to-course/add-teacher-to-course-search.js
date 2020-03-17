var teacherData = {
    data: null,
    set lang(data) {
        this.data = data;
    },
    get lang() {
        return this.data;
    }
};

function searchTeacherListFirstTime(pageNo, pageSize) {
    const teacherSearchDTO = {
        "firstName": $("#teacherFirstNameSearchInput").val(),
        "lastName": $("#teacherLastNameSearchInput").val(),
        "degreeOfEducation": $("#teacherDegreeOfEducationSearchInput").val(),
        "teacherCode": $("#teacherCodeSearchInput").val(),
        "status":"ACTIVATE"
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/teacher/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(teacherSearchDTO),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $("#teachers-list-table-body").empty();
            $("#teachers-list-paging").empty();
            pagingTableResultSearchTeachersList(JSON.parse(data.totalPages));
            prepareTableTeacherResultSearch(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function searchTeacherListAfterFirstTime(pageNo, pageSize) {
    const teacherSearchDTO = {
        "firstName": $("#teacherFirstNameSearchInput").val(),
        "lastName": $("#teacherLastNameSearchInput").val(),
        "degreeOfEducation": $("#teacherDegreeOfEducationSearchInput").val(),
        "teacherCode": $("#teacherCodeSearchInput").val(),
        "status":"ACTIVATE"
    };
    jQuery.ajax({
        url: "http://localhost:7777/manager/teacher/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(teacherSearchDTO),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            prepareTableTeacherResultSearch(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function pagingTableResultSearchTeachersList(totalPage) {
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="searchTeacherListAfterFirstTime(' + i + ',10)">' + (i + 1) + '</li>';
        }
        $("#teachers-list-paging").append(middle);
    }
}

function prepareTableTeacherResultSearch(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += "<tr>";
        content += "<td class='text-center'><input class='form-check-input' type='checkbox' value='" + data[i].id + "'></td>";
        content += "<td>" + data[i].id + "</td>";
        content += "<td >" + data[i].firstName + "</td>";
        content += "<td >" + data[i].lastName + "</td>";
        content += "<td >" + data[i].degreeOfEducation + "</td>";
        content += "<td >" + data[i].teacherCode + "</td>";
        content += "<td >" + data[i].activeCourse + "</td>";
        content += "<td >" +
            '<button type="button" class="btn btn-info btn-sm" onclick="showDetails(' + data[i].id + ')"></button></td>';
        content += "</tr>";
    }
    $('#teachers-list-table-body').html(content);
}

function showDetails(id) {
    for (let i = 0; i < teacherData.length; i++) {
        if (teacherData.data[i].id === id) {
            for (let j = 0; j < teacherData[i].courses.length; j++) {
                $("#courseCodeTeacherList").text(teacherData[i].courses[j].courseCode);
                $("#lessonNameTeacherList").text(teacherData[i].courses[j].lesson.name);
                $("#courseStartDateTeacherList").text(teacherData[i].courses[j].courseStart);
                $("#courseEndDateTeacherList").text(teacherData[i].courses[j].courseEnd);
            }
        }
    }
    $("#teacherCourseDetailsModal").modal('show');

}

