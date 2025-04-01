document.getElementById("login_form").addEventListener("submit", function(event) {
    event.preventDefault(); 
    // get email/pw from form
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
   // login endpoint
    fetch("/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email, password: password })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
        var userRole = data.roles[0]
        alert("Login successful! Token: " + data.token);
        localStorage.clear()
        localStorage.setItem("jwtToken", data.token);

        // check for admin
        if (userRole === "ROLE_ADMIN") {
            window.location.replace("/admin/dashboard");
            return;
        }
        window.location.replace("/auth/auctions");
    })
    .catch(error => {
        alert("Login failed: " + error.message);
    });
});