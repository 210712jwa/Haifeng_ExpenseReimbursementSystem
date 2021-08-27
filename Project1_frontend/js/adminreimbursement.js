let logoutButton = document.getElementById("logout");
let approveFilter = document.getElementById("approvefilter");
let deniedFilter = document.getElementById("deniedfilter");
let pendingFilter = document.getElementById("pendingfilter");
let defaultFilter = document.getElementById("showdefault");
let loadCookie;

function onLoad(event) {
    fetch("http://127.0.0.1:7000/currentUser", {
        method: "GET",
        credentials: "include"
    }).then((response) => {
        if (response.status === 401) {
            window.location.href = "/index.html";
        } else if (response.status === 200) {
            return response.json();
        }
    }).then((user) => {
        return fetch(`http://127.0.0.1:7000/admin/${user.id}/reimbursement`, {
            method: "GET",
            credentials: "include"
        });
    }).then((response) => {
        return response.json();
    }).then((reimbursement) => {
        if (loadCookie == null) {
            clearTable();
            populateReimbursements(reimbursement);
            document.cookie = "cookieloaded=load; expires=Thu, 18 Dec 2013 12:00:00 UTC; path=/";
        }
    }).catch(error => {
        console.log(error)

    })
};


function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function showAll() {
    document.cookie = "cookieloaded=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/"
    location.reload();
}

function logout(event) {
    event.preventDefault();
    fetch("http://127.0.0.1:7000/logout", {
        method: 'POST',
        credentials: "include"
    }).then((response) => {
        if (response.status === 200) {
            window.location.href = "/index.html";
        }
    }).catch((error) => {
        console.log(error);
    })

};

function decision(e) {
    e.preventDefault();
    var reimbursementId = $(this).closest("tr").find("td:first").text();
    var reimbursementStatus = $(this).val();
    fetch("http://127.0.0.1:7000/currentUser", {
        method: "GET",
        credentials: "include"
    }).then((response) => {
        if (response.status === 401) {
            window.location.href = "/index.html";
        } else if (response.status === 200) {
            return response.json();
        }
    }).then((user) => {
        return fetch(`http://127.0.0.1:7000/admin/${user.id}/reimbursement/${reimbursementId}`, {
            method: "PUT",
            credentials: "include",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reimbursementStatus)
        });
    }).then((response) => {
        if (response.status === 200) {
            location.reload();
            return response.json();
        }
    }).catch((error) => {
        console.log(error);
    })
};

function statusFilter(event) {
    var filterValue = $(this).text();
    fetch("http://127.0.0.1:7000/currentUser", {
        method: "GET",
        credentials: "include"
    }).then((response) => {
        if (response.status === 401) {
            window.location.href = "/index.html";
        } else if (response.status === 200) {
            return response.json();
        }
    }).then((user) => {
        return fetch(`http://127.0.0.1:7000/admin/${user.id}/reimbursement?status=${filterValue}`, {
            method: "Get",
            credentials: "include"
        });
    }).then((response) => {
        if (response.status == 200) {
            return response.json();
        }
    }).then((reimbursement) => {
        if (Object.keys(reimbursement).length !== 0) {
            clearTable();
            populateReimbursements(reimbursement);
        } else {
            clearTable();
        }
    }).catch((error) => {
        console.log(error);
    })
}

function clearTable() {
    let tbody = document.querySelector('#reimbursement-table tbody');
    tbody.innerHTML = '';
}


function previewImage() {
    $(imageModalCenter).modal('toggle');
    let src = $(this).attr('src');
    let imageView = document.getElementById('imgid');
    imageView.src = src;
}


