$("#users-list").ready(function () {
    usersListFirstTime(0, 5);
});

$('#pageSizeUsersList').change(function () {
    var value = $(this).val();
    if (value === null || value === "") {
        value = 10;
    }
    $("#users-list-paging").empty();
    usersListFirstTime(0, parseInt(value));
});

function usersListFirstTime(pageNo, pageSize) {
    const accountStatusDTO = {
        "field": 'status',
        "status": 'DEACTIVATE'
    };
    jQuery.ajax({
        url: "http://localhost:7777/manager/accounts/all-account-status-not/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(accountStatusDTO),
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

function usersListAfterFirstTime(pageNo, pageSize) {
    const accountStatusDTO = {
        "field": 'status',
        "status": 'DEACTIVATE'
    };
    jQuery.ajax({
        url: "http://localhost:7777/manager/accounts/all-account-status-not/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(accountStatusDTO),
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
    var value = $("#pageSizeUsersList").val();
    if (value === null || value === "") {
        value = 5;
    }
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="usersListAfterFirstTime(\'' + i + '\' , \'' + value + '\')">' + (i + 1) + '</li>';
        }
        $("#users-list-paging").append(middle);
    }
}

function prepareTable(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        let status = data[i].accountStatus;
        if (status === 'ACTIVATE')
            status = 'فعال';
        else if (status === 'DEACTIVATE')
            status = 'غیرفعال';
        else if (status === 'AWAITING_APPROVAL')
            status = 'در انتظار تایید';
        else if (status === 'REJECTED')
            status = 'رد شده';

        let role = '';
        for (let j = 0; j < data[i].roles.length; j++) {
            if (data[i].roles[j].roleType === "ROLE_STUDENT")
                role = 'دانشجو';
            if (data[i].roles[j].roleType === "ROLE_TEACHER")
                role = 'استاد';
        }
        let createAccountDate = new Date(data[i].createAccountDate);
        let email = data[i].person.communication.email;
        let phoneNumber = data[i].person.communication.phoneNumber;
        let cellPhoneNumber = data[i].person.communication.cellPhoneNumber;
        let address = data[i].person.communication.address;
        var birthOfDate = new Date(data[i].person.birthOfDate);
        content += "<tr>";
        content += "<td class='text-center'><input class='form-check-input' type='checkbox' value='" + data[i].id + "'></td>";
        content += "<td>" + data[i].id + "</td>";
        content += "<td >" + data[i].person.firstName + "</td>";
        content += "<td >" + data[i].person.lastName + "</td>";
        content += "<td >" + data[i].person.fathersName + "</td>";
        content += "<td >" + data[i].person.nationalCode + "</td>";
        content += "<td >" + jDateFunctions.prototype.gregorian_to_jalali(birthOfDate) + "</td>";
        content += "<td >" + data[i].person.degreeOfEducation + "</td>";
        content += "<td >" + data[i].username + "</td>";
        content += "<td >" + role + "</td>";
        content += "<td >" + status + "</td>";
        content += "<td >" +
            '<button type="button" class="btn btn-info btn-sm" onclick="showDetails(\'' + jDateFunctions.prototype.gregorian_to_jalali(createAccountDate) + '\' , \'' + email + '\' , \'' + phoneNumber + '\', \'' + cellPhoneNumber + '\', \'' + address + '\')">مشاهده</button></td>';
        content += "</tr>";
    }
    $('#users-table-body').html(content);
}

function showDetails(createAccountDate, email, phoneNumber, cellPhoneNumber, address) {
    $("#createAccountDate").text(createAccountDate);
    $("#email").text(email);
    $("#phoneNumber").text(phoneNumber);
    $("#cellPhoneNumber").text(cellPhoneNumber);
    $("#address").text(address);
    $("#detailsModal").modal('show');
}

var submitData = function () {
    let checks = [];
    $('input[type="checkbox"]:checked').each(function (i) {
        checks[i] = $(this).val();
    });

    let action = $('#action option:selected').val();

    if (action === 'AcceptAllSelected') {
        acceptAllSelected(checks);
    } else if (action === 'DismissAllSelected') {
        dismissAllSelected(checks);
    } else if (action === 'AcceptAll') {
        acceptAll();
    } else if (action === 'DismissAll') {
        dismissAll();
    }
    return false;
};

function acceptAllSelected(checks) {
    let newUsersIdsList = {
        "secret": "ajax",
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/manager/new-user-list/accept-all-selected",
        type: "POST",
        data: JSON.stringify(newUsersIdsList),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('users-list');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function acceptAll() {
    $.ajax({
        url: serverUrl() + "/manager/new-user-list/accept-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('users-list');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function dismissAllSelected(checks) {
    let newUsersIdsList = {
        "secret": "ajax",
        "listId": checks
    };
    $.ajax({
        url: serverUrl() + "/manager/new-user-list/dismiss-all-selected",
        type: "POST",
        data: JSON.stringify(newUsersIdsList),
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('users-list');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}

function dismissAll() {
    $.ajax({
        url: serverUrl() + "/manager/new-user-list/dismiss-all",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (result) {
            showAlert('success', 'عملیات با موفقیت انجام شد');
            loadPage('users-list');
        },
        error: function (errorMessage) {
            showAlert('danger', errorMessage.responseJSON.message);
        }
    })
}
