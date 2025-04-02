   
    function refreshToken() {
        fetch('/auth/refresh-cookies', {
        method: 'POST',
        credentials: 'include', 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem("jwtToken"),
            },
        })
        .then(response => {
            if (!response.ok) {
            throw new Error('Token refresh failed');
            }
            return response.json();
        })
        .then(data => {
            if (data.jwtToken) {
            localStorage.setItem("jwtToken", data.jwtToken);
            console.log('Token refreshed:', data.message);
            } else if (data.error) {
            console.error('Error refreshing token:', data.error);
            }
        })
        .catch(error => {
            console.error('Error during token refresh:', error);
        });
    }
    
    // hourly refresh (3600000 milliseconds)
    setInterval(refreshToken, 3600000);
    
    // also refresh on page load
    refreshToken();
    