var studentNumber = {
    number: 0,
    set student(number) {
        this.number = number;
    },
    get student() {
        return this.number;
    }
};
var teacherNumber = {
    number: 0,
    set student(number) {
        this.number = number;
    },
    get student() {
        return this.number;
    }
};

var allUserNumber = {
    number: 0,
    set all(number) {
        this.number = number;
    },
    get all() {
        return this.number;
    }
};

var activateUserNumber = {
    number: 0,
    set all(number) {
        this.number = number;
    },
    get all() {
        return this.number;
    }
};

var deactivateUserNumber = {
    number: 0,
    set all(number) {
        this.number = number;
    },
    get all() {
        return this.number;
    }
};

var awaitingUserNumber = {
    number: 0,
    set all(number) {
        this.number = number;
    },
    get all() {
        return this.number;
    }
};

var rejectedUserNumber = {
    number: 0,
    set all(number) {
        this.number = number;
    },
    get all() {
        return this.number;
    }
};

$(document).ready(function () {
    studentList(0, 5);
    teacherList(0, 5);
    allUserList(0, 5);
    google.charts.load('current', {'packages': ['corechart']});
    google.charts.setOnLoadCallback(studentTeacherDrawChart);
    google.charts.setOnLoadCallback(accountStatusDrawChart);
    google.charts.setOnLoadCallback(studentTeacherCourseDrawChart);
});

function teacherList(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/teacher/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            teacherNumber.number = data.totalElements;
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function studentList(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/student/list/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            studentNumber.number = data.totalElements;
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function allUserList(pageNo, pageSize) {
    jQuery.ajax({
        url: "http://localhost:7777/manager/accounts/findAll/",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            allUserNumber.number = data.length;
            for (let i = 0; i < data.length; i++) {
                let status = data[i].accountStatus;
                if (status === 'ACTIVATE') {
                    activateUserNumber.number++;
                }
                if (status === 'DEACTIVATE') {
                    deactivateUserNumber.number++;
                }
                if (status === 'AWAITING_APPROVAL') {
                    awaitingUserNumber.number++;
                }
                if (status === 'REJECTED') {
                    rejectedUserNumber.number++;
                }
            }
        }, error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}


function studentTeacherDrawChart() {
    const data = google.visualization.arrayToDataTable([
        ['تعداد اعضای فعال', 'تعداد'],
        ['دانشجویان', studentNumber.number],
        ['اساتید', teacherNumber.number],
        ['تعداد کل اعضا', allUserNumber.number],
    ]);

    // Optional; add a title and set the width and height of the chart
    const options = {'title': 'تعداد اعضای فعال', 'width': 550, 'height': 400};

    // Display the chart inside the <div> element with id="piechart"
    const chart = new google.visualization.PieChart(document.getElementById('studentTeacherChart'));
    chart.draw(data, options);
}

function accountStatusDrawChart() {
    const data = google.visualization.arrayToDataTable([
        ['وضعیت اعضا', 'تعداد'],
        ['در انتظار تایید', awaitingUserNumber.number],
        ['غیرفعال', deactivateUserNumber.number],
        ['رد شده', rejectedUserNumber.number],
        ['فعال', activateUserNumber.number],
    ]);

    // Optional; add a title and set the width and height of the chart
    const options = {'title': 'وضعیت اعضا', 'width': 550, 'height': 400};

    // Display the chart inside the <div> element with id="piechart"
    const chart = new google.visualization.PieChart(document.getElementById('accountStatusChart'));
    chart.draw(data, options);
}

function studentTeacherCourseDrawChart() {
    const data = google.visualization.arrayToDataTable([
        ['تعداد دوره های فعال و اعضای دارای دوره ی فعال', 'تعداد'],
        ['دانشجویان', 8],
        ['اساتید', 2],
        ['دوره', 2],
    ]);

    // Optional; add a title and set the width and height of the chart
    const options = {'title': 'تعداد دوره های فعال و اعضای دارای دوره ی فعال', 'width': 550, 'height': 400};

    // Display the chart inside the <div> element with id="piechart"
    const chart = new google.visualization.PieChart(document.getElementById('studentTeacherCourseChart'));
    chart.draw(data, options);
}
