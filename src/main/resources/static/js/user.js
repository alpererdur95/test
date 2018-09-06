let idTodelete = null;

function removePrivilege(id) {

    var privilegeId = id;
    var privilegeField = document.getElementById("privilege-" + id);
    var userTable = document.getElementById("usertable");


    $.ajax({
        url: "/api/deletePrivilege/" + privilegeId,
        type: "DELETE",

        success: function () {
            userTable.removeChild(privilegeField);
        },
        error: function (xhr) {

        }
    })
}

function getUser(id) {
    return new Promise(resolve => {
            $.ajax({
                url: "/api/user/" + id,
                type: "GET",
                success: function (data) {
                    resolve(data);
                },
                error: function (response) {
                    reject(response);

                }
            })
        }
    )
}


getUserAndShowPopup = function (event) {
    const id = event.target.id;
    getUser(id)
        .then(user => showPopup(user))

}

function showPopup(user) {

    var id = document.getElementById("modal-id");
    var firstname = document.getElementById("modal-first-name");
    var middlename = document.getElementById("modal-middle-name");
    var lastname = document.getElementById("modal-last-name");
    var email = document.getElementById("modal-email");
    var pass = document.getElementById("modal-pass");
    var cell = document.getElementById("modal-cell");
    id.value = user.uid;
    firstname.value = user.firstname;
    middlename.value = user.middlename;
    lastname.value = user.lastname;
    email.value = user.email;
    pass.value = user.password;
    cell.value = "";
    var titleUser = document.getElementById("title-seconduser")
    titleUser.innerText = "Kuıllanıcı Düzenleme > " + user.firstname + " " + user.lastname;
    var mainSelect = document.getElementById("cell-country");
    var checkedCell;
    var options = mainSelect.options;
    var selectedIndex;
    var selectedValue;
    for (var i = 0; i < options.length; i++) {
        if (user.cell.startsWith(options[i].value)) {
            selectedIndex = i;
            selectedValue = options[i].value;
            options[i].selected = true;
            checkedCell = user.cell.substring(options[i].value.length, user.cell.length);
            cell.value = checkedCell;
            break;
        }

    }


    var elems = document.querySelectorAll('select');
    M.FormSelect.init(elems);


}

function getPrivileges(id) {
    return new Promise(resolve => {
        $.ajax({
            url: "/api/getPrivilege/" + id,
            type: "GET",
            success: function (data) {
                resolve(data);
            },
            error: function (response) {
                reject(response);

            }
        })

    })
}

function getUserSecond(event) {
    const id = event.target.id;
    getUser(id).then(user => showSecond(user)).then(getPrivileges(id).then(userprivilege => filList(userprivilege)));


}

function showSecond(user) {
    var titleUser = document.getElementById("title-user");
    var resourceName = document.getElementById("resourceName");
    var privilegesSelect = document.getElementById("privileges");


    var ID = document.getElementById("ID");
    var userInfo = document.getElementById("userInfo");
    userInfo.value = user.firstname + " " + user.lastname;
    ID.value = user.uid;
    var middlename = user.middlename == "." ? " " : user.middlename;
    titleUser.innerText = "Yetkiler > " + user.firstname + " " + middlename + " " + user.lastname;
    resourceName.value="";
    privilegesSelect.value="";
    M.FormSelect.init(privilegesSelect);

}

function filList(userprivilege) {
    var table = document.getElementById("usertable");
    while (table.firstChild) {
        table.removeChild(table.firstChild);
    }


    if (userprivilege.length == 0) {
        var pMessage = document.createElement("p");
        pMessage.innerText = "Yetki Bulunmamaktır";
        table.appendChild(pMessage);
    } else {
        var thead = document.createElement("thead");
        var th1 = document.createElement("th");
        var th2 = document.createElement("th");
        var tr3 = document.createElement("tr");
        th1.innerText = "Yetki Adı";
        th2.innerText = "Baca ID";
        tr3.appendChild(th1);
        tr3.appendChild(th2);
        thead.appendChild(tr3)
        table.appendChild(thead);
        userprivilege.forEach(function (privilege) {
            var tr = document.createElement("tr");
            var td = document.createElement("td");
            var td2 = document.createElement("td");
            var td3 = document.createElement("td");
            var button = document.createElement("a");
            var i = document.createElement("i");


            var count = privilege.id;
            td.innerText = privilege.privilegename;
            td2.innerText = privilege.resourcename;

            button.classList.add("secondary-content");
            button.classList.add("user-item");
            i.classList.add("small");
            i.classList.add("material-icons");
            i.classList.add("privilege-delete");
            i.classList.add("add-cursor");
            i.setAttribute("title","Yetki Sil")
            tr.setAttribute("id", "privilege-" + count);

            i.setAttribute("onClick", "removePrivilege(" + count + ")");
            i.innerText = "delete";
            button.appendChild(i);
            td3.appendChild(button);
            tr.appendChild(td);
            tr.appendChild(td2);
            tr.appendChild(td3);

            table.appendChild(tr);
        })
    }

}

