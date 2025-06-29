const API_BASE_URL = 'http://45.142.44.171:32769/api/v1';

document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorElement = document.getElementById('errorMessage');
    const loginBtn = document.getElementById('loginBtn');
    
    // Показываем индикатор загрузки
    loginBtn.disabled = true;
    loginBtn.innerHTML = `<div class="loading"></div>Вход...`;
    errorElement.style.display = 'none';
    
    try {
        // Basic Auth header
        const authHeader = 'Basic ' + btoa(username + ':' + password);
        
        const response = await fetch(`${API_BASE_URL}/jwt/tokens`, {
            method: 'POST',
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Ошибка входа. Проверьте данные.');
        }
        
        const tokens = await response.json();
        
        // Сохраняем токены
        localStorage.setItem('accessToken', tokens.accessToken);
        localStorage.setItem('refreshToken', tokens.refreshToken);
        
        // Перенаправляем в систему
        window.location.href = '../dashboard/index.html';
        
    } catch (error) {
        // errorElement.textContent = error.message;
        errorElement.textContent = 'Ошибка входа. Проверьте данные.';
        errorElement.style.display = 'block';
        loginBtn.disabled = false;
        loginBtn.innerHTML = '<span id="loginText">Войти</span>';
    }
});