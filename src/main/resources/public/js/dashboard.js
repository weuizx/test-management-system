import { makeAuthenticatedRequest, fetchData } from './api.js';

const API_BASE_URL = 'http://localhost:8080/api/v1';
let currentUser = null;
let selectedProjectId = null;
let currentTestCaseId = null;
let currentTestCycleId = null;

document.addEventListener('DOMContentLoaded', async () => {
    await checkAuth();
    setupTabs();
    setupModals();
    loadInitialData();
    setupLogout();
});

async function checkAuth() {
    const token = localStorage.getItem('accessToken');
    if (!token) {
        window.location.href = '../auth/login.html';
        return;
    }
    
    try {
        const userId = getUserIdFromToken(token);
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/users/${userId}`);
        currentUser = await response.json();
    } catch (error) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '../auth/login.html';
    }
}

function getUserIdFromToken(token) {
    try {
        // Разбиваем токен на части: header.payload.signature
        const payloadBase64 = token.split('.')[1];
        // Декодируем Base64 (учитываем замену символов)
        const decodedPayload = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
        // Парсим JSON и достаем userId
        const payload = JSON.parse(decodedPayload);
        // Проверяем разные возможные поля (sub, userId, id)
        return payload.userId || payload.sub || payload.id;
    } catch (error) {
        console.error('Ошибка при декодировании токена:', error);
        return null;
    }
}

function setupTabs() {
    const tabs = document.querySelectorAll('.tab-btn');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            document.querySelectorAll('.tab-btn').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
            
            tab.classList.add('active');
            const tabId = `${tab.dataset.tab}-tab`;
            document.getElementById(tabId).classList.add('active');
            
            switch(tab.dataset.tab) {
                case 'projects': loadProjects(); break;
                case 'releases': loadReleases(selectedProjectId); break;
                case 'test-plans': loadTestPlans(); break;
                case 'test-cycles': loadTestCycles(selectedProjectId); break;
                case 'test-cases': loadTestCases(selectedProjectId); break;
                case 'executions': setupExecutionsTab(); break;
                case 'statistics': setupStatisticsTab(); break;
            }
        });
    });
}

function setupModals() {
    const modalOverlay = document.getElementById('modalOverlay');
    
    document.querySelector('.close-btn').addEventListener('click', () => {
        modalOverlay.style.display = 'none';
    });
    
    modalOverlay.addEventListener('click', (e) => {
        if (e.target === modalOverlay) {
            modalOverlay.style.display = 'none';
        }
    });
    
    document.getElementById('createProjectBtn').addEventListener('click', () => showCreateForm('project'));
    document.getElementById('createReleaseBtn').addEventListener('click', () => showCreateForm('release'));
    document.getElementById('createTestPlanBtn').addEventListener('click', () => showCreateForm('test-plan'));
    document.getElementById('createTestCycleBtn').addEventListener('click', () => showCreateForm('test-cycle'));
    document.getElementById('createTestCaseBtn').addEventListener('click', () => showCreateForm('test-case'));

    // Обработчики для кнопок выполнения
        document.getElementById('executeTestCaseBtn').addEventListener('click', () => {
            currentTestCaseId = document.getElementById('testCaseSelect').value;
            if (currentTestCaseId) {
                showExecuteTestCaseModal();
            } else {
                showErrorMessage('Выберите тест-кейс');
            }
        });

        document.getElementById('executeTestCycleBtn').addEventListener('click', () => {
            currentTestCycleId = document.getElementById('testCycleSelect').value;
            if (currentTestCycleId) {
                showExecuteTestCycleModal();
            } else {
                showErrorMessage('Выберите тест-цикл');
            }
        });

        // Обработчики для форм выполнения
        document.getElementById('executeTestCaseForm').addEventListener('submit', handleTestCaseExecution);
        document.getElementById('executeTestCycleForm').addEventListener('submit', handleTestCycleExecution);

        // Закрытие модальных окон
        document.querySelectorAll('#executeTestCaseModal .close-btn, #executeTestCycleModal .close-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                document.getElementById('executeTestCaseModal').style.display = 'none';
                document.getElementById('executeTestCycleModal').style.display = 'none';
            });
        });

        document.querySelectorAll('#executeTestCaseModal, #executeTestCycleModal').forEach(modal => {
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    modal.style.display = 'none';
                }
            });
        });

        document.getElementById('loadTestCaseExecutionsBtn').addEventListener('click', loadTestCaseExecutions);
        document.getElementById('loadTestCycleExecutionsBtn').addEventListener('click', loadTestCycleExecutions);
}

function setupExecutionsTab() {
    document.getElementById('loadTestCaseExecutionsBtn').addEventListener('click', loadTestCaseExecutions);
    document.getElementById('loadTestCycleExecutionsBtn').addEventListener('click', loadTestCycleExecutions);
}

async function loadTestCaseExecutions() {
    try {
        const executions = await fetchData(`${API_BASE_URL}/test-case-execution`);
        const container = document.getElementById('testCaseExecutionsList');
        container.innerHTML = ''; // Очищаем контейнер перед добавлением новых данных
    
        if (executions.length === 0) {
          container.innerHTML = '<p class="no-data">Нет данных о выполнении тест-кейсов</p>';
          return;
        }
        
        const userPromises = executions.map(execution =>
            fetchData(`${API_BASE_URL}/users/${execution.userId}`)
        );
        const users = await Promise.all(userPromises);

        // Создаем карточки для каждого выполнения тест-кейса
        executions.forEach((execution, index) => {
          const card = document.createElement('div');
          card.className = `execution-card status-${execution.state.toLowerCase()}`;
          card.innerHTML = `
            <div class="card-header">
              <span class="execution-id">#${execution.id}</span>
              <span class="test-case-id">Test Case: ${execution.testCaseId}</span>
            </div>
            <div class="card-body">
              <div class="execution-info">
                <span class="date">${new Date(execution.executionDateTime).toLocaleString()}</span>
                <span class="user">User: ${users[index].username}</span>
              </div>
              <div class="execution-status">
                <span class="state">${execution.state}</span>
                ${execution.result ? `<span class="result">${execution.result}</span>` : ''}
              </div>
            </div>
          `;
          container.appendChild(card);
        });
    } catch (error) {
        showErrorMessage(error.message);
        document.getElementById('testCaseExecutionsList').innerHTML = 
          '<p class="error">Ошибка при загрузке данных о выполнении тест-кейсов</p>';
    }
}

async function loadTestCycleExecutions() {
  try {
    const executions = await fetchData(`${API_BASE_URL}/test-cycle-execution`);
    const container = document.getElementById('testCycleExecutionsList');
    container.innerHTML = ''; // Очищаем контейнер
    
    if (executions.length === 0) {
      container.innerHTML = '<p class="no-data">Нет данных о выполнении тест-циклов</p>';
      return;
    }

    const userPromises = executions.map(execution =>
        fetchData(`${API_BASE_URL}/users/${execution.userId}`)
    );
    const users = await Promise.all(userPromises);

    // Создаем карточки для каждого выполнения тест-цикла
    executions.forEach((execution, index) => {
      const card = document.createElement('div');
      card.className = 'cycle-execution-card';
      card.innerHTML = `
        <div class="card-header">
          <span class="execution-id">#${execution.id}</span>
          <span class="test-cycle-id">Test Cycle: ${execution.testCycleId}</span>
        </div>
        <div class="card-body">
          <div class="execution-info">
            <span class="date">${new Date(execution.executionDateTime).toLocaleString()}</span>
            <span class="user">User: ${users[index].username}</span>
          </div>
          <div class="execution-stats">
            <div class="stat passed">
              <span class="count">${execution.testsPassed}</span>
              <span class="label">Passed</span>
            </div>
            <div class="stat failed">
              <span class="count">${execution.testsFail}</span>
              <span class="label">Failed</span>
            </div>
            <div class="stat not-executed">
              <span class="count">${execution.testsNotExecuted}</span>
              <span class="label">Not Executed</span>
            </div>
            <div class="stat skipped">
              <span class="count">${execution.testsSkipped}</span>
              <span class="label">Skipped</span>
            </div>
            <div class="stat blocked">
              <span class="count">${execution.testsBlocked}</span>
              <span class="label">Blocked</span>
            </div>
          </div>
        </div>
      `;
      container.appendChild(card);
    });
    
  } catch (error) {
    showErrorMessage(error.message);
    document.getElementById('testCycleExecutionsList').innerHTML = 
      '<p class="error">Ошибка при загрузке данных о выполнении тест-циклов</p>';
  }
}

function setupStatisticsTab() {
    document.getElementById('statisticsType').addEventListener('change', updateStatisticsFilters);
    document.getElementById('loadStatisticsBtn').addEventListener('click', loadStatistics);
}

async function updateStatisticsFilters() {
    const type = document.getElementById('statisticsType').value;
    const select = document.getElementById('statisticsEntityId');
    
    if (type === 'project') {
        try {
            const response = await makeAuthenticatedRequest(`${API_BASE_URL}/projects`);
            const projects = await response.json();
            select.innerHTML = '<option value="" disabled selected>Выберите проект</option>';
            select.innerHTML += projects.map(project =>
                `<option value="${project.id}">${project.name}</option>`
            ).join('');
        } catch (error) {
            showErrorMessage(error.message);
        }
    } else {
        if (!selectedProjectId) {
            showErrorMessage('Сначала выберите проект в соответствующей вкладке');
            return;
        }

        try {
            const response = await makeAuthenticatedRequest(`${API_BASE_URL}/releases?projectId=${selectedProjectId}`);
            const releases = await response.json();
            select.innerHTML = '<option value="" disabled selected>Выберите релиз</option>';
            select.innerHTML += releases.map(release =>
                `<option value="${release.id}">${release.name}</option>`
            ).join('');
        } catch (error) {
            showErrorMessage(error.message);
        }
    }
}

function showCreateForm(type) {
    const modalOverlay = document.getElementById('modalOverlay');
    const modalTitle = document.getElementById('modalTitle');
    const formContainer = document.getElementById('modalFormContainer');
    
    modalTitle.textContent = `Создать ${getEntityName(type)}`;
    
    let formHTML = '';
    switch(type) {
        case 'project':
            formHTML = `
                <form id="createForm">
                    <div class="form-group">
                        <label for="name">Название</label>
                        <input type="text" id="name" class="form-control" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Создать</button>
                </form>
            `;
            break;
        case 'release':
            formHTML = `
                <form id="createForm">
                    <div class="form-group">
                        <label for="name">Название</label>
                        <input type="text" id="name" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="description">Описание</label>
                        <textarea id="description" class="form-control"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="projectId">Проект</label>
                        <select id="projectId" class="form-control" required></select>
                    </div>
                    <button type="submit" class="btn btn-primary">Создать</button>
                </form>
            `;
            break;
        case 'test-plan':
            formHTML = `
                <form id="createForm">
                    <div class="form-group">
                        <label for="name">Название</label>
                        <input type="text" id="name" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="description">Описание</label>
                        <textarea id="description" class="form-control"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="releaseId">Релиз</label>
                        <select id="releaseId" class="form-control" required></select>
                    </div>
                    <button type="submit" class="btn btn-primary">Создать</button>
                </form>
            `;
            break;
        case 'test-cycle':
            formHTML = `
                <form id="createForm">
                    <div class="form-group">
                        <label for="name">Название</label>
                        <input type="text" id="name" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="description">Описание</label>
                        <textarea id="description" class="form-control"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="projectId">Проект</label>
                        <select id="projectId" class="form-control" required></select>
                    </div>
                    <button type="submit" class="btn btn-primary">Создать</button>
                </form>
            `;
            break;
        case 'test-case':
            formHTML = `
                <form id="createForm">
                    <div class="form-group">
                        <label for="name">Название</label>
                        <input type="text" id="name" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="precondition">Предусловие</label>
                        <textarea id="precondition" class="form-control"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="projectId">Проект</label>
                        <select id="projectId" class="form-control" required></select>
                    </div>
                    <button type="submit" class="btn btn-primary">Создать</button>
                </form>
            `;
            break;
    }
    
    formContainer.innerHTML = formHTML;
    modalOverlay.style.display = 'flex';
    
    if (type === 'release' || type === 'test-cycle' || type === 'test-case') {
        loadProjectsForSelect();
        if (selectedProjectId) {
            document.getElementById('projectId').value = selectedProjectId;
        }
    }
    if (type === 'test-plan') {
        loadReleasesForSelect(selectedProjectId);
    }
    
    document.getElementById('createForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await handleCreateFormSubmit(type);
    });
}

async function handleCreateFormSubmit(type) {
    try {
        let response;
        
        switch(type) {
            case 'project':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/projects`, 'POST', {
                    name: document.getElementById('name').value
                });
                break;
            case 'release':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/releases`, 'POST', {
                    name: document.getElementById('name').value,
                    description: document.getElementById('description').value,
                    projectId: parseInt(document.getElementById('projectId').value)
                });
                break;
            case 'test-plan':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-plans`, 'POST', {
                    name: document.getElementById('name').value,
                    description: document.getElementById('description').value,
                    releaseId: parseInt(document.getElementById('releaseId').value),
                    state: "NOT_EXECUTED",
                    assigneeId: currentUser.id
                });
                break;
            case 'test-cycle':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cycles`, 'POST', {
                    name: document.getElementById('name').value,
                    description: document.getElementById('description').value,
                    projectId: parseInt(document.getElementById('projectId').value),
                    state: "NOT_EXECUTED",
                    assigneeId: currentUser.id
                });
                break;
            case 'test-case':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases`, 'POST', {
                    name: document.getElementById('name').value,
                    precondition: document.getElementById('precondition').value,
                    projectId: parseInt(document.getElementById('projectId').value),
                    state: "NOT_EXECUTED"
                });
                break;
        }
        
        const data = await response.json();
        showSuccessMessage(`${getEntityName(type)} успешно создан!`);
        document.getElementById('modalOverlay').style.display = 'none';
        
        switch(type) {
            case 'project': loadProjects(); break;
            case 'release': loadReleases(selectedProjectId); break;
            case 'test-plan': loadTestPlans(); break;
            case 'test-cycle': loadTestCycles(selectedProjectId); break;
            case 'test-case': loadTestCases(selectedProjectId); break;
        }
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadProjectsForSelect() {
    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/projects`);
        const projects = await response.json();
        const select = document.getElementById('projectId');
        
        select.innerHTML = projects.map(project => 
            `<option value="${project.id}">${project.name}</option>`
        ).join('');
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadReleasesForSelect(projectId = null) {
    try {
        if (projectId === null) {
            showErrorMessage("Выберите проект");
            return;
        }
        const url = `${API_BASE_URL}/releases?projectId=${projectId}`;
        const response = await makeAuthenticatedRequest(url);
        const releases = await response.json();
        const select = document.getElementById('releaseId');

        if (select != null) {
            select.innerHTML = releases.map(release => 
                `<option value="${release.id}">${release.name}</option>`
            ).join('');
        }
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadProjects() {
    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/projects`);
        const projects = await response.json();
        renderProjects(projects);
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadReleases(projectId = null) {
    try {
        if (projectId === null) {
            showErrorMessage("Выберите проект");
            return;
        }
        const url = `${API_BASE_URL}/releases?projectId=${projectId}`;
        const response = await makeAuthenticatedRequest(url);
        const releases = await response.json();
        renderReleases(releases);
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadTestPlans() {
    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-plans`);
        const testPlans = await response.json();
        const releasePromises = testPlans.map(plan =>
            fetchData(`${API_BASE_URL}/releases/${plan.releaseId}`)
        );
        const releases = await Promise.all(releasePromises);
        renderTestPlans(testPlans, releases);
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadTestCycles(projectId = null) {
    try {
        if (projectId === null) {
            showErrorMessage("Выберите проект");
            return;
        }
        const url = `${API_BASE_URL}/test-cycles?projectId=${projectId}`;
        const response = await makeAuthenticatedRequest(url);
        const testCycles = await response.json();
        renderTestCycles(testCycles);
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadTestCases(projectId = null) {
    try {
        if (projectId === null) {
            showErrorMessage("Выберите проект");
            return;
        }
        const url = `${API_BASE_URL}/test-cases?projectId=${projectId}`;
        const response = await makeAuthenticatedRequest(url);
        const testCases = await response.json();
        renderTestCases(testCases);
    } catch (error) {
        showErrorMessage(error.message);
    }
}

function renderProjects(projects) {
    const container = document.getElementById('projectsList');
    container.innerHTML = projects.map(project => `
        <div class="item-card ${selectedProjectId == project.id ? 'active' : ''}" data-id="${project.id}">
            <h4>${project.name}</h4>
            <div class="item-actions">
                <button class="btn btn-edit" data-id="${project.id}">Редактировать</button>
                <button class="btn btn-delete" data-id="${project.id}">Удалить</button>
            </div>
        </div>
    `).join('');
    
    addEditDeleteHandlers('project');
    
    document.querySelectorAll('.item-card').forEach(card => {
        card.addEventListener('click', (e) => {
            if (!e.target.classList.contains('btn')) {
                selectedProjectId = card.dataset.id;
                document.dispatchEvent(new Event('projectSelected'));
                document.querySelectorAll('.item-card').forEach(c => c.classList.remove('active'));
                card.classList.add('active');
                
                const activeTab = document.querySelector('.tab-btn.active');
                if (activeTab) {
                    switch(activeTab.dataset.tab) {
                        case 'releases': loadReleases(selectedProjectId); break;
                        case 'test-cycles': loadTestCycles(selectedProjectId); break;
                        case 'test-cases': loadTestCases(selectedProjectId); break;
                    }
                }
                loadReleasesForSelect(selectedProjectId);
            }
        });
    });
}

function renderReleases(releases) {
    const container = document.getElementById('releasesList');
    container.innerHTML = releases.map(release => `
        <div class="item-card" data-id="${release.id}">
            <h4>${release.name}</h4>
            <p>${release.description || ''}</p>
            <div class="item-actions">
                <button class="btn btn-edit" data-id="${release.id}">Редактировать</button>
                <button class="btn btn-delete" data-id="${release.id}">Удалить</button>
            </div>
        </div>
    `).join('');
    
    addEditDeleteHandlers('release');
}

function renderTestPlans(testPlans, releases) {
    const container = document.getElementById('testPlansList');
    container.innerHTML = testPlans.map((plan, index) => `
        <div class="item-card" data-id="${plan.id}">
            <h4>${plan.name}</h4>
            <p>${plan.description || ''}</p>
            <p>Статус: ${plan.state}</p>
            <p>Релиз: ${releases[index].name}</p>
            <div class="item-actions">
                <button class="btn btn-edit" data-id="${plan.id}">Редактировать</button>
                <button class="btn btn-delete" data-id="${plan.id}">Удалить</button>
                <button class="btn btn-manage-cycles" data-id="${plan.id}">Управление циклами</button>
            </div>
        </div>
    `).join('');
    
    addEditDeleteHandlers('test-plan');
    // Добавляем обработчик для кнопки управления циклами
    document.querySelectorAll('.btn-manage-cycles').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const testPlanId = e.target.dataset.id;
            showTestCyclesManagement(testPlanId);
        });
    });
}

function renderTestCycles(testCycles) {
    const container = document.getElementById('testCyclesList');
    container.innerHTML = testCycles.map(cycle => `
        <div class="item-card" data-id="${cycle.id}">
            <h4>${cycle.name}</h4>
            <p>${cycle.description || ''}</p>
            <p>Статус: ${cycle.state}</p>
            <div class="item-actions">
                <button class="btn btn-edit" data-id="${cycle.id}">Редактировать</button>
                <button class="btn btn-delete" data-id="${cycle.id}">Удалить</button>
                <button class="btn btn-manage-cases" data-id="${cycle.id}">Управление кейсами</button>
            </div>
        </div>
    `).join('');
    
    addEditDeleteHandlers('test-cycle');
    // Добавляем обработчик для кнопки управления кейсами
    document.querySelectorAll('.btn-manage-cases').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const testCycleId = e.target.dataset.id;
            showTestCasesManagement(testCycleId);
        });
    });
}

// Функция для управления тест-циклами в тест-плане
async function showTestCyclesManagement(testPlanId) {
    try {
        // Получаем тест-план и релиз
        let testPlan = await fetchData(`${API_BASE_URL}/test-plans/${testPlanId}`);
        const release = await fetchData(`${API_BASE_URL}/releases/${testPlan.releaseId}`);
        // Получаем все тест-циклы проекта
        const allTestCycles = await fetchData(`${API_BASE_URL}/test-cycles?projectId=${release.projectId}`);
        // Получаем тест-циклы, уже связанные с этим планом
        let linkedTestCycles = testPlan.testCycleIds || [];

        const modalOverlay = document.getElementById('modalOverlay');
        const modalTitle = document.getElementById('modalTitle');
        const formContainer = document.getElementById('modalFormContainer');
        
        modalTitle.textContent = `Управление тест-циклами для плана "${testPlan.name}"`;
        
        formContainer.innerHTML = `
            <div class="management-container">
                <h5>Доступные тест-циклы</h5>
                <div class="items-list" id="availableTestCycles">
                    ${allTestCycles.map(cycle => `
                        <div class="item-selectable ${linkedTestCycles.includes(cycle.id) ? 'selected' : ''}" data-id="${cycle.id}">
                            ${cycle.name}
                            <button class="btn btn-toggle ${linkedTestCycles.includes(cycle.id) ? 'btn-remove' : 'btn-add'}" 
                                    data-id="${cycle.id}">
                                ${linkedTestCycles.includes(cycle.id) ? 'Удалить' : 'Добавить'}
                            </button>
                        </div>
                    `).join('')}
                </div>
            </div>
            <button class="btn btn-primary" id="closeManagementBtn">Закрыть</button>
        `;
        
        modalOverlay.style.display = 'flex';
        
        // Обработчики для кнопок добавления/удаления
        document.querySelectorAll('#availableTestCycles .btn-toggle').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                const testCycleId = e.target.dataset.id;
                const item = e.target.closest('.item-selectable');
                
                try {
                    testPlan = await fetchData(`${API_BASE_URL}/test-plans/${testPlanId}`);
                    linkedTestCycles = testPlan.testCycleIds || [];
                    if (linkedTestCycles.includes(parseInt(testCycleId))) {
                        await removeTestCycleFromTestPlan(testPlan, testCycleId);
                        item.classList.remove('selected');
                        e.target.classList.remove('btn-remove');
                        e.target.classList.add('btn-add');
                        e.target.textContent = 'Добавить';
                    } else {
                        await addTestCycleToTestPlan(testPlan, testCycleId);
                        item.classList.add('selected');
                        e.target.classList.remove('btn-add');
                        e.target.classList.add('btn-remove');
                        e.target.textContent = 'Удалить';
                    }
                    showSuccessMessage('Изменения сохранены!');
                } catch (error) {
                    showErrorMessage(error.message);
                }
            });
        });
        
        document.getElementById('closeManagementBtn').addEventListener('click', () => {
            modalOverlay.style.display = 'none';
        });
    } catch (error) {
        showErrorMessage(error.message);
    }
}

// Функция для управления тест-кейсами в тест-цикле
async function showTestCasesManagement(testCycleId) {
    try {
        // Получаем тест-цикл
        let testCycle = await fetchData(`${API_BASE_URL}/test-cycles/${testCycleId}`);
        // Получаем все тест-кейсы проекта
        const allTestCases = await fetchData(`${API_BASE_URL}/test-cases?projectId=${testCycle.projectId}`);
        // Получаем тест-кейсы, уже связанные с этим циклом
        let linkedTestCases = testCycle.testCaseIds || [];
        
        const modalOverlay = document.getElementById('modalOverlay');
        const modalTitle = document.getElementById('modalTitle');
        const formContainer = document.getElementById('modalFormContainer');
        
        modalTitle.textContent = `Управление тест-кейсами для цикла "${testCycle.name}"`;
        
        formContainer.innerHTML = `
            <div class="management-container">
                <h5>Доступные тест-кейсы</h5>
                <div class="items-list" id="availableTestCases">
                    ${allTestCases.map(testCase => `
                        <div class="item-selectable ${linkedTestCases.includes(testCase.id) ? 'selected' : ''}" data-id="${testCase.id}">
                            ${testCase.name}
                            <button class="btn btn-toggle ${linkedTestCases.includes(testCase.id) ? 'btn-remove' : 'btn-add'}" 
                                    data-id="${testCase.id}">
                                ${linkedTestCases.includes(testCase.id) ? 'Удалить' : 'Добавить'}
                            </button>
                        </div>
                    `).join('')}
                </div>
            </div>
            <button class="btn btn-primary" id="closeManagementBtn">Закрыть</button>
        `;
        
        modalOverlay.style.display = 'flex';
        
        // Обработчики для кнопок добавления/удаления
        document.querySelectorAll('#availableTestCases .btn-toggle').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                const testCaseId = e.target.dataset.id;
                const item = e.target.closest('.item-selectable');
                
                try {
                    testCycle = await fetchData(`${API_BASE_URL}/test-cycles/${testCycleId}`);
                    linkedTestCases = testCycle.testCaseIds || [];
                    if (linkedTestCases.includes(parseInt(testCaseId))) {
                        await removeTestCaseFromTestCycle(testCycle, testCaseId);
                        item.classList.remove('selected');
                        e.target.classList.remove('btn-remove');
                        e.target.classList.add('btn-add');
                        e.target.textContent = 'Добавить';
                    } else {
                        await addTestCaseToTestCycle(testCycle, testCaseId);
                        item.classList.add('selected');
                        e.target.classList.remove('btn-add');
                        e.target.classList.add('btn-remove');
                        e.target.textContent = 'Удалить';
                    }
                    showSuccessMessage('Изменения сохранены!');
                } catch (error) {
                    showErrorMessage(error.message);
                }
            });
        });
        
        document.getElementById('closeManagementBtn').addEventListener('click', () => {
            modalOverlay.style.display = 'none';
        });
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function addTestCycleToTestPlan(testPlan, testCycleId) {
    const numericTestCycleId = parseInt(testCycleId);
    const updatedTestCycleIds = [...(testPlan.testCycleIds || []), numericTestCycleId];
    return makeAuthenticatedRequest(`${API_BASE_URL}/test-plans/${testPlan.id}`, 'PUT', {
        ...testPlan,
        testCycleIds: updatedTestCycleIds
    });
}

async function removeTestCycleFromTestPlan(testPlan, testCycleId) {
    const numericTestCycleId = parseInt(testCycleId);
    const updatedTestCycleIds = (testPlan.testCycleIds || []).filter(id => id !== numericTestCycleId);
    return makeAuthenticatedRequest(`${API_BASE_URL}/test-plans/${testPlan.id}`, 'PUT', {
        ...testPlan,
        testCycleIds: updatedTestCycleIds
    });
}

async function addTestCaseToTestCycle(testCycle, testCaseId) {
    const numericTestCaseId = parseInt(testCaseId);
    const updatedTestCaseIds = [...(testCycle.testCaseIds || []), numericTestCaseId];
    return makeAuthenticatedRequest(`${API_BASE_URL}/test-cycles/${testCycle.id}`, 'PUT', {
        ...testCycle,
        testCaseIds: updatedTestCaseIds
    });
}

async function removeTestCaseFromTestCycle(testCycle, testCaseId) {
    const numericTestCaseId = parseInt(testCaseId);
    const updatedTestCaseIds = (testCycle.testCaseIds || []).filter(id => id !== numericTestCaseId);
    return makeAuthenticatedRequest(`${API_BASE_URL}/test-cycles/${testCycle.id}`, 'PUT', {
        ...testCycle,
        testCaseIds: updatedTestCaseIds
    });
}

function renderTestCases(testCases) {
    const container = document.getElementById('testCasesList');
    container.innerHTML = testCases.map(testCase => `
        <div class="item-card" data-id="${testCase.id}">
            <h4>${testCase.name}</h4>
            <p>Предусловие: ${testCase.precondition || ''}</p>
            <p>Статус: ${testCase.state}</p>
            <div class="item-actions">
                <button class="btn btn-edit" data-id="${testCase.id}">Редактировать</button>
                <button class="btn btn-delete" data-id="${testCase.id}">Удалить</button>
                <button class="btn btn-steps" data-id="${testCase.id}">Шаги</button>
            </div>
        </div>
    `).join('');
    
    addEditDeleteHandlers('test-case');
    
    document.querySelectorAll('.btn-steps').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const testCaseId = e.target.dataset.id;
            showTestCaseSteps(testCaseId);
        });
    });
}

async function showTestCaseSteps(testCaseId) {
    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases/${testCaseId}/test-case-steps`);
        const steps = await response.json();
        
        const modalOverlay = document.getElementById('modalOverlay');
        const modalTitle = document.getElementById('modalTitle');
        const formContainer = document.getElementById('modalFormContainer');
        
        modalTitle.textContent = `Шаги тест-кейса`;
        
        formContainer.innerHTML = `
            <div class="steps-container">
                ${steps.map((step, index) => `
                    <div class="step-card" data-id="${step.id}">
                        <h5>Шаг ${index + 1}</h5>
                        <p><strong>Описание:</strong> ${step.description}</p>
                        <p><strong>Ожидаемый результат:</strong> ${step.expectedResult}</p>
                        <p><strong>Тестовые данные:</strong> ${step.testData}</p>
                        <div class="step-actions">
                            <button class="btn btn-edit-step" data-id="${step.id}">Редактировать</button>
                            <button class="btn btn-delete-step" data-id="${step.id}">Удалить</button>
                        </div>
                    </div>
                `).join('')}
            </div>
            <button id="addStepBtn" class="btn btn-primary">Добавить шаг</button>
        `;
        
        modalOverlay.style.display = 'flex';
        
        document.querySelectorAll('.btn-edit-step').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const stepId = e.target.dataset.id;
                editTestCaseStep(testCaseId, stepId);
            });
        });
        
        document.querySelectorAll('.btn-delete-step').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const stepId = e.target.dataset.id;
                deleteTestCaseStep(testCaseId, stepId);
            });
        });
        
        document.getElementById('addStepBtn').addEventListener('click', () => {
            addTestCaseStep(testCaseId);
        });
    } catch (error) {
        showErrorMessage(error.message);
    }
}

