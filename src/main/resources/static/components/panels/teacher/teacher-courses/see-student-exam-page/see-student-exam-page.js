$("#questionsList").ready(function () {
    getQuestion("descriptive");
    getQuestion("multiple-choice");
});

function getQuestion(questionType) {
    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/exam/question-list/" + questionType + "/" + window.seeExamPageExamId,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            if (questionType === 'multiple-choice') {
                prepareFormMultipleChoice(data);
            } else if (questionType === 'descriptive') {
                prepareFormDescriptive(data);
            }
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function prepareFormDescriptive(data) {
    let content = '';
    let color = '';
    for (let i = 0; i < data.length; i++) {


        content += ` <div class="form-group mt-3  col-10">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fa fa-question"></i>
                                <label class="mr-4 mt-2">سوال</label>
                                </span>
                                <textarea class="form-control" type="text" placeholder="` + data[i].text + `" readonly></textarea>
                            </div>
                        </div>`;

        content += ` <div class="form-group col-10">
                            <div class="input-group">
                                <span class="input-group-text">
                                     <label class="mr-4 mt-2">پاسخ سوال</label>
                                </span>
                                    <textarea class="form-control" name="defaultAnswer" id="` + data[i].id + `" placeholder="پاسخ سوال" readonly></textarea>          
                             </div>
                      </div>`;


        content += ` <div class="form-group col-8">
                            <div class="input-group">
                                <span class="input-group-text">
                                     <label class="mr-4 mt-2">پاسخ دانشجو</label>
                                </span>
                                    <textarea class="form-control" name="studentAnswer" id="` + data[i].id + `" placeholder="پاسخ دانشجو" readonly></textarea>          
                             </div>
                      </div>`;


        content += ` <div class="form-group col-2">
                            <div class="input-group">
                                <span class="input-group-text">
                                     <label class="mr-4 mt-2">نمره</label>
                                </span>
                                    <input type="text" name="score" class="form-control" id="` + data[i].id + `" placeholder="نمره"/>       
                             </div>
                      </div>`;

    }
    $('#descriptive').html(content);
}

window.options = [];

function prepareFormMultipleChoice(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        content += ` <div class="form-group mt-3  col-10">
                            <div class="input-group">
                                <span class="input-group-text">
                                     <label class="mr-4 mt-2">سوال</label>
                                </span>
                                <textarea class="form-control" type="text" placeholder="` + data[i].text + `" readonly></textarea>
                            </div>
                        </div>`;
        content += `<div class="col-10">`;
        for (let j = 0; j < data[i].options.length; j++) {
            content += ` 
                            <div class="form-check form-check-inline col-2">
                                <input class="form-check-input" disabled type="radio" name="` + data[i].id + `" id="` + data[i].id + `" value="` + data[i].options[j] + `">
                                <label class="form-check-label" for="` + data[i].id + `"  id="` + data[i].id + "" + data[i].options[j] + `">` + data[i].options[j] + `</label>
                            </div>
                      `;
        }

        window.options.push(data[i].id);
        content += `</div>`;
    }
    $('#multipleChoice').html(content);
    getAnswer();
}

function getAnswer() {
    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/exam/" + window.seeExamPageExamId + "/student/" + window.seeExamPageStudentId + "/answer",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            prepareScoreAndAnswer(data);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function prepareScoreAndAnswer(data) {
    var defaultAnswer = document.getElementsByName("defaultAnswer");
    var studentAnswer = document.getElementsByName("studentAnswer");
    var score = document.getElementsByName("score");
    for (let i = 0; i < data.studentAnswerOutcomeList.length; i++) {
        if (data.studentAnswerOutcomeList[i].questionType === "descriptive") {
            for (let j = 0; j < defaultAnswer.length; j++) {
                if (data.studentAnswerOutcomeList[i].questionId.toString().trim() === defaultAnswer[j].getAttribute('id').toString().trim()) {
                    defaultAnswer[j].value = data.studentAnswerOutcomeList[i].defaultAnswer;
                }
            }
            for (let j = 0; j < score.length; j++) {
                if (data.studentAnswerOutcomeList[i].questionId.toString().trim() === score[j].getAttribute('id').toString().trim()) {
                    score[j].value = data.studentAnswerOutcomeList[i].defaultPoint;
                }
            }
        }
        for (let j = 0; j < studentAnswer.length; j++) {
            if (data.studentAnswerOutcomeList[i].questionId.toString().trim() === studentAnswer[j].getAttribute('id').toString().trim()) {
                studentAnswer[j].value = data.studentAnswerOutcomeList[i].studentAnswer;
            }
        }
        if (data.studentAnswerOutcomeList[i].questionType === "multiple-choice") {
            var radio = document.getElementsByName(data.studentAnswerOutcomeList[i].questionId);
            for (let j = 0; j < radio.length; j++) {
                if (radio[j].getAttribute('value').toString().trim() === data.studentAnswerOutcomeList[i].studentAnswer.toString().trim()) {
                    radio[j].checked = true;
                    document.getElementById(data.studentAnswerOutcomeList[i].questionId.toString().trim() + "" + data.studentAnswerOutcomeList[i].defaultAnswer.toString().trim())
                        .setAttribute("class", "bg-success");
                }
            }
        }
    }
}

function submitScoresByTeacher() {

    let scores = [];

    var x = document.getElementsByName("score");
    if (x !== null) {
        for (let i = 0; i < x.length; i++) {
            let context, qId;
            if (x[i].getAttribute('id') !== null) {
                qId = x[i].getAttribute('id');
                if (x[i].value !== null) {
                    context = x[i].value;
                }
                let score = {
                    "point": context,
                    "questionId": qId,
                };
                scores.push(score);
            }
        }
    }
    const scoresExamDTO = {
        "studentId": window.seeExamPageStudentId,
        "addScoresDTO": scores,
    };
    jQuery.ajax({
        url: "http://localhost:7777/teacher/teacher-course/exam/student/score/" + window.seeExamPageExamId,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(scoresExamDTO),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            showAlert('success',data);
            loadPage('teacher-courses');
        },
        error: function (errorMessage) {
            showAlert('danger',errorMessage.responseText);
        }
    });
}
