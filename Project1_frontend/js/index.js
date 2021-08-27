let loginButton = document.getElementById('login');
let usernameInput = document.getElementById('username');
let passwordInput = document.getElementById('password');

function login(event) {

    event.preventDefault();

    const loginInfo = {
        'username': usernameInput.value,
        'password': passwordInput.value
    }

    fetch("http://127.0.0.1:7000/login", {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginInfo)
    }).then((response) => {
        if (response.status === 400) {
            displayInvalidLogin();
        } else if (response.status === 401) {
            displayInvalidLogin();
        } else if (response.status === 200) {
            return response.json();
        }
    }).then((user) => {
        return fetch(`http://127.0.0.1:7000/login/${user.id}`, {
            method: "GET",
            credentials: "include"
        });
    }).then((response) => {
        return response.json();
    }).then((userRole) => {
        if (userRole === "employee") {
            window.location.href = "/viewreimbursement.html";
        } else if (userRole === "finance manager") {
            window.location.href = "/adminreimbursement.html";
        }
    })
};


function displayInvalidLogin() {
    let errorMessage = "Invalid login";
    let eMessage = document.getElementById("login-message");
    eMessage.style.color = "red";
    eMessage.innerHTML = errorMessage;
    setTimeout(function () {
        eMessage.innerHTML = "";
    }, 1000);
}

function checkIfUserCurrentlyLogin(event) {
    fetch("http://127.0.0.1:7000/currentUser", {
        method: "GET",
        credentials: "include"
    }).then((response) => {
        if (response.status === 401) {

        } else if (response.status == 200) {
            return response.json();
        }
    }).then((user) => {
        return fetch(`http://127.0.0.1:7000/login/${user.id}`, {
            method: "GET",
            credentials: "include"
        });
    }).then((response) => {
        return response.json();
    }).then((userRole) => {
        if (userRole === "employee") {
            window.location.href = "/viewreimbursement.html";
        } else if (userRole === "finance manager") {
            window.location.href = "/adminreimbursement.html";
        }
    });
}

loginButton.addEventListener("click", login);
window.addEventListener("load", checkIfUserCurrentlyLogin)