function updateUser() {

    var userid = document.getElementById("modal-id");
    var firstname = document.getElementById("modal-first-name");
    var middlename = document.getElementById("modal-middle-name");
    var lastname = document.getElementById("modal-last-name");
    var email = document.getElementById("modal-email");
    var pass = document.getElementById("modal-pass");
    var cell = document.getElementById("modal-cell");
    var itemCountry = document.getElementById("cell-country")
    var cellChecked;
    var cellFormat = cell.value.replace("(", "");
    cellFormat = cellFormat.replace(")", "");
    cellFormat = cellFormat.replace(" ", "");
    cellFormat = cellFormat.replace(" ", "");
    if (cell.value == "")
        cellChecked = "";
    else
        cellChecked = itemCountry.value + cellFormat;

    var responseUser = {
        uid: userid.value,
        firstname: firstname.value,
        middlename: middlename.value,
        lastname: lastname.value,
        email: email.value,
        password: pass.value,
        cell: cellChecked,
        is_deleted:false,

    };
    $.ajax({
        url: "/api/user/" + userid.value,
        contentType: "application/json; charset=utf-8",
        type: "PUT",
        data: JSON.stringify(responseUser),

             success: function (e) {

            window.location.href = "/users";


        },
        error: function (response) {
        window.alert(response.responseText);
            window.location.href = "/users";


        }
    })
}


document.addEventListener('DOMContentLoaded', function () {
    var elems = document.querySelector('#modal1');


    M.Modal.init(elems);

});

document.addEventListener('DOMContentLoaded', function () {


    var elems2 = document.querySelector('#modal2');


    M.Modal.init(elems2);
});
function addPrivilege() {

    var uid = document.getElementById("ID");
    var resource = document.getElementById("resourceName");
    var privileges = document.getElementById('privileges');
    var instance = M.FormSelect.getInstance(privileges);
    var elems2 = document.querySelector('#modal2');


    var responsePrivilige = {
        uid: uid.value,
        privilegesLists: instance.getSelectedValues(),
        resourceName: resource.value,


    }
    $.ajax({
        url: "/api/addPrivilege",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        data: JSON.stringify(responsePrivilige),

        success: function (e) { if(e==""){

        }
        else {window.location.reload(true);}
        },
        error: function (response) {
            var error = JSON.parse(response.responseText);
            alert(error.message);


        }
    })
}

function getParameterByName(name) {
    var url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}


function dynamicPage(count) {
    var page = getParameterByName("page");
    var counter = parseInt(page);
    if (isNaN(counter)) {
        counter = 1;
    }
     var dynamic = document.getElementById("page-open");
    if(count==="0"){
    }
    else{
        var liLeft = document.createElement("li");
        var iLeft = document.createElement("i");
        iLeft.classList.add("material-icons");
        iLeft.classList.add("add-cursor");
        iLeft.innerText = "chevron_left";
        iLeft.setAttribute("id", "pageLeft");

        if (counter == 1) {
            iLeft.classList.add("disabled");
            iLeft.classList.add("disabled-event");

        }
        else {
            iLeft.setAttribute("onClick", "clickLeft(" + count + ")");
        }
        liLeft.appendChild(iLeft);
        dynamic.appendChild(liLeft);
        for (var i = 0; i < count; i++) {
            var pageIndex = i + 1;
            var liElement = document.createElement("li");
            liElement.classList.add("waves-effect");
            if (counter === pageIndex)
                liElement.classList.add("active");

            var aElement = document.createElement("a");
            aElement.setAttribute("th:id", "${" + pageIndex + "}}");
            aElement.setAttribute("onClick", "staticSearch(" + pageIndex + ")");
            aElement.innerText = pageIndex.toString();
            liElement.appendChild(aElement);
            dynamic.appendChild(liElement);

        }


        var liRight = document.createElement("li");
        var iRight = document.createElement("i");
        iRight.setAttribute("id", "pageRight");
        iRight.classList.add("material-icons");
        iRight.classList.add("add-cursor");
        if (counter == parseInt(count)) {
            iRight.classList.add("disabled-right");
            iRight.classList.add("disabled");
        }
        else {
            iRight.setAttribute("onClick", "clickRight(" + count + ")");

        }
        iRight.innerText = "chevron_right";
        liRight.appendChild(iRight);
        dynamic.appendChild(liRight);
    }
}

