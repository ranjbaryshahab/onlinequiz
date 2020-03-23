$("#questionList").ready(function () {
    window.questionType = "descriptive";
    window.pageSizeQuestion = 5;
    questionListFirstTime(0, 5);
});

var questionData = {
    data: null,
    set lang(data) {
        this.data = data;
    },
    get lang() {
        return this.data;
    }
};

$('#pageSizeQuestionList').change(function () {
    var value = $(this).val();
    if (value === null || value === "") {
        value = 5;
    }
    $("#questions-list-paging").empty();
    window.pageSizeQuestion = parseInt(value);
    questionTypeListChange();
});

function questionTypeListChange() {
    var selectBox = document.getElementById("questionTypeListSelect");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    window.questionType = selectedValue;
    $("#questions-list-paging").empty();
    questionListFirstTime(0, window.pageSizeQuestion);
}

function questionListFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/teacher/question-bank/list/" + questionType + "/" + usernameHeader + "/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            pagingTableQuestion(JSON.parse(data.totalPages));
            prepareTable(data.content);
            questionData.data = data.content;
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function questionListAfterFirstTime(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/teacher/question-bank/list/" + questionType + "/" + usernameHeader + "/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            prepareTable(data.content);
            questionData.data = data.content;
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function pagingTableQuestion(totalPage) {
    var value = $("#pageSizeQuestionList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="questionListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#questions-list-paging").append(middle);
    }
}


function prepareTable(question) {
    let content = '';
    for (let j = 0; j < question.length; j++) {
        content += "<tr>";
        content += "<td><input type='checkbox' value='" + question[j].id + "'></td>";
        content += "<td>" + question[j].id + "</td>";
        content += "<td >" + question[j].text + "</td>";
        content += "<td >" + question[j].title + "</td>";
        content += "<td >" + question[j].answer + "</td>";
        let point = 0.0;
        for (let i = 0; i < question[j].scoreList.length; i++) {
            if (question[j].scoreList[i].exam.id.toString().trim()  === window.questionListExamId.toString().trim() ) {
                point = question[j].scoreList[i].point;
                break;
            }
        }
        content += "<td >" + point + "</td>";
        if (question[j].options !== undefined)
            content += '<td>' +
                '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showChoiceQuestionModal(\'' + question[j].id + '\')">گزینه ها</button>' +
                '</td>';
        else
            content += '<td></td>';
        content += "</tr>";
    }
    $('#questions-list-table-body').html(content);
}

function showChoiceQuestionModal(id) {
    let content = '';
    for (let i = 0; i < questionData.data.length; i++) {
        let options = questionData.data[i].options;
        if (questionData.data[i].id.toString().trim() === id.toString().trim()) {
            for (let j = 0; j < options.length; j++) {
                content += "<tr>";
                content += "<td>" + j + "</td>";
                content += "<td >" + options[j] + "</td>";
                content += "</tr>";
            }
        }
        $('#choicesQuestionList').html(content);
    }
    $('#choicesQuestionModal').modal('show');
}

function addQuestionToExamFromQuestionBank() {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });
    let lessonToCourse = {
        "secret": window.questionListExamId,
        "listId": checks
    };

    jQuery.ajax({
        url: "http://localhost:7777/teacher/question-bank/add-question-to-exam",
        type: "POST",
        data: JSON.stringify(lessonToCourse),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('question-management');
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}
