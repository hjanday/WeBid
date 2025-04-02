// Grab query params
const queryParams = new URLSearchParams(window.location.search);
const total = queryParams.get("total");   // e.g. 123.45
const auctionID = queryParams.get("auctionId"); // e.g. 42
const token = localStorage.getItem("jwtToken");
document.getElementById("totalAmount").textContent = total || "0";


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
.then(usrData => {
console.log("Auction data:", usrData.currentBidderID);
let userId = usrData.currentBidderID;
if (!token) {
alert("User not logged in!");
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
  // show details
  console.log("User data:", userData);
  document.getElementById("firstName").textContent = userData.firstName || "N/A";
  document.getElementById("lastName").textContent = userData.lastName || "N/A";
  document.getElementById("email").textContent = userData.email || "N/A";
  document.getElementById("country").textContent = userData.country || "N/A";
  document.getElementById("city").textContent = userData.city || "N/A";
  document.getElementById("postalCode").textContent = userData.postalCode || "N/A";
})
.catch(err => {
  console.error("Error fetching user info:", err);
  document.getElementById("firstName").textContent = "Unknown";
  document.getElementById("lastName").textContent = "Unknown";
  document.getElementById("email").textContent = "Unknown";
  document.getElementById("country").textContent = "Unknown";
  document.getElementById("city").textContent = "Unknown";
  document.getElementById("postalCode").textContent = "Unknown";
});
} else {
document.getElementById("firstName").textContent = "Unknown";
document.getElementById("lastName").textContent = "Unknown";
document.getElementById("email").textContent = "Unknown";
document.getElementById("country").textContent = "Unknown";
document.getElementById("city").textContent = "Unknown";
document.getElementById("postalCode").textContent = "Unknown";
}
})
.catch(err => {
console.error("Error fetching auction info:", err);
});


document.getElementById("paymentForm").addEventListener("submit", function(e) {
  e.preventDefault();

  // payment details
  const cardHolder = document.getElementById("cardHolder").value.trim();
  const cardNumber = document.getElementById("cardNumber").value.trim();
  const expDate = document.getElementById("expDate").value.trim();
  const cvv = document.getElementById("cvv").value.trim();
  const totalDue = parseFloat(document.getElementById("totalAmount").textContent) || 0;

  if (!token) {
    alert("Token not found. Please login.");
    return;
  }

  const paymentUrl = `http://localhost:8080/api/payments/${auctionID}/10/pay`;


  const paymentData = {
    cardHolder,
    cardNumber,
    expDate,
    cvv,
    totalDue
  };
// send payment
fetch(paymentUrl, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            },
          })
            .then((paymentResponse) => {
              console.log(paymentResponse);
            })
            .then((paymentResults) => {
              alert("Thank you for your payment!");
              console.log("Payment success");
              window.location.replace(`/checkout/receipt?auctionId=${auctionID}`);
            })
            .catch((error) => {
              console.error("Error during payment:", error);
              alert(`Payment failed: ${error.message}`);
        
});
});