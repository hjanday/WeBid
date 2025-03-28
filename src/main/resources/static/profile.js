document.addEventListener("DOMContentLoaded", () => {
    const userUrl = "http://localhost:8080/api/users/currentuser";
    const token = localStorage.getItem("jwtToken"); // Assuming token is stored in local storage

    fetch(userUrl, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch user data");
        }
        return response.json();
    })
    .then(userData => {
        document.getElementById("userEmail").textContent = userData.username;
        document.getElementById("userid").textContent = userData.id;
    })
    .catch(error => {
        console.error("Error fetching user data:", error);
        alert("Failed to retrieve user information.");
    });
});
