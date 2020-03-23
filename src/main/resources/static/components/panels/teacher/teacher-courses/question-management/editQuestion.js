
function showEditQuestionModal(id) {
    window.questionId = id;
    let question = questionData.data;
    for (let j = 0; j < question.length; j++) {
        if (id === question[j].id.toString()) {
            $('#titleQuestionEditInput').val(question[j].title);
            $('#textQuestionEditInput').val(question[j].text);
            $('#answerQuestionEditInput').val(question[j].answer);
            $('#questionTypeEditSelect').val(window.questionType);
            questionTypeEditChange();

            for (let i = 0; i < question[j].scoreList.length; i++) {
                if (question[j].scoreList[i].exam.id.toString().trim() === window.questionListExamId.toString().trim()) {
                    $('#pointQuestionEditInput').val(question[j].scoreList[i].point);
                    break;
                }
            }

            if (question[j].options !== undefined) {
                for (let i = 0; i < question[j].options.length; i++) {
                    addNewChoiceEdit();
                    $('#optional-question-add-option' + (i + 1)).val(question[j].options[i]);
                }
            }
        }
    }
    $('#editNewQuestionModal').modal('show');
}

function editQuestion() {
    let options = [];
    $("#questionCreateForm").find('input[name="question-options"]').each(function () {
        options.push($(this).val());
    });
    let question = {
        "examId": window.questionListExamId,
        "questionId": window.questionId,
        "title": $('#titleQuestionEditInput').val(),
        "text": $('#textQuestionEditInput').val(),
        "answer": $('#answerQuestionEditInput').val(),
        "type": window.questionType,
        "point": $('#pointQuestionEditInput').val(),
        "options": options
    };

    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/edit-question",
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

function questionTypeEditChange() {
    var selectBox = document.getElementById("questionTypeEditSelect");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    window.questionType = selectedValue;
    if (selectedValue === 'multiple-choice') {
        window.questionType = 'multipleChoice';
        $('#multipleChoiceQuestionContentEdit').html(`
            <button type="button" class="btn btn-sm btn-success" onclick="addNewChoiceEdit()">افزودن گزینه
                    </button>
        `);
    } else if (selectedValue === 'descriptive') {
        $('#multipleChoiceQuestionContentEdit').empty();

    }
}

function addNewChoiceEdit() {
    var cols = "";
    cols += '<div class="form-group">';
    cols += '<div style="display: flex; justify-content: space-between">';
    cols += '<label for="optional-question-add-option' + counter + '" class="col-form-label">گزینه سوال:</label>';
    cols += '<input type="button" style="border-radius: 10px" class="ibtnDel btn btn-md btn-danger " value="حذف" onclick="deleteChoiceEdit(this)">';
    cols += '</div>';
    cols += '<input type="text" class="form-control" name="question-options" id="optional-question-add-option' + counter + '" >';
    cols += '</div>';
    $('#multipleChoiceQuestionContentEdit').append(cols);
    counter++;
}


function deleteChoiceEdit(This) {
    $(This).closest("div").parent().remove();
    counter -= 1
}
