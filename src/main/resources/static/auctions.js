let selectedAuction = null;
// hide bids
document.getElementById("bidNowButton").style.display = "none";
document.getElementById("buyNowButton").style.display = "none";
document.getElementById("editDutchBtn").style.display = "none";
// pick radio btn
function onRadioSelect(auction) {
    selectedAuction = auction;
    document.getElementById("bidNowButton").style.display = "block";
}

// update bid
document
.getElementById("bidNowButton")
.addEventListener("click", function () {
    if (!selectedAuction) return;
    if ((selectedAuction.auctionType || "").toLowerCase() === "forward") {
    showauctionSection(selectedAuction);
    } else if (
    (selectedAuction.auctionType || "").toLowerCase() === "dutch"
    ) {
    showauctionSectionDutch(selectedAuction);
    }
});

// fwd
function showauctionSection(auction) {
    document.getElementById("auctionTable").style.display = "none";
    document.getElementById("bidNowButton").style.display = "none";
    document.getElementById("buyNowButton").style.display = "none";
    document.getElementById("faPlaceBidButton").style.display = "block";
    document.getElementById("payNowButton").style.display = "block";
    document.getElementById("faItemName").textContent =
        auction.itemName || "";
    document.getElementById("faDescription").textContent =
        "Description: " + (auction.description || "N/A");
    document.getElementById("faShippingPrice").textContent =
        "Shipping Price: $" + auction.expeditedShippingCost; // Adjust if needed

    const currentBidText =
        auction.currentBid != null && auction.currentBid !== ""
        ? "$" + auction.currentBid
        : "No Bid Yet";
    document.getElementById("faCurrentPrice").textContent =
        "Current Price: " + currentBidText;

    const highestBidderText = auction.currentBidderID || "Unknown";
    document.getElementById("faHighestBidder").textContent =
        "Highest Bidder: " + highestBidderText;

    document.getElementById("auctionSection").style.display = "block";
}
// dutch
function showauctionSectionDutch(auction) {
document.getElementById("auctionTable").style.display = "none";
document.getElementById("bidNowButton").style.display = "none";
document.getElementById("faPlaceBidButton").style.display = "none";
document.getElementById("payNowButton").style.display = "none";
document.getElementById("buyNowButton").style.display = "block";
document.getElementById("faBidAmount").style.display = "none";
document.getElementById("bidLabel").style.display = "none";
document.getElementById("editDutchBtn").style.display = "block";

document.getElementById("faItemName").textContent =
    auction.itemName || "";
document.getElementById("faDescription").textContent =
    "Description: " + (auction.description || "N/A");
document.getElementById("faShippingPrice").textContent =
    "Shipping Price: $" + auction.expeditedShippingCost; // Adjust if needed

const currentBidText =
    auction.currentBid != null && auction.currentBid !== ""
    ? "$" + auction.currentBid
    : "No Bid Yet";
document.getElementById("faCurrentPrice").textContent =
    "Current Price: " + currentBidText;
document.getElementById("auctionSection").style.display = "block";
}

// edit dutch
document
.getElementById("editDutchBtn")
.addEventListener("click", function () {
    if (!selectedAuction) return;
    var editDutchUrl = `/api/auctions/dutch/${selectedAuction.id}`;
    fetch(editDutchUrl, {
    method: "PUT",
    headers: {
        Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
    },
    }).then((response) => {
    if (!response.ok) {
        alert("You are not authorized to edit this auction!");
        return response.text().then((text) => {
        throw new Error(text);
        });
    }
    alert("Dutch auction decremented successfully!");
    })
    .then((data) => {
        // Update UI with new auction information
        const newCurrentBid =
        data.currentBid != null && data.currentBid !== ""
            ? "$" + data.currentBid.toFixed(2)
            : "No Bid Yet";
        document.getElementById("faCurrentPrice").textContent =
            "Current Price: " + newCurrentBid;
        }
    })
    .catch(error => {
        console.error("Request failed:", error);
    });

// place bid
document
.getElementById("faPlaceBidButton")
.addEventListener("click", function () {
    if (!selectedAuction) return;
    const bidAmount = document.getElementById("faBidAmount").value;

    const token = localStorage.getItem("jwtToken");
    if (!token) {
    alert("User is not logged in.");
    return;
    }
    const url = `http://localhost:8080/api/bid/${selectedAuction.id}?bidAmount=${bidAmount}`;
    fetch(url, {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    },
    })
    .then((response) => {
        if (!response.ok) {
        // If response is not OK, handle the error
        return response.text().then((text) => {
            throw new Error(text); // The error message will be in the body
        });
        } else {
        // If the response is OK, parse the JSON response (which will contain the bid details)
        return response.json();
        }
    })
    .then((data) => {
        // If the response was successful, handle the bid data
        if (data && data.id) {
        // Assuming 'data' is a Bid object returned by the backend
        alert(
            `Bid placed successfully! Server response: ${JSON.stringify(
            data
            )}`
        );

        // Update UI with new bid information
        const auc = data.auction;
        const newCurrentBid =
            auc.currentBid != null && auc.currentBid !== ""
            ? "$" + auc.currentBid.toFixed(2)
            : "No Bid Yet";
        document.getElementById("faCurrentPrice").textContent =
            "Current Price: " + newCurrentBid;
        document.getElementById("faHighestBidder").textContent =
            "Highest Bidder: " + (auc.currentBidderID || "Unknown");

        if (calculateRemainingTime(auc.endTime) === "Expired") {
            alert("Auction is over! Please pay for your bid!");
            window.location.href = "/paynow";
        }
        }
    })
    .catch((error) => {
        // In case of error (from response body or other issues)
        alert(`Bid failed: ${error.message}`);
    });
});

