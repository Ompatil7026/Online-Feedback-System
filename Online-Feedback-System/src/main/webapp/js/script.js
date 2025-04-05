document.addEventListener("DOMContentLoaded", function () {
    const registerForm = document.querySelector(".register-form");
    const loginForm = document.querySelector(".login-form");

    // Hide login form initially
    loginForm.style.display = "none";

    document.getElementById("signIn").addEventListener("click", function () {
        registerForm.style.display = "none";
        loginForm.style.display = "block";
    });

    document.getElementById("signUp").addEventListener("click", function () {
        loginForm.style.display = "none";
        registerForm.style.display = "block";
    });

    // Redirect on button click
    document.getElementById("registerBtn").addEventListener("click", function () {
        window.location.href = "register.html";
    });

    document.getElementById("loginBtn").addEventListener("click", function () {
        window.location.href = "user_login.html";
    });
});
