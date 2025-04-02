const token = localStorage.getItem("jwtToken"); 
// get tok from storage ^^

document.addEventListener("DOMContentLoaded", () => {

    // get user details
    const userUrl = "http://localhost:8080/api/users/currentuser";

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
    // show data
    .then(userData => {
        document.getElementById("userEmail").textContent = userData.email;
        document.getElementById("userid").textContent = userData.id;
        document.getElementById("firstName").textContent = userData.firstName;
        document.getElementById("lastName").textContent = userData.lastName;
        document.getElementById("city").textContent = userData.city;
        document.getElementById("country").textContent = userData.country;
        document.getElementById("postalCode").textContent = userData.postalCode;
        document.getElementById("address").textContent = userData.address;

    })
    .catch(error => {
        console.error("Error fetching user data:", error);
        alert("Failed to retrieve user information.");
    });

    // get notifs
    const notifUrl = "http://localhost:8080/api/notification";
    const notifList = document.getElementById("notificationList");
    fetch(notifUrl, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
    // check if notifs retrieved
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch notifications: User may not have any notifications");
        }
        return response.json(); 
    })
    .then(notifications => {
        notifList.innerHTML = ""; 

        if (notifications.length === 0) {
            notifList.innerHTML = "No notifications";
        } else {
            notifications.forEach(notif => {
                const notifItem = document.createElement("p"); 
                notifItem.textContent = notif;
                notifItem.classList.add("notification-item");
                notifList.appendChild(notifItem);
            });
        }
    })
    .catch(error => {
        console.error("Error fetching notifications:", error);
        notifList.innerHTML = "User has no notifications ...";
    });
});

// deleting notifications
document.getElementById("clearNotificationsBtn").addEventListener("click", () => {
    const token = localStorage.getItem("jwtToken");
    const notifUrl = "http://localhost:8080/api/notification/deleteAll";

    fetch(notifUrl, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        },
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(errorMessage => { 
                throw new Error(errorMessage || "Failed to delete notifications"); 
            });
        }
        return response.text(); 
    })
    .then(message => {
        alert(message);
        document.getElementById("notificationList").innerHTML = "<li>User has no notifications ...</li>"; 
    })
    .catch(error => {
        console.error("Error deleting notifications:", error);
        alert(error.message);
    });
});