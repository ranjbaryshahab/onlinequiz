$("#lessons-list").ready(function () {
    lessonListFirstTime(0, 5);
});

$('#pageSizeLessonList').change(function () {
    var value = $(this).val();
    if (value === null || value === "") {
        value = 5;
    }
    $("#lessons-list-paging").empty();
    lessonListFirstTime(0, parseInt(value));
});

function addLessonToCourse() {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });
    let lessonToCourse = {
        "secret": window.courseIdLessonModal,
        "listId": checks
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/course/add-lessons",
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

function lessonListFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/lessons/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            pagingTable(JSON.parse(data.totalPages));
            prepareTable(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function lessonListAfterFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/lessons/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
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

function pagingTable(totalPage) {
    var value = $("#pageSizeLessonList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="lessonListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#lessons-list-paging").append(middle);
    }
}

function prepareTable(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        let id = data[i].id;
        let lessonName = data[i].name;
        let lessonTopic = data[i].topic;
        content += "<tr>";
        content += "<td class='text-center'><div class='text-left ml-4'><input class='form-check-input' type='checkbox' value='" + data[i].id + "'></div></td>";
        content += "<td>" + id + "</td>";
        content += "<td >" + lessonName + "</td>";
        content += "<td >" + lessonTopic + "</td>";
        content += "</tr>";
    }
    $('#lessons-list-table-body').html(content);
}
