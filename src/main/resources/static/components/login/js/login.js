let loginUsername = '';
$(document).ready(function () {
    $("#loginForm").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        event.stopPropagation();

        let username = $("#username").val();
        let password = $("#password").val();

        if (username !== '' && password !== '') {
            loginToAccount();
        }
    });

});

function loginToAccount() {
    const username = $("#username").val();
    const password = $("#password").val();

    const LoginToAccountByUserCommand = {
        "username": username,
        "password": password
    };

    $.ajax({
        url: "http://localhost:7777/user/login",
        type: "POST",
        data: JSON.stringify(LoginToAccountByUserCommand),
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password)
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            loginUsername = data.username;
            let guest = false;

            for (let i = 0; i < data.roles.length; i++) {
                if (data.roles[i].roleType === "ROLE_GUEST") {
                    guest = true;
                    break;
                }
            }

            if (guest) {
                location.replace("/components/panels/guest/index/guest-panel.html?" + btoa(username + ":" + password))
            } else {
                for (let i = 0; i < data.roles.length; i++) {
                    if (data.roles[i].roleType === "ROLE_STUDENT") {
                        location.replace("/components/panels/student/index/student-panel.html?&role=" + data.roles[0].roleType  +"&@" + btoa(username + ":" + password));
                    }
                    if (data.roles[i].roleType === "ROLE_TEACHER") {
                        location.replace("/components/panels/teacher/index/teacher-panel.html?&role=" + data.roles[0].roleType  +"&@" + btoa(username + ":" + password));
                    }
                    if (data.roles[i].roleType === "ROLE_MANAGER") {
                        location.replace("/components/panels/manager/index/manager-panel.html?" + btoa(username + ":" + password));
                    }
                }
            }
        },
        error: function (errorMessage) {
            showAlert("danger", 'اطلاعات وارد شده صحیح نمی باشد');
        }
    });
}