function clickRight(count) {
    var Right = document.getElementById("pageRight");
    var counter = getParameterByName("page");
    var intCounter = parseInt(counter);
    if (isNaN(intCounter))
        intCounter = 1;
    Right.classList.remove("disabled");
    Right.classList.remove("disabled-event");
    intCounter += 1;
    staticSearch(intCounter);
}

function clickLeft(count) {
    var Left = document.getElementById("pageLeft");
    var getCounter = getParameterByName("page");
    var counter = parseInt(getCounter);
    if (counter == 1) {
        Left.classList.add("disabled");
        Left.classList.add("disabled-event");

    }
    if (counter != 1) {

        Left.classList.remove("disabled");
        Left.classList.remove("disabled");
        counter--;
        staticSearch(counter);
    }

}


function loadPage() {
    var count = document.getElementById("page-count").getAttribute("value");
    dynamicPage(count);

}

function load() {
    var searchKey = getParameterByName("search");
    if (searchKey == null)
        searchKey = " ";
    var searchElement = document.getElementById("searchingKey");
    searchElement.value = searchKey;
    loadPage();
}

function customSearch(page) {
    var search = document.getElementById("searchingKey");
    var pageNum;
    var searchingKey;
    if (search == null) {
        if (page)
            pageNum = page;


        else
            pageNum = 1;

        window.location.href = "/users?page=" + pageNum + "&" + "search=" + search2;

    }
    else {
        searchingKey = search.value;
        if (page)
            pageNum = page;

        else
            pageNum = 1;

        window.location.href = "/users?page=" + pageNum + "&" + "search=" + searchingKey;
    }


}

function goToUserPage() {
    var pageNumber = 1;
    var searchingKey = " ";
    window.location.href = "/users?page=" + pageNumber + "&" + "search=" + searchingKey;


}

function removeUser() {

    var id = idTodelete;

    $.ajax({
        url: "/api/user/delete/" + id,
        type: "DELETE",
        success: function (data) {
            window.location.reload(true);
        },
        error: function (response) {


        }
    })


}

function showDeletePopup(event) {
    idTodelete = event.target.id;
    getUser(idTodelete).then(user => titleDelete(user));


}

function titleDelete(user) {
    var titleDelete = document.getElementById("title-delete");
    var middlename = user.middlename == "." ? " " : user.middlename;
    titleDelete.innerText = "Kullanıcı > " + user.firstname + " " + middlename + " " + user.lastname;


}

function contuineDelete() {
    removeUser();
}

function clearİd() {
    idTodelete = null;

}

document.addEventListener('DOMContentLoaded', function () {
    var elems3 = document.querySelector('#modal3');


    M.Modal.init(elems3);

});

function goToQualist() {
    window.location.href = "https://www.qualist.com/"

}

function staticSearch(count) {
    var searchingKey = getParameterByName("search");
    if (searchingKey == null) {
        searchingKey = " ";
    }
    else {
        searchingKey = getParameterByName("search");

    }
    window.location.href = "/users?page=" + (count) + "&search=" + searchingKey;
}

function addUser() {
    var firstn = document.getElementById("first-name");
    var middlen = document.getElementById("middle-name");
    var lastn = document.getElementById("last-name");
    var emailg = document.getElementById("email-text");
    var pass = document.getElementById("password-text");
    var cell = document.getElementById("cell-text");
    var cellChecked;
    var itemCountry = document.getElementById("cell-country");
    var isDeleted = false;
    var instance = M.FormSelect.getInstance(itemCountry);
    var cellFormat = cell.value.replace("(", "");
    cellFormat = cellFormat.replace(")", "");
    cellFormat = cellFormat.replace(" ", "");
    cellFormat = cellFormat.replace(" ", "");
    if (cell.value == "")
        cellChecked = "";
    else
        cellChecked = itemCountry.value + cellFormat;
    var responseUser = {
        firstname: firstn.value,
        middlename: middlen.value,
        lastname: lastn.value,
        email: emailg.value,
        password: pass.value,
        cell: cellChecked,
        is_deleted: isDeleted,
    }

    $.ajax({
        url: "/api/user",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        data: JSON.stringify(responseUser),

        success: function () {
            goToUserPage();
        },
        error: function (response) {
            var error = JSON.parse(response.responseText);

            alert(error.message);
        }
    })
}


window.onload = load;
