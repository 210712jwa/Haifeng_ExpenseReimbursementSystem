
let logoutButton = document.getElementById("logout");
let submitButton = document.getElementById('submit');
let amountSubmit = document.getElementById('amountInput');
let typeSubmit = document.getElementById('typeFormControlSelect1');
let descriptionSubmit = document.getElementById('descirptionFormControlTextarea1');
let postButton = document.getElementById('post');

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
        return fetch(`http://127.0.0.1:7000/user/${user.id}/reimbursement`, {
            method: "GET",
            credentials: "include"
        });
    }).then((response) => {
        return response.json();
    }).then((reimbursement) => {
        populateReimbursements(reimbursement);
    }).catch((error) => {
        console.log(error);
    })
};


function logout(event) {
    event.preventDefault();
    fetch("http://127.0.0.1:7000/logout", {
        method: 'POST',
        credentials: "include"
    }).then((response) => {
        if (response.status === 200) {
            window.location.href = "/index.html";
        } else if (response.status === 400) {

        }
    }).catch((error) => {
        console.log(error);
    })

};

function submit(event) {
    event.preventDefault();
    const submitValue = {
        'amount': amountSubmit.value,
        'type': typeSubmit.value,
        'description': descriptionSubmit.value
    }
    let formData = new FormData();
    var fileInput = document.getElementById('fileUpload');
    var file = fileInput.files[0]
    formData.append('reciept', file);

    fetch("http://127.0.0.1:7000/currentUser", {
        method: "GET",
        credentials: "include"
    }).then((response) => {
        if (response.status === 401) {
            window.location.href = "/index.html";
        } else if (response.status === 200) {
            return response.json();
        }
    }).catch((error) => {
        console.log(error);
    }).then((user) => {
        return fetch(`http://127.0.0.1:7000/user/${user.id}/reimbursement`, {
            method: "POST",
            credentials: "include",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(submitValue)
        });
    }).then((respond) => {
        if (respond.status === 200 && file !== undefined) {
            return respond.json();
        } else {
            location.reload();
        }
    }).then((reimbursement) => {
        return fetch(`http://127.0.0.1:7000/user/${reimbursement.author.id}/reimbursement/${reimbursement.id}/reciept`, {
            method: "PUT",
            credentials: "include",
            body: formData
        });
    }).then((respond) => {
        if (respond.status === 200) {
            location.reload();
        }
    }).catch((error) => {
        console.log(error);
    })
}

function previewImage() {
    $(imageModalCenter).modal('toggle');
    let src = $(this).attr('src');
    let imageView = document.getElementById('imgid');
    imageView.src = src;
}

function editRow() {
    var reimbursementStatus2 = $(this).closest("tr").find("td:nth-child(9)").text();
    if (reimbursementStatus2 == 'pending') {
        var reimbursementId2 = $(this).closest("tr").find("td:first").text();
        var reimbursementAmount2 = $(this).closest("tr").find("td:nth-child(2)").text();
        var reimbursementType2 = $(this).closest("tr").find("td:nth-child(3)").text();
        var reimbursementDesc2 = $(this).closest("tr").find("td:nth-child(4)").text();
        $(ReimbursementModalCenter).modal('toggle');
        let amountChange = document.getElementById('amountInput');
        let typeChange = document.getElementById('typeFormControlSelect1');
        let descriptionChange = document.getElementById('descirptionFormControlTextarea1');
        amountChange.value = reimbursementAmount2;
        typeChange.value = reimbursementType2;
        descriptionChange.value = reimbursementDesc2;
        var change = "<button class='btn btn-primary' id='change'>Change</button>";
        var deleteReimb = "<button class='btn btn-danger' id='deleteReimb'>Delete</button>";
        document.getElementById("submit").style.visibility = 'hidden';
        document.getElementById("popupselection").innerHTML = deleteReimb + change;
        let changeButton = document.getElementById('change');
        changeButton.addEventListener('click', function () {
            const changeValue = {
                'amount': amountChange.value,
                'type': typeChange.value,
                'description': descriptionChange.value,
            }
            let formData = new FormData();
            var fileInput = document.getElementById('fileUpload');
            var file = fileInput.files[0]
            formData.append('reciept', file);

            fetch("http://127.0.0.1:7000/currentUser", {
                method: "GET",
                credentials: "include"
            }).then((response) => {
                if (response.status === 401) {
                    window.location.href = "/index.html";
                } else if (response.status === 200) {
                    return response.json();
                }
            }).catch((error) => {
                console.log(error);
            }).then((user) => {
                return fetch(`http://127.0.0.1:7000/user/${user.id}/reimbursement/${reimbursementId2}`, {
                    method: "PUT",
                    credentials: "include",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(changeValue)
                });
            }).then((respond) => {
                if (respond.status === 200 && file !== undefined) {
                    return respond.json();
                } else {
                    location.reload();
                }
            }).then((reimbursement) => {
                return fetch(`http://127.0.0.1:7000/user/${reimbursement.author.id}/reimbursement/${reimbursement.id}/reciept`, {
                    method: "PUT",
                    credentials: "include",
                    body: formData
                });
            }).then((respond) => {
                if (respond.status === 200) {
                    location.reload();
                }
            }).catch((error) => {
                console.log(error);
            })
        });
        let deleteButton = document.getElementById('deleteReimb');
        deleteButton.addEventListener('click', function () {
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
                return fetch(`http://127.0.0.1:7000/user/${user.id}/reimbursement/${reimbursementId2}`, {
                    method: "Delete",
                    credentials: "include"
                });
            }).then((respond) => {
                if (respond.status === 200) {
                    location.reload();
                }
            })
        });
    }
}