function populateReimbursements(reimbursementArray) {
    let tbody = document.querySelector('#reimbursement-table tbody');
    var i = 0;
    for (const reimbursement of reimbursementArray) {

        var tr = document.createElement('tr');

        var reimbursementIdTd = document.createElement('td');
        reimbursementIdTd.innerHTML = reimbursement.id;

        var reimbursementAmountTd = document.createElement('td');
        reimbursementAmountTd.innerHTML = reimbursement.amount;

        var reimbursementTypeTd = document.createElement('td');
        reimbursementTypeTd.innerHTML = reimbursement.type.type;

        var reimbursementDescriptionTd = document.createElement('td');
        reimbursementDescriptionTd.innerHTML = reimbursement.description;

        var reimbursementSubmittedTd = document.createElement('td');
        reimbursementSubmittedTd.innerHTML = new Date(reimbursement.submitted).toDateString();

        var reimbursementResolvedTd = document.createElement('td');
        reimbursementResolvedTd.innerHTML = reimbursement.resolved ? new Date(reimbursement.resolved).toDateString() : null;


        var reimbursementAuthorTd = document.createElement('td');
        if (reimbursement?.author?.firstName && reimbursement?.author?.lastName) {
            reimbursementAuthorTd.innerHTML = reimbursement.author.firstName + " " + reimbursement.author.lastName;
        }

        let reimbursementRecieptTd = document.createElement('td');
        let reimbursementImage = document.createElement('img');
        reimbursementImage.style.width = "50px";
        reimbursementImage.id = `itemPreview_${i}`;
        if (reimbursement.recieptImage !== null) {
            reimbursementImage.src = "data:image/png;base64," + reimbursement.recieptImage;
            reimbursementRecieptTd.appendChild(reimbursementImage);
        }

        var reimbursementStatusTd = document.createElement('td');
        reimbursementStatusTd.innerHTML = reimbursement.status.status;
        if (reimbursement.status.status === "denied") {
            reimbursementStatusTd.style.color = "red";
        } else if (reimbursement.status.status === "approved") {
            reimbursementStatusTd.style.color = "green";
        }

        reimbursementImage.addEventListener('mousedown', previewImage);
        tr.appendChild(reimbursementIdTd);
        tr.appendChild(reimbursementAmountTd);
        tr.appendChild(reimbursementTypeTd);
        tr.appendChild(reimbursementDescriptionTd);
        tr.appendChild(reimbursementSubmittedTd);
        tr.appendChild(reimbursementResolvedTd);
        tr.appendChild(reimbursementAuthorTd);
        tr.appendChild(reimbursementRecieptTd);
        tr.appendChild(reimbursementStatusTd);
        if (reimbursement.status.status == "pending") {
            var btn = document.createElement("button");
            btn.type = 'button';
            btn.className = 'btn btn-outline-success';
            btn.id = `btn_approve_${i}`;
            btn.value = 'approved';
            btn.style.width = '80px'
            btn.textContent = 'Approve';
            btn.addEventListener('click', decision);
            var btn1 = document.createElement("button");
            btn1.type = 'button';
            btn1.className = 'btn btn-outline-danger';
            btn1.id = `btn_approve_${i}`;
            btn1.value = 'denied';
            btn1.style.width = '80px'
            btn1.textContent = 'Deny';
            btn1.style.marginTop = '10px';
            btn1.addEventListener('click', decision);

            i++;
            let linkbreak = document.createElement("br");
            let reimbursementApproveDecisionTd = document.createElement('td');
            reimbursementApproveDecisionTd.appendChild(btn);
            reimbursementApproveDecisionTd.appendChild(linkbreak);
            reimbursementApproveDecisionTd.appendChild(btn1);
            tr.appendChild(reimbursementApproveDecisionTd);
        } else {
            var btn2 = document.createElement("button");
            btn2.type = 'button'
            btn2.textContent = 'Decided';
            btn2.className = 'btn btn-secondary';
            var reimbursementDecidedTd = document.createElement('td');
            reimbursementDecidedTd.appendChild(btn2);
            tr.appendChild(reimbursementDecidedTd);
        }
        tbody.appendChild(tr);
    }

}


window.addEventListener('load', onLoad);
logoutButton.addEventListener("click", logout);
approveFilter.addEventListener('click', statusFilter);
pendingFilter.addEventListener('click', statusFilter);
deniedFilter.addEventListener('click', statusFilter);
defaultFilter.addEventListener('click', showAll);
