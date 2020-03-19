function searchCourseListFirstTime(pageNo, pageSize) {
    let searchCourse = {
        'courseId': '',
        'courseName': $('#courseNameSearchInput').val(),
        'courseStartDate': $("#courseStartSearchInput").attr('data-gdate'),
        'courseEndDate': $("#courseEndSearchInput").attr('data-gdate')
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/course/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(searchCourse),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $("#courses-list-table-body").empty();
            $("#courses-list-paging").empty();
            pagingTableResultCourseSearch(JSON.parse(data.totalPages));
            prepareTableResultCourseSearch(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function searchCourseListAfterFirstTime(pageNo, pageSize) {
    let searchCourse = {
        'courseId': '',
        'courseName': $('#courseNameSearchInput').val(),
        'courseStartDate': $("#courseStartSearchInput").attr('data-gdate'),
        'courseEndDate': $("#courseEndSearchInput").attr('data-gdate')
    };
    jQuery.ajax({
        url: "http://localhost:7777/manager/course/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(searchCourse),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            prepareTable(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function pagingTableResultCourseSearch(totalPage) {
    var value = $("#pageSizeCourseList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="searchCourseListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#courses-list-paging").append(middle);
    }
}

function prepareTableResultCourseSearch(data) {
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
