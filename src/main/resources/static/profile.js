document.addEventListener("DOMContentLoaded", () => {

    // Obtaining Profile information
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

    // Obtaining notification information
    const notifUrl = "http://localhost:8080/api/notification";
    const notifList = document.getElementById("notificationList");
    fetch(notifUrl, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch notifications: User may not have any notifications");
        }
        return response.json(); // Expecting List<String>
    })
    .then(notifications => {
        notifList.innerHTML = ""; // Clear existing notifications

        if (notifications.length === 0) {
            notifList.innerHTML = "<li>No notifications</li>";
        } else {
            notifications.forEach(notif => {
                const li = document.createElement("li");
                li.textContent = notif;
                notifList.appendChild(li);
            });
        }
    })
    .catch(error => {
        console.error("Error fetching notifications:", error);
        notifList.innerHTML = "User has no notifications ...";
    });
});
