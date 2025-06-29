package org.evilincorporated.pineapple.security.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum UserRole implements GrantedAuthority {
    ROLE_ADMIN("Администратор"),       // Администратор: редактировать пользователей, смотреть логи
    ROLE_TEST_MANAGER("Руководитель тестирования"),// Руководитель тестирования: может всё + смотреть статистику
    ROLE_TESTER("Тестировщик"),      // Тестировщик: может менять статусы тест-кейсов
    ROLE_TEST_ANALYST("аналитик"), // Тест-аналитик: может всё, кроме просмотра статистики

    ROLE_USER("Пользователь?"),
    ROLE_MANAGER("Менеджер?");

    private final String description;

    @Override
    public String getAuthority() {
        return name();
    }
}
