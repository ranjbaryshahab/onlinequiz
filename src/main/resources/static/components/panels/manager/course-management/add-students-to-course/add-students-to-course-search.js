var studentData = {
    data: null,
    set lang(data) {
        this.data = data;
    },
    get lang() {
        return this.data;
    }
};

function searchStudentListFirstTime(pageNo, pageSize) {
    const studentSearchDTO = {
        "firstName": $("#studentFirstNameSearchInput").val(),
        "lastName": $("#studentLastNameSearchInput").val(),
        "degreeOfEducation": $("#studentDegreeOfEducationSearchInput").val(),
        "studentCode": $("#studentCodeSearchInput").val(),
        "status":"ACTIVATE"
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/student/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(studentSearchDTO),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $("#students-list-table-body").empty();
            $("#students-list-paging").empty();
            pagingTableResultSearchStudentsList(JSON.parse(data.totalPages));
            prepareTableStudentResultSearch(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function searchStudentListAfterFirstTime(pageNo, pageSize) {
    const studentSearchDTO = {
        "firstName": $("#studentFirstNameSearchInput").val(),
        "lastName": $("#studentLastNameSearchInput").val(),
        "degreeOfEducation": $("#studentDegreeOfEducationSearchInput").val(),
        "studentCode": $("#studentCodeSearchInput").val(),
        "status":"ACTIVATE"
    };
    jQuery.ajax({
        url: "http://localhost:7777/manager/student/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(studentSearchDTO),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            prepareTableStudentResultSearch(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function pagingTableResultSearchStudentsList(totalPage) {
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="searchStudentListAfterFirstTime(' + i + ',10)">' + (i + 1) + '</li>';
        }
        $("#students-list-paging").append(middle);
    }
}

function prepareTableStudentResultSearch(data) {
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
            '<button type="button" class="btn btn-info btn-sm" onclick="showDetails(' + data[i].id + ')">مشاهده</button></td>';
        content += "</tr>";
    }
    $('#students-list-table-body').html(content);
}

function showDetails(id) {
    for (let i = 0; i < studentData.length; i++) {
        if (studentData.data[i].id === id) {
            for (let j = 0; j < studentData[i].courses.length; j++) {
                $("#courseCodeStudentList").text(studentData[i].courses[j].courseCode);
                $("#lessonNameStudentList").text(studentData[i].courses[j].lesson.name);
                $("#courseStartDateStudentList").text(studentData[i].courses[j].courseStart);
                $("#courseEndDateStudentList").text(studentData[i].courses[j].courseEnd);
            }
        }
    }
    $("#studentCourseDetailsModal").modal('show');
}