document
.getElementById("payNowButton")
.addEventListener("click", function () {
    if (!selectedAuction) return;

    const bidAmount = document.getElementById("faBidAmount").value;
    const token = localStorage.getItem("jwtToken");
    if (!token) {
        alert("User is not logged in.");
        return;
    }

    const auctionUrl = `http://localhost:8080/api/auctions/${selectedAuction.id}`;
    const userUrl = `http://localhost:8080/api/users/currentuser`;

    // Fetch auction data
    Promise.all([
        fetch(auctionUrl, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }).then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch auction data");
            }
            return response.json();
        }),
    
        fetch(userUrl, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }).then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch user data");
            }
            return response.json();
        })
    ])
    .then(([auctionData, userData]) => {
        // check if auction is expired and is the correct type
        if (
            auctionData.auctionType &&
            auctionData.auctionType.toLowerCase() === "forward" &&
            calculateRemainingTime(auctionData.endTime) !== "Expired"
          ) {
            alert("Auction is still active. Payment is allowed only after the auction has expired.");
            return;
          }

        if (auctionData.currentBidderID !== userData.id) {
            alert("You are not the winner of this auction.");
            return;
        }
    
        // Redirect to the payment page with auction ID as a URL parameter
        window.location.href = `/paynow?auctionId=${selectedAuction.id}`;
    })
    .catch(error => {
        console.error("Error fetching data:", error);
        alert("Failed to retrieve data. Please try again.");
    });
});

// dutch buy now button
document
.getElementById("buyNowButton")
.addEventListener("click", function () {
    if (!selectedAuction) return;
    const token = localStorage.getItem("jwtToken");
    if (!token) {
    alert("User is not logged in.");
    return;
    }
    const url = `http://localhost:8080/api/auctions/complete/${selectedAuction.id}`;
    fetch(url, {
    method: "PUT",
    headers: { Authorization: `Bearer ${token}` },
    })
    .then((response) => {
        console.log(response);
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text);  // Extract error message from backend
              });
        }
        alert("Dutch auction completed successfully!");
        window.location.href = `/paynow?auctionId=${selectedAuction.id}`;
        return response;
    })
    .catch((error) => {
        alert("Dutch complete failed: " + error.message);
    });
});
// handle form
document
.getElementById("auctionForm")
.addEventListener("submit", function (event) {
    event.preventDefault();
    const itemName = document
    .getElementById("itemNameInput")
    .value.trim();
    const url =
    "/api/auctions/search?itemName=" + encodeURIComponent(itemName);

    fetch(url, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
    })
    .then((response) => {
        if (!response.ok) {
        return response.text().then((text) => {
            throw new Error(text);
        });
        }
        return response.json();
    })
    .then((data) => {
        document.getElementById("errorMessage").textContent = "";
        const tbody = document.getElementById("auctionTbody");
        tbody.innerHTML = "";
        selectedAuction = null;

        document.getElementById("auctionSection").style.display = "none";
        document.getElementById("bidNowButton").style.display = "none";

        if (data.length === 0) {
        document.getElementById("auctionTable").style.display = "none";
        document.getElementById("errorMessage").textContent =
            "No auctions found.";
        return;
        }

        document.getElementById("auctionTable").style.display = "table";

        data.forEach((auction) => {
        const row = document.createElement("tr");

        const cellItemName = document.createElement("td");
        cellItemName.textContent = auction.itemName || "";
        row.appendChild(cellItemName);

        const cellCurrentBid = document.createElement("td");
        cellCurrentBid.textContent =
            auction.currentBid != null && auction.currentBid !== ""
            ? "$" + auction.currentBid
            : "No Bid Yet";
        row.appendChild(cellCurrentBid);

        const cellAuctionType = document.createElement("td");
        cellAuctionType.textContent = auction.auctionType || "";
        row.appendChild(cellAuctionType);

        const cellRemainingTime = document.createElement("td");
        if ((auction.auctionType || "").toLowerCase() === "forward") {
            if (auction.endTime) {
            cellRemainingTime.textContent = calculateRemainingTime(
                auction.endTime
            );
            } else {
            cellRemainingTime.textContent = "N/A";
            }
        } else if (
            (auction.auctionType || "").toLowerCase() === "dutch"
        ) {
            cellRemainingTime.textContent = "now";
        } else {
            cellRemainingTime.textContent = "";
        }
        row.appendChild(cellRemainingTime);

        const cellSelect = document.createElement("td");
        const radio = document.createElement("input");
        radio.type = "radio";
        radio.name = "auctionSelect";
        radio.addEventListener("click", function () {
            onRadioSelect(auction);
        });
        cellSelect.appendChild(radio);
        row.appendChild(cellSelect);

        tbody.appendChild(row);
        });
    })
    .catch((error) => {
        document.getElementById("errorMessage").textContent =
        "Error: " + error.message;
        document.getElementById("auctionTable").style.display = "none";
        document.getElementById("auctionSection").style.display = "none";
    });
});

// ger
function calculateRemainingTime(endTime) {
    const now = new Date();
    const end = new Date(endTime);
    let diff = end - now;
    if (diff <= 0) {
        return "Expired";
    }
    const hours = Math.floor(diff / (1000 * 60 * 60));
    diff -= hours * (1000 * 60 * 60);
    const minutes = Math.floor(diff / (1000 * 60));
    diff -= minutes * (1000 * 60);
    const seconds = Math.floor(diff / 1000);
    return `${hours}h ${minutes}m ${seconds}s`;
}