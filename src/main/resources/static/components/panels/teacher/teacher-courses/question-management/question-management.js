$("#questionsList").ready(function () {
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
        url: "http://localhost:7777/teacher/teacher-course/exam/question-list/" + questionType + "/" + window.questionListExamId + "/" + pageNo + "/" + pageSize,
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
        url: "http://localhost:7777/teacher/teacher-course/exam/question-list/" + questionType + "/" + window.questionListExamId + "/" + pageNo + "/" + pageSize,
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
            if (question[j].scoreList[i].exam.id.toString().trim() === window.questionListExamId.toString().trim()) {
                point = question[j].scoreList[i].point;
                break;
            }
        }
        content += "<td >" + point + "</td>";
        if (question[j].options !== undefined)
            content += '<td>' +
                '<button type="button" class="btn btn-primary btn-sm mr-2" onclick="showChoiceQuestionModal(\'' + question[j].id + '\')">گزینه ها</button>' +
                '<button type="button" class="btn btn-warning btn-sm mr-2" onclick="showEditQuestionModal(\'' + question[j].id + '\')">ویرایش</button>' +
                '</td>';
        else
            content += '<td>' +
                '<button type="button" class="btn btn-warning btn-sm mr-2" onclick="showEditQuestionModal(\'' + question[j].id + '\')">ویرایش</button>' +
                '</td>';
        content += "</tr>";
    }
    $('#questionsList').html(content);
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


function questionTypeSelect() {
    var selectBox = document.getElementById("questionTypeSelect");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    if (selectedValue === 'newQuestion') {
        $('#addNewQuestionModal').modal('show');
    }

    if (selectedValue === 'myQuestionsBank') {
        loadPage('teacher-question-bank')
    }
}

function questionTypeCreateChange() {
    var selectBox = document.getElementById("questionTypeCreateSelect");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    window.questionType = selectedValue;
    if (selectedValue === 'multipleChoice') {
        $('#multipleChoiceQuestionContent').html(`
            <button type="button" class="btn btn-sm btn-success" onclick="addNewChoice()">افزودن گزینه
                    </button>
        `);
    } else if (selectedValue === 'descriptive') {
        $('#multipleChoiceQuestionContent').empty();

    }
}


var counter = 1;

function addNewChoice() {
    var cols = "";
    cols += '<div class="form-group">';
    cols += '<div style="display: flex; justify-content: space-between">';
    cols += '<label for="optional-question-add-option' + counter + '" class="col-form-label">گزینه سوال:</label>';
    cols += '<input type="button" style="border-radius: 10px" class="ibtnDel btn btn-md btn-danger " value="حذف" onclick="deleteChoice(this)">';
    cols += '</div>';
    cols += '<input type="text" class="form-control" name="question-options" id="optional-question-add-option' + counter + '" >';
    cols += '</div>';
    $('#multipleChoiceQuestionContent').append(cols);
    counter++;
}


function deleteChoice(This) {
    $(This).closest("div").parent().remove();
    counter -= 1
}


function createNewQuestion() {
    let options = [];
    $("#questionCreateForm").find('input[name="question-options"]').each(function () {
        options.push($(this).val());
    });
    let question = {
        "examId": window.questionListExamId,
        "questionId": null,
        "title": $('#titleQuestionCreateInput').val(),
        "text": $('#textQuestionCreateInput').val(),
        "answer": $('#answerQuestionCreateInput').val(),
        "type": window.questionType,
        "point": $('#pointQuestionCreateInput').val(),
        "options": options
    };

    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/add-question-to-exam",
        type: "POST",
        data: JSON.stringify(question),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $('#addNewQuestionModal').modal('hide');
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('question-management');
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function questionDeleteAction() {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });

    let action = $('#questionAction option:selected').val();

    if (action === 'DeleteAllSelectedQuestions') {
        deleteAllSelectedQuestions(checks);
    } else if (action === 'DeleteAllQuestions') {
        deleteAllQuestions();
    }
    return false;
}


function deleteAllSelectedQuestions(checks) {
    let examsCoursesIdsList = {
        "secret": window.questionListExamId,
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/teacher/teacher-course/exam/questions/delete-all-selected",
        type: "POST",
        data: JSON.stringify(examsCoursesIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('question-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function deleteAllQuestions() {
    $.ajax({
        url: serverUrl() + "/teacher/teacher-course/exam/" + window.questionListExamId + "/questions/delete-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('question-management');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}


