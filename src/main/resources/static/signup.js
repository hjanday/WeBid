document.getElementById("register_form").addEventListener("submit", function(event) {
    event.preventDefault(); 
    // get form data
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const address = document.getElementById("address").value;
    const postalCode = document.getElementById("postalCode").value;
    const country = document.getElementById("country").value;
    const city = document.getElementById("city").value;
    
    // sign up user
    fetch("/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            username: username,
            email: email,
            password: password,
            address: address,
            postalCode: postalCode,
            country: country,
            city: city
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
    })
    .then(data => {
        alert("Registration successful!");
        window.location.href = "/authentication/login";
    })
    .catch(error => {
        alert("Registration failed: " + error.message);
    });
});