function addEditDeleteHandlers(type) {
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = e.target.dataset.id;
            showEditForm(type, id);
        });
    });
    
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = e.target.dataset.id;
            deleteEntity(type, id);
        });
    });
}

async function showEditForm(type, id) {
    try {
        let response = await makeAuthenticatedRequest(`${API_BASE_URL}/${typeToEndpoint(type)}/${id}`);
        const entity = await response.json();
        
        const modalOverlay = document.getElementById('modalOverlay');
        const modalTitle = document.getElementById('modalTitle');
        const formContainer = document.getElementById('modalFormContainer');
        
        modalTitle.textContent = `Редактировать ${getEntityName(type)}`;
        
        let formHTML = '';
        switch(type) {
            case 'project':
                formHTML = `
                    <form id="editForm">
                        <div class="form-group">
                            <label for="name">Название</label>
                            <input type="text" id="name" class="form-control" value="${entity.name}" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </form>
                `;
                break;
            case 'release':
                formHTML = `
                    <form id="editForm">
                        <div class="form-group">
                            <label for="name">Название</label>
                            <input type="text" id="name" class="form-control" value="${entity.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Описание</label>
                            <textarea id="description" class="form-control">${entity.description || ''}</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </form>
                `;
                break;
            case 'test-plan':
                formHTML = `
                    <form id="editForm">
                        <div class="form-group">
                            <label for="name">Название</label>
                            <input type="text" id="name" class="form-control" value="${entity.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Описание</label>
                            <textarea id="description" class="form-control">${entity.description || ''}</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </form>
                `;
                break;
            case 'test-cycle':
                formHTML = `
                    <form id="editForm">
                        <div class="form-group">
                            <label for="name">Название</label>
                            <input type="text" id="name" class="form-control" value="${entity.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Описание</label>
                            <textarea id="description" class="form-control">${entity.description || ''}</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </form>
                `;
                break;
            case 'test-case':
                formHTML = `
                    <form id="editForm">
                        <div class="form-group">
                            <label for="name">Название</label>
                            <input type="text" id="name" class="form-control" value="${entity.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="precondition">Предусловие</label>
                            <textarea id="precondition" class="form-control">${entity.precondition || ''}</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </form>
                `;
                break;
        }
        
        formContainer.innerHTML = formHTML;
        modalOverlay.style.display = 'flex';
        
        document.getElementById('editForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            await handleEditFormSubmit(type, id);
        });
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function handleEditFormSubmit(type, id) {
    try {
        let response;
        
        switch(type) {
            case 'project':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/projects/${id}`, 'PUT', {
                    name: document.getElementById('name').value
                });
                break;
            case 'release':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/releases/${id}`, 'PUT', {
                    name: document.getElementById('name').value,
                    description: document.getElementById('description').value
                });
                break;
            case 'test-plan':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-plans/${id}`, 'PUT', {
                    name: document.getElementById('name').value,
                    description: document.getElementById('description').value
                });
                break;
            case 'test-cycle':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cycles/${id}`, 'PUT', {
                    name: document.getElementById('name').value,
                    description: document.getElementById('description').value
                });
                break;
            case 'test-case':
                response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases/${id}`, 'PUT', {
                    name: document.getElementById('name').value,
                    precondition: document.getElementById('precondition').value
                });
                break;
        }
        
        const data = await response.json();
        showSuccessMessage(`${getEntityName(type)} успешно обновлен!`);
        document.getElementById('modalOverlay').style.display = 'none';
        
        switch(type) {
            case 'project': loadProjects(); break;
            case 'release': loadReleases(selectedProjectId); break;
            case 'test-plan': loadTestPlans(); break;
            case 'test-cycle': loadTestCycles(selectedProjectId); break;
            case 'test-case': loadTestCases(selectedProjectId); break;
        }
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function deleteEntity(type, id) {
    if (!confirm(`Вы уверены, что хотите удалить этот ${getEntityName(type)}?`)) {
        return;
    }
    
    try {
        await makeAuthenticatedRequest(`${API_BASE_URL}/${typeToEndpoint(type)}/${id}`, 'DELETE');
        showSuccessMessage(`${getEntityName(type)} успешно удален!`);
        
        switch(type) {
            case 'project': 
                loadProjects(); 
                selectedProjectId = null;
                break;
            case 'release': loadReleases(selectedProjectId); break;
            case 'test-plan': loadTestPlans(); break;
            case 'test-cycle': loadTestCycles(selectedProjectId); break;
            case 'test-case': loadTestCases(selectedProjectId); break;
        }
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function addTestCaseStep(testCaseId) {
    const modalOverlay = document.getElementById('modalOverlay');
    const modalTitle = document.getElementById('modalTitle');
    const formContainer = document.getElementById('modalFormContainer');
    
    modalTitle.textContent = `Добавить шаг тест-кейса`;
    
    formContainer.innerHTML = `
        <form id="stepForm">
            <div class="form-group">
                <label for="description">Описание</label>
                <textarea id="description" class="form-control" required></textarea>
            </div>
            <div class="form-group">
                <label for="expectedResult">Ожидаемый результат</label>
                <textarea id="expectedResult" class="form-control" required></textarea>
            </div>
            <div class="form-group">
                <label for="testData">Тестовые данные</label>
                <textarea id="testData" class="form-control"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Сохранить</button>
        </form>
    `;
    
    modalOverlay.style.display = 'flex';
    
    document.getElementById('stepForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        try {
            await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases/${testCaseId}/test-case-steps`, 'POST', {
                description: document.getElementById('description').value,
                expectedResult: document.getElementById('expectedResult').value,
                testData: document.getElementById('testData').value,
                testCaseId: parseInt(testCaseId)
            });
            
            showSuccessMessage('Шаг успешно добавлен!');
            showTestCaseSteps(testCaseId);
        } catch (error) {
            showErrorMessage(error.message);
        }
    });
}