function populateReimbursements(reimbursementArray) {
    let tbody = document.querySelector('#reimbursement tbody');
    var i;
    for (const reimbursement of reimbursementArray) {

        let tr = document.createElement('tr');

        let reimbursementIdTd = document.createElement('td');
        reimbursementIdTd.innerHTML = reimbursement.id;

        let reimbursementAmountTd = document.createElement('td');
        reimbursementAmountTd.innerHTML = reimbursement.amount;

        let reimbursementTypeTd = document.createElement('td');
        if (reimbursement?.type?.type) {
            reimbursementTypeTd.innerHTML = reimbursement.type.type;
        }
        let reimbursementDescriptionTd = document.createElement('td');
        reimbursementDescriptionTd.innerHTML = reimbursement.description;

        let reimbursementSubmittedTd = document.createElement('td');
        reimbursementSubmittedTd.innerHTML = new Date(reimbursement.submitted).toDateString();

        let reimbursementResolvedTd = document.createElement('td');
        reimbursementResolvedTd.innerHTML = reimbursement.resolved ? new Date(reimbursement.resolved).toDateString() : null;


        let reimbursementResolverTd = document.createElement('td');
        if (reimbursement?.resolver?.firstName) {
            if (reimbursement?.resolver?.lastName) {
                reimbursementResolverTd.innerHTML = reimbursement.resolver.firstName + ' ' + reimbursement.resolver.lastName;
            }
        }

        let reimbursementRecieptTd = document.createElement('td');
        let reimbursementImage = document.createElement('img');
        reimbursementImage.style.width = "50px";
        reimbursementImage.id = `itemPreview_${i}`;
        if (reimbursement.recieptImage !== null) {
            reimbursementImage.src = "data:image/png;base64," + reimbursement.recieptImage;
            reimbursementRecieptTd.appendChild(reimbursementImage);
        }

        let reimbursementStatusTd = document.createElement('td');
        if (reimbursement?.status?.status) {
            reimbursementStatusTd.innerHTML = reimbursement.status.status;
        }
        if (reimbursement.status.status === "denied") {
            reimbursementStatusTd.style.color = "red";
        } else if (reimbursement.status.status === "approved") {
            reimbursementStatusTd.style.color = "green";
        }
        i++;

        reimbursementImage.addEventListener('mousedown', previewImage);

        tr.addEventListener('click', editRow);

        tr.appendChild(reimbursementIdTd);
        tr.appendChild(reimbursementAmountTd);
        tr.appendChild(reimbursementTypeTd);
        tr.appendChild(reimbursementDescriptionTd);
        tr.appendChild(reimbursementSubmittedTd);
        tr.appendChild(reimbursementResolvedTd);
        tr.appendChild(reimbursementResolverTd);
        tr.appendChild(reimbursementRecieptTd);
        tr.appendChild(reimbursementStatusTd);
        tbody.appendChild(tr);

    }
}

function clearInput() {
    amountSubmit.value = '';
    descriptionSubmit.value = '';
}


postButton.addEventListener('click', clearInput);
submitButton.addEventListener('click', submit);
logoutButton.addEventListener("click", logout);
window.addEventListener('load', onLoad);


