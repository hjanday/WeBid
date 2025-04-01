// get params
const params = new URLSearchParams(window.location.search);
const auctionID = params.get("auctionId");


const token = localStorage.getItem("jwtToken");


// get auc with auth header
fetch(`http://localhost:8080/api/auctions/${auctionID}`, {
method: "GET",
headers: {
"Authorization": `Bearer ${token}`
}
})
.then(response => {
if (!response.ok) {
  throw new Error("Failed to fetch auction info");
}
return response.json();
})
.then(auctionData => {
document.getElementById("itemName").textContent = auctionData.itemName || "Untitled";
document.getElementById("itemDesc").textContent = auctionData.description || "No description";

const userId = auctionData.currentBidderID;

// get payments with auth header
fetch(`http://localhost:8080/api/payments/${auctionID}`, {
  method: "GET",
  headers: {
    "Authorization": `Bearer ${token}`
  }
})
  .then(resp => {
    if (!resp.ok) {
      throw new Error("Failed to fetch payment info");
    }
    return resp.json();
  })
  .then(paymentData => {
    document.getElementById("totalAmount").textContent = paymentData.totalCost || "0";
    document.getElementById("shipDaysSpan").textContent = paymentData.shippingDays || "N/A";
  })
  .catch(err => {
    console.error("Error fetching payment info:", err);
  });

if (!token) {
  alert("User not logged in!");
  setUserPlaceholders();
} else if (userId) {
  const userUrl = `http://localhost:8080/api/users/${userId}`;
  fetch(userUrl, {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}`
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to fetch user info");
      }
      return response.json();
    })
    .then(userData => {
      document.getElementById("username").textContent = userData.username || "N/A";
      document.getElementById("firstName").textContent = userData.firstName || "N/A";
      document.getElementById("lastName").textContent = userData.lastName || "N/A";
      document.getElementById("email").textContent = userData.email || "N/A";
      document.getElementById("country").textContent = userData.country || "N/A";
      document.getElementById("city").textContent = userData.city || "N/A";
      document.getElementById("postalCode").textContent = userData.postalCode || "N/A";
    })
    .catch(err => {
      console.error("Error fetching user info:", err);
      setUserPlaceholders();
    });
} else {
  setUserPlaceholders();
}
})
.catch(err => {
console.error("Error fetching auction info:", err);
});


function setUserPlaceholders() {
  document.getElementById("username").textContent = "Unknown";
  document.getElementById("firstName").textContent = "Unknown";
  document.getElementById("lastName").textContent = "Unknown";
  document.getElementById("email").textContent = "Unknown";
  document.getElementById("country").textContent = "Unknown";
  document.getElementById("city").textContent = "Unknown";
  document.getElementById("postalCode").textContent = "Unknown";
}