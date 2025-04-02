const urlParams = new URLSearchParams(window.location.search);
    const auctionID = urlParams.get("auctionId");
    const auctionUrl = `http://localhost:8080/api/auctions/${auctionID}`;
    const token = localStorage.getItem("jwtToken");

window.onload = function () {
  fetch(auctionUrl, {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}`
    }
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to fetch auction data");
      }
      return response.json();
    })
    .then((data) => {
      document.getElementById("itemName").textContent =
        data.itemName || "Untitled";
      document.getElementById("itemDesc").textContent =
        data.description || "No description";
      document.getElementById("winningPrice").textContent =
        data.currentBid || 0;
      document.getElementById("highestBidder").textContent =
        data.currentBidderID || "N/A";

      document.getElementById("expship_label").textContent =
        "Express Shipping - $" + data.expeditedShippingCost;
      document
        .querySelector('input[name="shippingOption"][value="1"]')
        .setAttribute("amount", data.expeditedShippingCost);
    })
    .catch((error) => {
      console.error("Error fetching auction:", error);
      document.getElementById("itemDesc").textContent = "Error loading data";
    });
};


    
    const shippingOptions = document.querySelectorAll('input[name="shippingOption"]');
    shippingOptions.forEach((option) => {
      option.addEventListener("change", function () {
        if (this.value === "1") {
          document.getElementById("radioSelected").textContent =
            "Express shipping selected. Additional fees apply.";
            fetch("http://localhost:8080/api/auctions/" + auctionID + `?expShip=${true}`, {
              method: "PUT",
              headers: {"Authorization": `Bearer ${localStorage.getItem("jwtToken")}`} 
            })


        } else {
          document.getElementById("radioSelected").textContent = "No additional fees!";
          fetch("http://localhost:8080/api/auctions/" + auctionID + `?expShip=${false}`, {
              method: "PUT",
              headers: {"Authorization": `Bearer ${localStorage.getItem("jwtToken")}`} 
            })
        }
      });
    });

    document
      .getElementById("payNowButton")
      .addEventListener("click", function () {
        // winner price
        const winningPrice = parseFloat(
          document.getElementById("winningPrice").textContent
        ) || 0;

        // get ship cost
        let shippingCost = 0;
        shippingOptions.forEach((radio) => {
          if (radio.checked) {
            shippingCost = parseFloat(radio.getAttribute("amount")) || 0;
          }
        });

        const total = winningPrice + shippingCost;
        alert(`Total cost is $${total.toFixed(2)} (including shipping).`);
        window.location.href = `/checkout/payment?total=${total.toFixed(2)}&auctionId=${auctionID}`;
        
      });