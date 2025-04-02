
  const showUsersBtn = document.getElementById('showUsersBtn');
  const showAuctionsBtn = document.getElementById('showAuctionsBtn');
  const hideDataBtn = document.getElementById('hideDataBtn');
  const dataContainer = document.getElementById('dataContainer');
  const messageContainer = document.getElementById('messageContainer');
  const token = localStorage.getItem("jwtToken");

  // show delete msg
  function showMessage(text) {
    messageContainer.innerHTML = `<p class="message">${text}</p>`;
    setTimeout(() => {
      messageContainer.innerHTML = "";
    }, 3000);
  }

  // get user detail
  function getCurrentUsername() {
    if (!token) return null;
    try {
      // JWT token payload is the second part (after the first period)
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded.sub; // Adjust if needed (e.g., decoded.username)
    } catch (error) {
      console.error("Error decoding token:", error);
      return null;
    }
  }

  // show all users function
  function showUsers() {
    fetch('http://localhost:8080/api/users', {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch users.');
        }
        return response.json();
      })
      .then(users => {
        const currentUser = getCurrentUsername();
        // filter user
        const filteredUsers = users.filter(user => user.username.toLowerCase() !== currentUser.toLowerCase());
 // make table for user data
        let tableHTML = `
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Auctions</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
        `;
        filteredUsers.forEach(user => {
          tableHTML += `
            <tr id="user-${user.id}">
              <td>${user.username}</td>
              <td>${user.email}</td>
              <td>
                <button class="action-btn" onclick="showUserAuctions('${user.email}')">Show Auctions</button>
              </td>
              <td>
                <button class="danger-btn" onclick="deleteUser(${user.id})">Delete</button>
              </td>
            </tr>
          `;
        });
        tableHTML += `
            </tbody>
          </table>
        `;
        dataContainer.innerHTML = tableHTML;
      })
      .catch(error => {
        dataContainer.innerHTML = `<p style="color:red;">${error.message}</p>`;
      });
  }

  // remove user
  function deleteUser(userId) {
    fetch(`http://localhost:8080/api/users/${userId}`, {
      method: 'DELETE', 
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to delete user.');
        }
        // Remove the user row 
        const row = document.getElementById(`user-${userId}`);
        if (row) {
          row.remove();
        }
        showMessage("User deleted successfully.");
      })
      .catch(error => {
        alert(error.message);
      });
  }

  // fetch auc
  function showAuctions() {
    fetch('http://localhost:8080/api/auctions', {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch auctions.');
        }
        return response.json();
      })
      .then(auctions => {
        displayAuctions(auctions);
      })
      .catch(error => {
        dataContainer.innerHTML = `<p style="color:red;">${error.message}</p>`;
      });
  }

  // show all auctions function
  function displayAuctions(auctions) {
    let tableHTML = `
      <table>
        <thead>
          <tr>
            <th>Owner</th>
            <th>Item Name</th>
            <th>Current Bid</th>
            <th>Auction Type</th>
            <th>Remaining Time</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
    `;
    // get time for auc
    auctions.forEach(auction => {
      let remainingTime = '';
      if (auction.auctionType && auction.auctionType.toLowerCase() === 'forward' && auction.endTime) {
        const now = new Date();
        const end = new Date(auction.endTime);
        let diff = end - now;
        if (diff <= 0) {
          remainingTime = 'Expired';
        } else {
          const hours = Math.floor(diff / (1000 * 60 * 60));
          diff -= hours * (1000 * 60 * 60);
          const minutes = Math.floor(diff / (1000 * 60));
          diff -= minutes * (1000 * 60);
          const seconds = Math.floor(diff / 1000);
          remainingTime = `${hours}h ${minutes}m ${seconds}s`;
        }
      } else if (auction.auctionType && auction.auctionType.toLowerCase() === 'dutch') {
        remainingTime = 'Now';
      }
      tableHTML += `
        <tr id="auction-${auction.id}">
          <td>${auction.owner.email || ''}</td>
          <td>${auction.itemName || ''}</td>
          <td>${auction.currentBid ? '$' + auction.currentBid : 'No Bid Yet'}</td>
          <td>${auction.auctionType || ''}</td>
          <td>${remainingTime}</td>
          <td>
            <button class="danger-btn" onclick="deleteAuction(${auction.id})">Delete</button>
          </td>
        </tr>
      `;
    });
    tableHTML += `
        </tbody>
      </table>
    `;
    dataContainer.innerHTML = tableHTML;
  }

  //show user auctions function
  function showUserAuctions(userEmail) {
    fetch('http://localhost:8080/api/auctions', {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch auctions.');
        }
        return response.json();
      })
      .then(auctions => {
        // Filter auctions where the auction owner's email matches the given user email
       
        const filteredAuctions = auctions.filter(auction => auction.owner.email === userEmail
        );
        displayAuctions(filteredAuctions);
      })
      .catch(error => {
        dataContainer.innerHTML = `<p style="color:red;">${error.message}</p>`;
      });
  }

  // remove auction function
  function deleteAuction(auctionId) {
    fetch(`http://localhost:8080/api/auctions/${auctionId}`, {
      method: 'DELETE',
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to delete auction.');
        }
        // Remove the auction row from the table on success
        const row = document.getElementById(`auction-${auctionId}`);
        if (row) {
          row.remove();
        }
        showMessage("Auction deleted successfully.");
      })
      .catch(error => {
        alert(error.message);
      });
  }

  // hide data function
  function hideData() {
    dataContainer.innerHTML = '';
  }

 
  showUsersBtn.addEventListener('click', showUsers);
  showAuctionsBtn.addEventListener('click', showAuctions);
  hideDataBtn.addEventListener('click', hideData);