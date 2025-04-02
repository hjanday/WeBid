window.logout = async function () {
    try {
        await fetch('/auth/logout', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
            },
            credentials: 'include' 
        });

        localStorage.removeItem('jwtToken');
        sessionStorage.clear();

        window.location.replace('/authentication/login');
    } catch (error) {
        console.error('Logout failed:', error);
        alert('Error logging out. Please try again.');
    }
}