async function editTestCaseStep(testCaseId, stepId) {
    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases/${testCaseId}/test-case-steps`);
        const steps = await response.json();
        const step = steps.find(s => s.id == stepId);
        
        if (!step) throw new Error('Шаг не найден');
        
        const modalOverlay = document.getElementById('modalOverlay');
        const modalTitle = document.getElementById('modalTitle');
        const formContainer = document.getElementById('modalFormContainer');
        
        modalTitle.textContent = `Редактировать шаг тест-кейса`;
        
        formContainer.innerHTML = `
            <form id="stepForm">
                <div class="form-group">
                    <label for="description">Описание</label>
                    <textarea id="description" class="form-control" required>${step.description}</textarea>
                </div>
                <div class="form-group">
                    <label for="expectedResult">Ожидаемый результат</label>
                    <textarea id="expectedResult" class="form-control" required>${step.expectedResult}</textarea>
                </div>
                <div class="form-group">
                    <label for="testData">Тестовые данные</label>
                    <textarea id="testData" class="form-control">${step.testData || ''}</textarea>
                </div>
                <button type="submit" class="btn btn-primary">Сохранить</button>
            </form>
        `;
        
        modalOverlay.style.display = 'flex';
        
        document.getElementById('stepForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            try {
                await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases/${testCaseId}/test-case-steps`, 'PUT', {
                    id: stepId,
                    description: document.getElementById('description').value,
                    expectedResult: document.getElementById('expectedResult').value,
                    testData: document.getElementById('testData').value,
                    testCaseId: parseInt(testCaseId)
                });
                
                showSuccessMessage('Шаг успешно обновлен!');
                showTestCaseSteps(testCaseId);
            } catch (error) {
                showErrorMessage(error.message);
            }
        });
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function deleteTestCaseStep(testCaseId, stepId) {
    if (!confirm('Вы уверены, что хотите удалить этот шаг?')) {
        return;
    }
    
    try {
        await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases/${testCaseId}/test-case-steps?testCaseId=${stepId}`, 'DELETE');
        showSuccessMessage('Шаг успешно удален!');
        showTestCaseSteps(testCaseId);
    } catch (error) {
        showErrorMessage(error.message);
    }
}

function typeToEndpoint(type) {
    const endpoints = {
        'project': 'projects',
        'release': 'releases',
        'test-plan': 'test-plans',
        'test-cycle': 'test-cycles',
        'test-case': 'test-cases'
    };
    return endpoints[type] || type;
}

function getEntityName(type) {
    const names = {
        'project': 'проект',
        'release': 'релиз',
        'test-plan': 'тест-план',
        'test-cycle': 'тест-цикл',
        'test-case': 'тест-кейс'
    };
    return names[type] || type;
}

function showSuccessMessage(message) {
    const toast = document.createElement('div');
    toast.className = 'toast toast-success';
    toast.textContent = message;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

function showErrorMessage(message) {
    const toast = document.createElement('div');
    toast.className = 'toast toast-error';
    toast.textContent = `Ошибка: ${message}`;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

function setupLogout() {
    document.getElementById('logoutBtn').addEventListener('click', () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '../auth/login.html';
    });
}

function loadInitialData() {
    loadProjects();
    
    // Добавить обработчик выбора проекта
    document.addEventListener('projectSelected', () => {
        loadTestCasesForExecution();
        loadTestCyclesForExecution();
    });
}

async function loadTestCasesForExecution() {
    if (!selectedProjectId) {
        showErrorMessage('Сначала выберите проект');
        return;
    }
    
    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cases?projectId=${selectedProjectId}`);
        const testCases = await response.json();
        const select = document.getElementById('testCaseSelect');
        
        select.innerHTML = '<option value="" disabled selected>Выберите тест-кейс</option>';
        select.innerHTML += testCases.map(testCase => 
            `<option value="${testCase.id}">${testCase.name}</option>`
        ).join('');
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadTestCyclesForExecution() {
    if (!selectedProjectId) {
        showErrorMessage('Сначала выберите проект');
        return;
    }
    
    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cycles?projectId=${selectedProjectId}`);
        const testCycles = await response.json();
        const select = document.getElementById('testCycleSelect');
        
        select.innerHTML = '<option value="" disabled selected>Выберите тест-цикл</option>';
        select.innerHTML += testCycles.map(testCycle => 
            `<option value="${testCycle.id}">${testCycle.name}</option>`
        ).join('');
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function loadStatistics() {
    const type = document.getElementById('statisticsType').value;
    const entityId = document.getElementById('statisticsEntityId').value;

    if (!entityId) {
        showErrorMessage(`Выберите ${type === 'project' ? 'проект' : 'релиз'}`);
        return;
    }

    try {
        const url = type === 'project'
            ? `${API_BASE_URL}/statistic/project?projectId=${entityId}`
            : `${API_BASE_URL}/statistic/release?releaseId=${entityId}`;

        const response = await makeAuthenticatedRequest(url);
        const statistics = await response.json();
        renderStatistics(statistics);
    } catch (error) {
        showErrorMessage(error.message);
    }
}

function renderStatistics(statistics) {
    // Обновляем цифровые значения
    document.getElementById('passedCount').textContent = statistics.testsPassed || 0;
    document.getElementById('failedCount').textContent = statistics.testsFailed || 0;
    document.getElementById('notExecutedCount').textContent = statistics.testsNotExecuted || 0;
    document.getElementById('blockedCount').textContent = statistics.testsBlocked || 0;
    document.getElementById('skippedCount').textContent = statistics.testsSkipped || 0;

    // Обновляем график (он сам проверит необходимость обновления)
    renderStatisticsChart(statistics);
}

// Глобальная переменная для хранения экземпляра графика
let statisticsChartInstance = null;

function renderStatisticsChart(statistics) {
    const ctx = document.getElementById('statisticsChart');
    if (!ctx) return;

    // Уничтожаем предыдущий график, если он существует
    if (statisticsChartInstance) {
        statisticsChartInstance.destroy();
        statisticsChartInstance = null;
    }

    // Проверяем, есть ли данные для отображения
    const total = (statistics.testsPassed || 0) + (statistics.testsFailed || 0) +
                (statistics.testsNotExecuted || 0) + (statistics.testsBlocked || 0) +
                (statistics.testsSkipped || 0);

    if (total === 0) {
        ctx.style.display = 'none';
        const noDataMsg = document.getElementById('noDataMessage') || document.createElement('div');
        noDataMsg.id = 'noDataMessage';
        noDataMsg.textContent = 'Нет данных для отображения';
        noDataMsg.style.textAlign = 'center';
        noDataMsg.style.margin = '20px 0';
        ctx.parentNode.insertBefore(noDataMsg, ctx.nextSibling);
        return;
    } else {
        ctx.style.display = 'block';
        const noDataMsg = document.getElementById('noDataMessage');
        if (noDataMsg) noDataMsg.remove();
    }

    // Создаем новый график
    try {
        statisticsChartInstance = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['Успешно', 'Провалено', 'Не выполнено', 'Заблокировано', 'Пропущено'],
                datasets: [{
                    data: [
                        statistics.testsPassed || 0,
                        statistics.testsFailed || 0,
                        statistics.testsNotExecuted || 0,
                        statistics.testsBlocked || 0,
                        statistics.testsSkipped || 0
                    ],
                    backgroundColor: [
                        '#4CAF50',
                        '#F44336',
                        '#9E9E9E',
                        '#FF9800',
                        '#2196F3'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'right',
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = total > 0
                                    ? Math.round((context.raw / total) * 100)
                                    : 0;
                                return `${context.label}: ${context.raw} (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Ошибка создания графика:', error);
        showErrorMessage('Ошибка при отображении графика');
    }
}

// Функции для показа модальных окон
function showExecuteTestCaseModal() {
    const modal = document.getElementById('executeTestCaseModal');
    modal.style.display = 'flex';
    // Сброс формы
    document.getElementById('testCaseStatus').value = 'NOT_EXECUTED';
    document.getElementById('testCaseComment').value = '';
}

function showExecuteTestCycleModal() {
    const modal = document.getElementById('executeTestCycleModal');
    modal.style.display = 'flex';
    // Сброс формы
    document.getElementById('testsPassed').value = '0';
    document.getElementById('testsFailed').value = '0';
    document.getElementById('testsNotExecuted').value = '0';
    document.getElementById('testsBlocked').value = '0';
    document.getElementById('testsSkipped').value = '0';
}

// Обработчики отправки форм
async function handleTestCaseExecution(e) {
    e.preventDefault();

    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-case-execution`, 'POST', {
            testCaseId: parseInt(currentTestCaseId),
            state: document.getElementById('testCaseStatus').value,
            executionDateTime: new Date().toISOString(),
            userId: currentUser.id,
            result: document.getElementById('testCaseComment').value || ''
        });

        const data = await response.json();
        showSuccessMessage('Результат выполнения тест-кейса сохранен!');
        document.getElementById('executeTestCaseModal').style.display = 'none';
        loadTestCaseExecutions();
    } catch (error) {
        showErrorMessage(error.message);
    }
}

async function handleTestCycleExecution(e) {
    e.preventDefault();

    try {
        const response = await makeAuthenticatedRequest(`${API_BASE_URL}/test-cycle-execution`, 'POST', {
            testCycleId: parseInt(currentTestCycleId),
            executionDateTime: new Date().toISOString(),
            userId: currentUser.id,
            testsPassed: parseInt(document.getElementById('testsPassed').value),
            testsFail: parseInt(document.getElementById('testsFailed').value),
            testsNotExecuted: parseInt(document.getElementById('testsNotExecuted').value),
            testsBlocked: parseInt(document.getElementById('testsBlocked').value),
            testsSkipped: parseInt(document.getElementById('testsSkipped').value)
        });

        const data = await response.json();
        showSuccessMessage('Результат выполнения тест-цикла сохранен!');
        document.getElementById('executeTestCycleModal').style.display = 'none';
        loadTestCycleExecutions();
    } catch (error) {
        showErrorMessage(error.message);
    }
}