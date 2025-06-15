const API_BASE_URL = 'http://45.142.44.171:8080/api/v1';

document.getElementById('registerForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const name = document.getElementById('name').value;
    const password = document.getElementById('password').value;
    const errorElement = document.getElementById('errorMessage');
    const successElement = document.getElementById('successMessage');
    const registerBtn = document.getElementById('registerBtn');
    
    // Показываем индикатор загрузки
    registerBtn.disabled = true;
    registerBtn.innerHTML = `<div class="loading"></div>Регистрация...`;
    errorElement.style.display = 'none';
    successElement.style.display = 'none';
    
    try {
        const response = await fetch(`${API_BASE_URL}/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username,
                name,
                password
            })
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Ошибка регистрации');
        }
        
        successElement.textContent = 'Регистрация прошла успешно! Перенаправляем на страницу входа...';
        successElement.style.display = 'block';
        
        // Перенаправляем на страницу входа через 1 секунду
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1000);
        
    } catch (error) {
        errorElement.textContent = 'Ошибка регистрации. Введите другое имя пользователя.';
        errorElement.style.display = 'block';
        registerBtn.disabled = false;
        registerBtn.innerHTML = '<span id="registerText">Зарегистрироваться</span>';
    }
});