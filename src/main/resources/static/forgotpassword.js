document.getElementById("forgot_form").addEventListener("submit", function(event) {
    event.preventDefault(); 

    const email = document.getElementById("email").value;
    const newPassword = document.getElementById("newPassword").value;
    // update pass
    fetch("/auth/forgotpassword", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email, newPassword: newPassword })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
    })
    .then(data => {
        alert("Password updated successfully!");
        window.location.href = "/authentication/login";
    })
    .catch(error => {
        alert("Failed to update password: " + error.message);
    });
});