function searchUsersListFirstTime(pageNo, pageSize) {
    const AccountSearchDTO = {
        "username": $("#usernameSearchUsersListInput").val(),
        "firstName": $("#firstNameSearchUsersListInput").val(),
        "lastName": $("#lastNameSearchUsersListInput").val(),
        "fathersName": $("#fathersNameSearchUsersListInput").val(),
        "nationalCode": $("#nationalCodeSearchUsersListInput").val(),
        "degreeOfEducation": $("#degreeOfEducationSearchUsersListInput").val(),
        "phoneNumber": $("#phoneNumberSearchUsersListInput").val(),
        "cellPhoneNumber": $("#cellPhoneNumberSearchUsersListInput").val(),
        "email": $("#emailSearchUsersListInput").val(),
        "address": $("#addressSearchUsersListInput").val(),
        "role": $('#roleSearchUsersListInput option:selected').val(),
        "status": 'notEqualDeactivate'
    };

    jQuery.ajax({
        url: "http://localhost:7777/manager/users-list/search/accounts/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(AccountSearchDTO),
        headers: {
            "Authorization": "Basic " + btoa(usernameHeader + ":" + passwordHeader)
        },
        success: function (data, textStatus, jQxhr) {
            $("#users-table-body").empty();
            $("#users-list-paging").empty();
            pagingTableResultSearchUsersList(JSON.parse(data.totalPages));
            prepareTableResultSearchUsersList(data.content);
        },
        error: function (errorMessage) {
            //alert(errorMessage)
        }
    });
}

function searchUsersListAfterFirstTime(pageNo, pageSize) {
    const AccountSearchDTO = {
        "username": $("#usernameSearchUsersListInput").val(),
        "firstName": $("#firstNameSearchUsersListInput").val(),
        "lastName": $("#lastNameSearchUsersListInput").val(),
        "fathersName": $("#fathersNameSearchUsersListInput").val(),
        "nationalCode": $("#nationalCodeSearchUsersListInput").val(),
        "degreeOfEducation": $("#degreeOfEducationSearchUsersListInput").val(),
        "phoneNumber": $("#phoneNumberSearchUsersListInput").val(),
        "cellPhoneNumber": $("#cellPhoneNumberSearchUsersListInput").val(),
        "email": $("#emailSearchUsersListInput").val(),
        "address": $("#addressSearchUsersListInput").val(),
        "role": $('#roleSearchUsersListInput option:selected').val(),
        "status": 'notEqualDeactivate'
    };
    jQuery.ajax({
        url: "http://localhost:7777/manager/users-list/search/accounts/" + pageNo + "/" + pageSize,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(AccountSearchDTO),
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

function pagingTableResultSearchUsersList(totalPage) {
    let middle = '';
    if (totalPage !== 0) {
        for (let i = 0; i < totalPage; i++) {
            middle += '<li class="page-item"><a class="page-link" onclick="searchUsersListAfterFirstTime(' + i + ',10)">' + (i + 1) + '</li>';
        }
        $("#users-list-paging").append(middle);
    }
}

function prepareTableResultSearchUsersList(data) {
    let content = '';
    for (let i = 0; i < data.length; i++) {
        let status = 'در انتظار تایید';
        let role = '';
        for (let j = 0; j < data[i].roles.length; j++) {
            if (data[i].roles[j].roleType === "ROLE_STUDENT")
                role = 'دانشجو';
            if (data[i].roles[j].roleType === "ROLE_TEACHER")
                role = 'استاد';
        }
        let createAccountDate = new Date(data[i].createAccountDate) ;
        let email = data[i].person.communication.email;
        let phoneNumber = data[i].person.communication.phoneNumber;
        let cellPhoneNumber = data[i].person.communication.cellPhoneNumber;
        let address = data[i].person.communication.address;
        var birthOfDate =  new Date(data[i].person.birthOfDate);
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
