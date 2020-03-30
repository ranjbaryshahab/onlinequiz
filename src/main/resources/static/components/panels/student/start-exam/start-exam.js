$("#questionsList").ready(function () {
            getDuration();
            getQuestion("descriptive");
            getQuestion("multiple-choice");
            $('#action').html(`<div class="form-row justify-content-center">
            <div class="form-group col-6 mt-3 text-center">
                <button type="button" class="btn btn-success" onclick="submitAnswersByStudentButton()" id="submitAnswersByStudent">ثبت آزمون
                </button>
            </div>
        </div>`);
});

function getQuestion(questionType) {
    jQuery.ajax({
        url: "http://localhost:7777/student/student-course/exam/question-list/" + questionType + "/" + window.globalExamStartedIdForStudent,
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


function getDuration() {
    jQuery.ajax({
        url: "http://localhost:7777/student/student-course/exam/get-time/" + window.globalExamStartedIdForStudent,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            prepareTimer(data);
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
        if (i % 2 !== 0)
            color = 'info';
        else
            color = 'warning';

        content += ` <div class="form-group mt-3 bg-` + color + ` col-10">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fa fa-question"></i>
                                </span>
                                <textarea class="form-control" type="text" placeholder="` + data[i].text + `" readonly></textarea>
                            </div>
                        </div>`;

        content += ` <div class="form-group  bg-` + color + ` col-10">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fa fa-key"></i>
                                </span>
                                    <textarea class="form-control" id="` + data[i].id + `" placeholder="پاسخ"></textarea>          
                             </div>
                      </div>`;
    }
    $('#descriptive').html(content);
}

window.options = [];

function prepareFormMultipleChoice(data) {
    let content = '';
    let color = '';
    for (let i = 0; i < data.length; i++) {
        if (i % 2 !== 0)
            color = 'info';
        else
            color = 'warning';

        content += ` <div class="form-group mt-3 bg-` + color + ` col-10">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fa fa-question"></i>
                                </span>
                                <textarea class="form-control" type="text" placeholder="` + data[i].text + `" readonly></textarea>
                            </div>
                        </div>`;
        content += `<div class="col-10">`;
        for (let j = 0; j < data[i].options.length; j++) {
            content += ` 
                            <div class="form-check form-check-inline col-2">
                                <input class="form-check-input" type="radio" name="` + data[i].id + `" id="` + data[i].id + `" value="` + data[i].options[j] + `">
                                <label class="form-check-label" for="` + data[i].id + `">` + data[i].options[j] + `</label>
                            </div>
                      `;
        }
        window.options.push(data[i].id);
        content += `</div>`;
    }
    $('#multipleChoice').html(content);
}

function prepareTimer(Duration) {
    var nowDay = new Date().getTime();
    var countDownDate = new Date(nowDay + Duration).getTime();

    try {
        timer = setInterval(function () {
            var now = new Date().getTime();
            var distance = countDownDate - now;
            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((distance % (1000 * 60)) / 1000);
            try {
                document.getElementById("timer").innerHTML = hours + "h "
                    + minutes + "m " + seconds + "s ";
            } catch (err) {
                clearInterval(timer);
            }
            document.getElementById("timer").innerHTML = hours + "h "
                + minutes + "m " + seconds + "s ";
            if (distance < 60 * 1000) {
                $("#timer").css("color", "darkred").fadeOut().fadeIn(200);
            }
            if (distance <= 0) {
                clearInterval(timer);
                $('#timer').innerHTML = "اتمام وقت";
                $('#submitAnswersByStudent').prop("disabled", true);
                submitAnswersAuto();
            }
            if ($('#timer') === null) {
                clearInterval(timer);
            }
        }, 1000);
    } catch (err) {
        clearInterval(timer);
    }
}

function submitAnswersByStudentButton() {
    window.submitExamByStudent = 'student';
    submitAnswersByStudent();
}

function submitAnswersAuto() {
    window.submitExamByStudent = 'auto';
    submitAnswersByStudent();
}

function submitAnswersByStudent() {

    let answers = [];

    var x = document.getElementsByTagName("TEXTAREA");
    if (x !== null) {
        for (let i = 0; i < x.length; i++) {
            let context, qId, qType = 'descriptive';
            if (x[i].getAttribute('id') !== null) {
                qId = x[i].getAttribute('id');
                if (x[i].value !== null) {
                    context = x[i].value;
                }
                let answer = {
                    "context": context,
                    "questionId": qId,
                    "questionType": qType
                };
                answers.push(answer);
            }
        }
    }
    if (window.options !== null) {
        let context, qId, qType = 'multiple-choice';
        for (let i = 0; i < window.options.length; i++) {
            context = $("input[name='" + window.options[i] + "']:checked").val();
            qId = window.options[i].toString();

            let answer = {
                "context": context,
                "questionId": qId,
                "questionType": qType
            };
            answers.push(answer);
        }
    }
    const answersExamDTO = {
        "studentUsername": usernameHeader,
        "addAnswersDTO": answers,
        "submitStudent": window.submitExamByStudent
    };
    jQuery.ajax({
        url: "http://localhost:7777/student/student-course/exam/answer/" + window.globalExamStartedIdForStudent,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(answersExamDTO),
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
