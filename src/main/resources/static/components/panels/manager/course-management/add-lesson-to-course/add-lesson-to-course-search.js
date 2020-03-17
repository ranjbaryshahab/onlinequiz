function searchLessonListFirstTime(pageNo, pageSize) {
    let searchLesson = {
        "id": "",
        "lessonName": $("#lessonNameSearchInput").val(),
        "lessonTopic": $("#lessonTopicSearchInput").val()
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/lesson/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(searchLesson),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $("#lessons-list-table-body").empty();
            $("#lessons-list-paging").empty();
            pagingTableResultLessonSearch(JSON.parse(data.totalPages));
            prepareTableResultLessonSearch(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function searchLessonListAfterFirstTime(pageNo, pageSize) {
    let searchLesson = {
        "id": "",
        "lessonName": $("#lessonNameSearchInput").val(),
        "lessonTopic": $("#lessonTopicSearchInput").val()
    };
    jQuery.ajax({
        url: "http://localhost:7777/manager/lesson/search/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(searchLesson),
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

function pagingTableResultLessonSearch(totalPage) {
    var value = $("#pageSizeLessonList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="searchLessonListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#lessons-list-paging").append(middle);
    }
}

function prepareTableResultLessonSearch(data) {
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

