const auctionTypeSelect = document.getElementById("auctionType");
    const lowestBidGroup = document.getElementById("lowestBidGroup");
    const currentBidGroup = document.getElementById("currentBidGroup");

    const lowestBidInput = document.getElementById("lowestBid");
    const currentBidInput = document.getElementById("currentBid");

// add listener for each form  
    auctionTypeSelect.addEventListener("change", function() {
      if (this.value === "DUTCH") {
      
        currentBidGroup.style.display = "block";
        currentBidInput.required = true;

        lowestBidGroup.style.display = "block";
        lowestBidInput.required = false;
        lowestBidInput.value = ""; 
      } else {
    
        currentBidGroup.style.display = "none";
        currentBidInput.required = false;
        currentBidInput.value = ""; 

        lowestBidGroup.style.display = "block";
        lowestBidInput.required = true;
      }
    });

    document.getElementById("auctionForm").addEventListener("submit", function(event) {
      event.preventDefault();

      // get data frm form
      const itemName = document.getElementById("itemName").value;
      const description = document.getElementById("description").value;
      const bidIncrement = parseFloat(document.getElementById("bidIncrement").value);
      const auctionType = auctionTypeSelect.value;
      const expeditedShippingCost = parseFloat(document.getElementById("expeditedShippingCost").value);
  
      const token = localStorage.getItem("jwtToken");
      if (!token) {
        alert("User is not logged in.");
        return;
      }

     
      const auction = {
        itemName,
        description,
        bidIncrement,
        auctionType,
        expeditedShippingCost,
        startTime: new Date().toISOString()
      };
     
      // NOTE vv
      // For DUTCH auctions, include currentBid
      // For FORWARD auctions, include lowestBid
      if (auctionType === "DUTCH") {
        auction.currentBid = parseFloat(currentBidInput.value);
        auction.lowestBid = parseFloat(lowestBidInput.value);
      } else {
        auction.lowestBid = parseFloat(lowestBidInput.value);
      }

// build auction object  
      fetch("http://localhost:8080/api/auctions/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(auction)
      })
      .then(response => {
        if (!response.ok) {
          return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
      })
      .then(data => {
        alert("Auction created successfully!");
        console.log("Created Auction:", data);
      
        window.location.replace("/auth/auctions");
      })
      .catch(error => {
        alert("Error creating auction: " + error.message);
      });
    });