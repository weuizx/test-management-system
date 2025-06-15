const API_BASE_URL = 'http://45.142.44.171:8080/api/v1';

// Общая функция для авторизованных запросов
export async function makeAuthenticatedRequest(url, method = 'GET', body = null) {
    let accessToken = localStorage.getItem('accessToken');
    
    try {
        const response = await fetch(url, {
            method,
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            },
            body: body ? JSON.stringify(body) : null
        });
        
        // Если токен просрочен, пробуем обновить
        if (response.status === 401 || response.status === 403) {
            const newAccessToken = await refreshToken();
            if (!newAccessToken) throw new Error('Ошибка аутентификации');
            
            // Повторяем запрос с новым токеном
            return await fetch(url, {
                method,
                headers: {
                    'Authorization': `Bearer ${newAccessToken}`,
                    'Content-Type': 'application/json'
                },
                body: body ? JSON.stringify(body) : null
            });
        }
        
        return response;
        
    } catch (error) {
        console.error('Ошибка запроса:', error);
        throw error;
    }
}

// Функция обновления токена (используется в makeAuthenticatedRequest)
export async function refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
        window.location.href = '../auth/login.html';
        return null;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/jwt/refresh`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${refreshToken}`,
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error('Ошибка обновления токена');
        }
        
        const tokens = await response.json();
        localStorage.setItem('accessToken', tokens.accessToken);
        localStorage.setItem('refreshToken', tokens.refreshToken);
        return tokens.accessToken;
        
    } catch (error) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '../auth/login.html';
        return null;
    }
}

export async function fetchData(path) {
    try {
        const response = await makeAuthenticatedRequest(path);
        if (!response.ok) throw new Error('Ошибка запроса');
        return await response.json();
    } catch (error) {
        console.error(`Ошибка при запросе ${path}:`, error);
        throw error;
    }
}

// export function showError(message) {
//     // Можно реализовать отображение ошибок в интерфейсе
//     console.error(message);
// }