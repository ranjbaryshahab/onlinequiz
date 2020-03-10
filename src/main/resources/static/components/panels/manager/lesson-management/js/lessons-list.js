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

function createLesson() {
    let newLesson = {
        "id": '',
        "lessonName": $("#lessonNameCreateInput").val(),
        "lessonTopic": $("#lessonTopicCreateInput").val()
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/lesson/create",
        type: "POST",
        data: JSON.stringify(newLesson),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            loadPage('lessons-list');
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function editLesson() {
    let editLesson = {
        "id": $("#lessonIdEditInput").val(),
        "lessonName": $("#lessonNameEditInput").val(),
        "lessonTopic": $("#lessonTopicEditInput").val()
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/lesson/edit",
        type: "POST",
        data: JSON.stringify(editLesson),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            loadPage('lessons-list');
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
        content += "<td class='text-center'><input class='form-check-input' type='checkbox' value='" + data[i].id + "'></td>";
        content += "<td>" + id + "</td>";
        content += "<td >" + lessonName + "</td>";
        content += "<td >" + lessonTopic + "</td>";
        content += "<td >" +
            '<button type="button" class="btn btn-warning btn-sm" onclick="showEditModal(\'' + id + '\' , \'' + lessonName + '\' , \'' + lessonTopic + '\')">ویرایش</button></td>';
        content += "</tr>";
    }
    $('#lessons-list-table-body').html(content);
}

function showEditModal(id, lessonName, lessonTopic) {
    $("#lessonIdEditInput").val(id);
    $("#lessonNameEditInput").val(lessonName);
    $("#lessonTopicEditInput").val(lessonTopic);
    $("#editLessonModal").modal('show');
}

var submitLessonData = function () {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });

    let action = $('#action option:selected').val();

    if (action === 'DeleteAllSelectedLesson') {
        deleteAllSelectedLesson(checks);
    } else if (action === 'DeleteAllLesson') {
        deleteAllLesson(checks);
    }
    return false;
};

function deleteAllSelectedLesson(checks) {
    let lessonsIdsList = {
        "secret": "ajax",
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/manager/lesson/delete-all-selected",
        type: "POST",
        data: JSON.stringify(lessonsIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('lessons-list');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function deleteAllLesson() {
    $.ajax({
        url: serverUrl() + "/manager/lesson/delete-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('lessons-list');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}
